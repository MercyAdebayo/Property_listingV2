using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using WebAPI.Interfaces;
using WebAPI.Models;

namespace WebAPI.Data.Repo
{
    public class PropertyRepository : IPropertyRepository
    {
        private readonly DataContext dc;

        public PropertyRepository(DataContext dc)
        {
            this.dc = dc;
        }
        public void AddProperty(Property property)
        {
            dc.Properties.Add(property);
        }

        public void DeleteProperty(int id)
        {
            throw new System.NotImplementedException();
        }

        // public async Task<IEnumerable<Property>> GetPropertiesAsync(int? sellRent = null)
        // {
        //     var query = dc.Properties
        //         .Include(p => p.PropertyType)
        //         .Include(p => p.City)
        //         .Include(p => p.FurnishingType)
        //         .Include(p => p.Photos)
        //         .AsQueryable();


        //     if (sellRent.HasValue)
        //     {
        //         query = query.Where(p => p.SellRent == sellRent.Value);
        //     }

        //     return await query.ToListAsync();
        // }

    public async Task<IEnumerable<Property>> GetPropertiesAsync(int? sellRent = null, int? cityId = null, string sortBy = null)
    {
        var query = dc.Properties
            .Include(p => p.PropertyType)
            .Include(p => p.City)
            .Include(p => p.FurnishingType)
            .Include(p => p.Photos)
            .AsQueryable();

        if (sellRent.HasValue)
            query = query.Where(p => p.SellRent == sellRent);

        if (cityId.HasValue)
            query = query.Where(p => p.CityId == cityId);

            if (!string.IsNullOrEmpty(sortBy))
            {
                query = sortBy switch
                {
                    "date" => query.OrderByDescending(p => p.PostedOn),
                    "alphabet" => query.OrderBy(p => p.Name),
                    _ => query
                };
            }

            return await query.ToListAsync();
        }


        public async Task<Property> GetPropertyDetailAsync(int id)
        {
            var properties = await dc.Properties
            .Include(p => p.PropertyType)
            .Include(p => p.City)
            .Include(p => p.FurnishingType)
            .Include(p => p.Photos)
            .Where(p => p.Id == id)
            .FirstAsync();

            return properties;
        }

        public async Task<Property> GetPropertyByIdAsync(int id)
        {
            var properties = await dc.Properties
            .Include(p => p.Photos)
            .Where(p => p.Id == id)
            .FirstOrDefaultAsync();

            return properties;
        }


    }
}