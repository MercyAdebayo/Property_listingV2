using System.ComponentModel.DataAnnotations;

namespace WebAPI.Models
{
    public class User : BaseEntity
    {
        public string Username { get; set; }
        public byte[] Password { get; set; } 
        public byte[] PasswordKey { get; set; }
        public string FullName { get; set; }
        public string Email { get; set; }
        public string Mobile { get; set; } 
    }
   
}