using System;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using WebAPI.Dtos;
using WebAPI.Errors;
using WebAPI.Extensions;
using WebAPI.Interfaces;
using WebAPI.Models;

namespace WebAPI.Controllers
{
    public class AccountController : BaseController
    {
        private readonly IUnitOfWork uow;
        private readonly IConfiguration configuration;
        public AccountController(IUnitOfWork uow, IConfiguration configuration)
        {
            this.configuration = configuration;
            this.uow = uow;
        }

        // api/account/login
        [HttpPost("login")]
        public async Task<IActionResult> Login(LoginReqDto loginReq)
        {
            var user = await uow.UserRepository.Authenticate(loginReq.UserName, loginReq.Password);

            ApiError apiError = new ApiError();

            if (user == null)
            {
                apiError.ErrorCode=Unauthorized().StatusCode;
                apiError.ErrorMessage="Invalid user name or password";
                apiError.ErrorDetails="This error appear when provided user id or password does not exists";
                return Unauthorized(apiError);
            }

            var loginRes = new LoginResDto();
            loginRes.UserName = user.Username;
            loginRes.Token = CreateJWT(user);
            return Ok(loginRes);
        }


        // api/account/register
   
        [HttpPost("register")]
        public async Task<IActionResult> Register(RegisterDto registerDto)
        {
            ApiError apiError = new ApiError();

            try
            {
                // Ensure all fields are present
                if (string.IsNullOrWhiteSpace(registerDto.UserName) || 
                    string.IsNullOrWhiteSpace(registerDto.Password) ||
                    string.IsNullOrWhiteSpace(registerDto.Email) ||
                    string.IsNullOrWhiteSpace(registerDto.FullName) ||
                    string.IsNullOrWhiteSpace(registerDto.Mobile))
                {
                    apiError.ErrorCode = BadRequest().StatusCode;
                    apiError.ErrorMessage = "All fields must be filled";
                    return BadRequest(apiError);
                }

                // Check if the user already exists
                if (await uow.UserRepository.UserAlreadyExists(registerDto.UserName))
                {
                    apiError.ErrorCode = BadRequest().StatusCode;
                    apiError.ErrorMessage = "User already exists, please try a different username or email";
                    return BadRequest(apiError);
                }

                // Register the user
                await uow.UserRepository.Register(registerDto.UserName, registerDto.Password, registerDto.FullName, registerDto.Email, registerDto.Mobile);

                // Return a success response
                return StatusCode(201, "Registration successful.");
            }
            catch (Exception ex)
            {
                // Log the exception (use a proper logging system in production)
                Console.WriteLine(ex.Message);
                apiError.ErrorCode = StatusCodes.Status500InternalServerError;
                apiError.ErrorMessage = "An internal server error occurred during registration.";
                return StatusCode(StatusCodes.Status500InternalServerError, apiError);
            }
        }

        [Authorize]
        [HttpGet("userinfo")]
        public async Task<IActionResult> GetUserInfo()
        {
            var userId = User.GetUserId(); 
            var user = await uow.UserRepository.GetUserByIdAsync(userId);

            if (user == null) return NotFound();

            var userInfo = new UserInfoDto
            {
                UserName = user.Username,
                FullName = user.FullName,
                Email = user.Email,
                Mobile = user.Mobile
            };

            return Ok(userInfo);
        }

        [HttpPost("add-favorite/{propertyId}")]
        public async Task<IActionResult> AddFavorite(int propertyId)
        {
            var userId = User.GetUserId();
            var existingFavorite = await uow.FavoriteRepository.GetFavorite(userId, propertyId);

            if (existingFavorite != null)
            {
                return BadRequest("Property is already in favorites.");
            }

            var favorite = new Favorite
            {
                UserId = userId,
                PropertyId = propertyId
            };

            await uow.FavoriteRepository.AddFavorite(favorite);
            if (await uow.Complete())
            {
                return Ok("Property added to favorites.");
            }

            return BadRequest("Failed to add property to favorites.");
        }

        [HttpDelete("remove-favorite/{propertyId}")]
        public async Task<IActionResult> RemoveFavorite(int propertyId)
        {
            var userId = User.GetUserId();
            var favorite = await uow.FavoriteRepository.GetFavorite(userId, propertyId);

            if (favorite == null)
            {
                return NotFound("Property is not in favorites.");
            }

            await uow.FavoriteRepository.RemoveFavorite(favorite);
            if (await uow.Complete())
            {
                return Ok("Property removed from favorites.");
            }

            return BadRequest("Failed to remove property from favorites.");
        }

        [HttpGet("is-favorite/{propertyId}")]
        public async Task<IActionResult> IsFavorite(int propertyId)
        {
            var userId = User.GetUserId();
            var favorite = await uow.FavoriteRepository.GetFavorite(userId, propertyId);

            return Ok(favorite != null);
        }

        [HttpGet("favorites")]
        public async Task<IActionResult> GetUserFavorites()
        {
            var userId = User.GetUserId();
            var favorites = await uow.FavoriteRepository.GetUserFavorites(userId);

            return Ok(favorites);
        }

       




        private string CreateJWT(User user)
        {
            var secretKey = configuration.GetSection("AppSettings:Key").Value;
            if (string.IsNullOrEmpty(secretKey))
            {
                throw new InvalidOperationException("Secret key must not be null or empty.");
            }
            var key = new SymmetricSecurityKey(Encoding.UTF8
                .GetBytes(secretKey));

            var claims = new Claim[] {
                new Claim(ClaimTypes.Name,user.Username),
                new Claim(ClaimTypes.NameIdentifier,user.Id.ToString())
            };

            var signingCredentials = new SigningCredentials(
                    key, SecurityAlgorithms.HmacSha256Signature);

            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(claims),
                Expires = DateTime.UtcNow.AddHours(1),
                SigningCredentials = signingCredentials
            };

            var tokenHandler = new JwtSecurityTokenHandler();
            var token = tokenHandler.CreateToken(tokenDescriptor);
            return tokenHandler.WriteToken(token);
        }

    }
}