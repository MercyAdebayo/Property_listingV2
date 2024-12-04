using AutoMapper;
using Microsoft.AspNetCore.JsonPatch;
using Microsoft.AspNetCore.Mvc;
using Moq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Controllers;
using WebAPI.Dtos;
using WebAPI.Interfaces;
using WebAPI.Models;

namespace WebAPI.Tests.Controllers
{
    public class CityControllerTests
    {
        private readonly Mock<IUnitOfWork> _mockUow;
        private readonly Mock<IMapper> _mockMapper;
        private readonly CityController _controller;

        public CityControllerTests()
        {
            _mockUow = new Mock<IUnitOfWork>();
            _mockMapper = new Mock<IMapper>();
            _controller = new CityController(_mockUow.Object, _mockMapper.Object);
        }

        [Fact]
        public async Task GetCities_ReturnsOkResult_WithListOfCities()
        {
            // Arrange
            var cities = new List<City>
            {
                new City("City1", "Country1"),
                new City("City2", "Country2")
            };

            _mockUow.Setup(u => u.CityRepository.GetCitiesAsync()).ReturnsAsync(cities);

            _mockMapper.Setup(m => m.Map<IEnumerable<CityDto>>(cities))
                       .Returns(new List<CityDto>
                       {
                           new CityDto { Id = 1, Name = "City1" },
                           new CityDto { Id = 2, Name = "City2" }
                       });

            // Act
            var result = await _controller.GetCities();

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result);
            var returnedCities = Assert.IsType<List<CityDto>>(okResult.Value);
            Assert.Equal(2, returnedCities.Count);
        }


        [Fact]
        public async Task UpdateCity_IdMismatch_ReturnsBadRequest()
        {
            // Arrange
            var cityDto = new CityDto { Id = 2, Name = "Updated City" };

            // Act
            var result = await _controller.UpdateCity(1, cityDto);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result);
            Assert.Equal("Update not allowed", badRequestResult.Value);
        }

        [Fact]
        public async Task DeleteCity_ValidId_ReturnsOk()
        {
            // Arrange
            _mockUow.Setup(u => u.CityRepository.DeleteCity(1));
            _mockUow.Setup(u => u.SaveAsync()).ReturnsAsync(true);

            // Act
            var result = await _controller.DeleteCity(1);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result);
            Assert.Equal(1, okResult.Value);
            _mockUow.Verify(u => u.CityRepository.DeleteCity(1), Times.Once);
            _mockUow.Verify(u => u.SaveAsync(), Times.Once);
        }

    }
}
