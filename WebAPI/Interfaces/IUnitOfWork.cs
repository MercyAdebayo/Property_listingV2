using System.Threading.Tasks;

namespace WebAPI.Interfaces
{
    public interface IUnitOfWork
    {
         ICityRepository CityRepository {get; }

         IUserRepository UserRepository {get; }

         IPropertyRepository PropertyRepository {get; }

         IFurnishingTypeRepository FurnishingTypeRepository {get; }

         IPropertyTypeRepository PropertyTypeRepository {get; }

         IFavoriteRepository FavoriteRepository { get; }

         Task<bool> SaveAsync();

         Task<bool> Complete();
        bool HasChanges();
    }
}