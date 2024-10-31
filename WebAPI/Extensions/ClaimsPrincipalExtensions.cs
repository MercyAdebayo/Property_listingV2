using System;
using System.Security.Claims;

namespace WebAPI.Extensions
{
      public static class ClaimsPrincipalExtensions
    {
        public static int GetUserId(this ClaimsPrincipal user)
        {
            if (user == null) throw new ArgumentNullException(nameof(user));

            var userIdClaim = user.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            if (userIdClaim == null) throw new InvalidOperationException("User ID claim is missing.");

            return int.Parse(userIdClaim);
        }
    }
}