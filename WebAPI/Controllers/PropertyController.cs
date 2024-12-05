using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WebAPI.Dtos;
using WebAPI.Interfaces;
using WebAPI.Models;
using WebAPI.Data;

namespace WebAPI.Controllers
{
    public class PropertyController : BaseController
    {
        private readonly IUnitOfWork uow;
        private readonly IMapper mapper;
        private readonly IPhotoService photoService;
        private readonly DataContext _context;

        public PropertyController(IUnitOfWork uow, IMapper mapper, IPhotoService photoService, DataContext context)
        {
            this.photoService = photoService;
            this.uow = uow;
            this.mapper = mapper;
            this._context = context; 
        }


        [HttpGet("list")]
        [AllowAnonymous]
        public async Task<IActionResult> GetPropertyList(int? sellRent = null, int? cityId = null, string sortBy = null)
        {
            var properties = await uow.PropertyRepository.GetPropertiesAsync(sellRent, cityId, sortBy);
            var propertyListDTO = mapper.Map<IEnumerable<PropertyListDto>>(properties);
            return Ok(propertyListDTO);
        }


        //property/detail/1
        [HttpGet("detail/{id}")]
        [AllowAnonymous]
        public async Task<IActionResult> GetPropertyDetail(int id)
        {
            var property = await uow.PropertyRepository.GetPropertyDetailAsync(id);
            var propertyDTO = mapper.Map<PropertyDetailDto>(property);
            return Ok(propertyDTO);
        }

        //property/add
        [HttpPost("add")]
        [Authorize]
        public async Task<IActionResult> AddProperty(PropertyDto propertyDto)
        {
            var property = mapper.Map<Property>(propertyDto);
            var userId = GetUserId();
            property.PostedBy = userId;
            property.LastUpdatedBy = userId;
            uow.PropertyRepository.AddProperty(property);
            await uow.SaveAsync();
            return StatusCode(201);
        }

        //property/add/photo/1
        [HttpPost("add/photo/{propId}")]
        [Authorize]
        public async Task<ActionResult<PhotoDto>> AddPropertyPhoto(IFormFile file, int propId)
        {
            var result = await photoService.UploadPhotoAsync(file);
            if (result.Error != null)
                return BadRequest(result.Error.Message);
            var userId = GetUserId();

            var property = await uow.PropertyRepository.GetPropertyByIdAsync(propId);

            if (property.PostedBy != userId)
                return BadRequest("You are not authorised to upload photo for this property");

            var photo = new Photo
            {
                ImageUrl = result.SecureUrl.AbsoluteUri,
                PublicId = result.PublicId
            };
            if (property.Photos.Count == 0)
            {
                photo.IsPrimary = true;
            }

            property.Photos.Add(photo);
            if (await uow.SaveAsync()) return mapper.Map<PhotoDto>(photo);

            return BadRequest("Some problem occured in uploading photo..");
        }

        //property/set-primary-photo/42/jl0sdfl20sdf2s
        [HttpPost("set-primary-photo/{propId}/{photoPublicId}")]
        [Authorize]
        public async Task<IActionResult> SetPrimaryPhoto(int propId, string photoPublicId)
        {
            var userId = GetUserId();

            var property = await uow.PropertyRepository.GetPropertyByIdAsync(propId);

            if (property.PostedBy != userId)
                return BadRequest("You are not authorised to change the photo");

            if (property == null || property.PostedBy != userId)
                return BadRequest("No such property or photo exists");

            var photo = property.Photos.FirstOrDefault(p => p.PublicId == photoPublicId);

            if (photo == null)
                return BadRequest("No such property or photo exists");

            if (photo.IsPrimary)
                return BadRequest("This is already a primary photo");


            var currentPrimary = property.Photos.FirstOrDefault(p => p.IsPrimary);
            if (currentPrimary != null) currentPrimary.IsPrimary = false;
            photo.IsPrimary = true;

            if (await uow.SaveAsync()) return NoContent();

            return BadRequest("Failed to set primary photo");
        }

        [HttpDelete("delete-photo/{propId}/{photoPublicId}")]
        [Authorize]
        public async Task<IActionResult> DeletePhoto(int propId, string photoPublicId)
        {
            var userId = GetUserId();

            var property = await uow.PropertyRepository.GetPropertyByIdAsync(propId);

            if (property.PostedBy != userId)
                return BadRequest("You are not authorised to delete the photo");

            if (property == null || property.PostedBy != userId)
                return BadRequest("No such property or photo exists");

            var photo = property.Photos.FirstOrDefault(p => p.PublicId == photoPublicId);

            if (photo == null)
                return BadRequest("No such property or photo exists");

            if (photo.IsPrimary)
                return BadRequest("You can not delete primary photo");

            if (photo.PublicId != null)
            {
                var result = await photoService.DeletePhotoAsync(photo.PublicId);
                if (result.Error != null) return BadRequest(result.Error.Message);
            }

            property.Photos.Remove(photo);

            if (await uow.SaveAsync()) return Ok();

            return BadRequest("Failed to delete photo");
        }

        // 
        [HttpGet("comments/{propertyId}")]
        public async Task<IActionResult> GetComments(int propertyId)
        {
            var comments = await _context.Comments
                .Where(c => c.PropertyId == propertyId)
                .Include(c => c.User) 
                .OrderBy(c => c.CreatedAt)
                .ToListAsync();

            var commentDtos = comments.Select(c => new CommentDto
            {
                Text = c.Text,
                UserName = c.User.Username,
                CreatedAt = c.CreatedAt
            }).ToList();

            return Ok(commentDtos);
        }

        // Add a comment to a property
        [HttpPost("add-comment/{propertyId}")]
        [Authorize]
        public async Task<IActionResult> AddComment(int propertyId, [FromBody] CommentDto commentDto)
        {
            if (string.IsNullOrEmpty(commentDto.Text))
            {
                Console.WriteLine("Comment text is empty.");
                return BadRequest("Comment cannot be empty.");
            }

            var user = await _context.Users.FirstOrDefaultAsync(u => u.Username == commentDto.UserName);
            if (user == null)
            {
                Console.WriteLine($"User not found for username: {commentDto.UserName}");
                return NotFound("User not found.");
            }

            var property = await uow.PropertyRepository.GetPropertyByIdAsync(propertyId);
            if (property == null)
            {
                Console.WriteLine($"Property not found for ID: {propertyId}");
                return NotFound("Property not found.");
            }

            var comment = new Comment
            {
                Text = commentDto.Text,
                PropertyId = propertyId,
                UserId = user.Id,
                CreatedAt = DateTime.UtcNow
            };

            _context.Comments.Add(comment);

            try
            {
                if (await _context.SaveChangesAsync() > 0)
                {
                    Console.WriteLine("Comment added successfully.");
                    return Ok(new CommentDto
                    {
                        Text = comment.Text,
                        UserName = user.Username,
                        CreatedAt = comment.CreatedAt
                    });
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error saving comment: {ex.Message}");
            }

            Console.WriteLine("Failed to add comment.");
            return BadRequest("Failed to add comment.");
        }






    }
}