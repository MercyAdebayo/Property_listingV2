using System;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
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
                Expires = DateTime.UtcNow.AddMinutes(1),
                SigningCredentials = signingCredentials
            };

            var tokenHandler = new JwtSecurityTokenHandler();
            var token = tokenHandler.CreateToken(tokenDescriptor);
            return tokenHandler.WriteToken(token);
        }

    }
}