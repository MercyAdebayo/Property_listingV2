using System.Collections.Generic;
using System.Threading.Tasks;
using WebAPI.Models;

namespace WebAPI.Interfaces
{
    public interface IPropertyRepository
    {
        Task<IEnumerable<Property>> GetPropertiesAsync(int? sellRent, int? cityId = null, string sortBy = null);
        Task<Property> GetPropertyDetailAsync(int id);
        Task<Property> GetPropertyByIdAsync(int id);
        void AddProperty(Property property);
        void DeleteProperty(int id);
    }
}