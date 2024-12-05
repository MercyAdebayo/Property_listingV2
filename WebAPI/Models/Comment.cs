using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebAPI.Models
{
   public class Comment
    {
        public int Id { get; set; }
        public string Text { get; set; }
        public int PropertyId { get; set; }
        public Property Property { get; set; }
        public int UserId { get; set; }
        public User User { get; set; }
        public DateTime CreatedAt { get; set; }
    }

}