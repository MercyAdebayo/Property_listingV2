using System.Threading.Tasks;
using WebAPI.Models;

namespace WebAPI.Interfaces
{
    public interface IUserRepository
    {
        Task<User> Authenticate(string userName, string password);   

        Task Register(string userName, string password, string fullName, string email, string mobile);

        Task<bool> UserAlreadyExists(string userName);
    }
}
