using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;


namespace WebAPI.Models
{
 public class Favorite
    {
        [Key]
        public int Id { get; set; }

        [Required]
        public int UserId { get; set; }

        [Required]
        public int PropertyId { get; set; }

        public User User { get; set; }

        public Property Property { get; set; }
    }
}