using System;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
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


    [HttpPost("register")]
    public async Task<IActionResult> Register(LoginReqDto loginReq)
    {
        ApiError apiError = new ApiError();

        // Validate that username and password are not blank
        if(string.IsNullOrWhiteSpace(loginReq.UserName) || string.IsNullOrWhiteSpace(loginReq.Password)) {
            apiError.ErrorCode = BadRequest().StatusCode;
            apiError.ErrorMessage = "Username or password cannot be blank";                    
            return BadRequest(apiError);
        }

        // Check if the user already exists
        if (await uow.UserRepository.UserAlreadyExists(loginReq.UserName)) {
            apiError.ErrorCode = BadRequest().StatusCode;
            apiError.ErrorMessage = "User already exists, please try a different username";
            return BadRequest(apiError);
        }                

        // Register the new user
        uow.UserRepository.Register(loginReq.UserName, loginReq.Password);
        await uow.SaveAsync();

        // Authenticate the newly registered user and return a JWT token
        var user = await uow.UserRepository.Authenticate(loginReq.UserName, loginReq.Password);
        if (user == null) {
            apiError.ErrorCode = Unauthorized().StatusCode;
            apiError.ErrorMessage = "There was an error during registration. Please try again.";
            return Unauthorized(apiError);
        }

        // Create JWT token for the newly registered user
        var loginRes = new LoginResDto();
        loginRes.UserName = user.Username;
        loginRes.Token = CreateJWT(user);

        
        return Ok(loginRes);
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