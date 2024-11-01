using System.Collections.Generic;
using System.Threading.Tasks;
using WebAPI.Models;

namespace WebAPI.Interfaces
{
    public interface IFavoriteRepository
    {
        Task AddFavorite(Favorite favorite);
        Task RemoveFavorite(Favorite favorite);
        Task<Favorite> GetFavorite(int userId, int propertyId);
        Task<IEnumerable<Favorite>> GetUserFavorites(int userId);
    }
}
