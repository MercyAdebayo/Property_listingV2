using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using WebAPI.Interfaces;
using WebAPI.Models;

namespace WebAPI.Data.Repo
{
    public class FavoriteRepository : IFavoriteRepository
    {
        private readonly DataContext _context;

        public FavoriteRepository(DataContext context)
        {
            _context = context;
        }

        public async Task AddFavorite(Favorite favorite)
        {
            await _context.Favorites.AddAsync(favorite);
        }

        public async Task RemoveFavorite(Favorite favorite)
        {
            _context.Favorites.Remove(favorite);
        }

        public async Task<Favorite> GetFavorite(int userId, int propertyId)
        {
            return await _context.Favorites
                .FirstOrDefaultAsync(f => f.UserId == userId && f.PropertyId == propertyId);
        }

        public async Task<IEnumerable<Favorite>> GetUserFavorites(int userId)
        {
            return await _context.Favorites
                .Where(f => f.UserId == userId)
                .Include(f => f.Property)
                .ToListAsync();
        }
    }
}
