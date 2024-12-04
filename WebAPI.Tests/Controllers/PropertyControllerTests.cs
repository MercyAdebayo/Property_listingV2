using AutoMapper;
using CloudinaryDotNet.Actions;
using Microsoft.AspNetCore.Http;
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
    public class PropertyControllerTests
    {
        private readonly Mock<IUnitOfWork> _mockUow;
        private readonly Mock<IMapper> _mockMapper;
        private readonly Mock<IPhotoService> _mockPhotoService;
        private readonly PropertyController _controller;

        public PropertyControllerTests()
        {
            _mockUow = new Mock<IUnitOfWork>();
            _mockMapper = new Mock<IMapper>();
            _mockPhotoService = new Mock<IPhotoService>();

            _controller = new PropertyController(_mockUow.Object, _mockMapper.Object, _mockPhotoService.Object);
        }

        [Fact]
        public async Task GetPropertyList_ReturnsOkResult_WithPropertyList()
        {
            // Arrange
            var properties = new List<Property> { new Property { Id = 1, Name = "Test Property" } };
            _mockUow.Setup(u => u.PropertyRepository.GetPropertiesAsync(It.IsAny<int?>(), It.IsAny<int?>(), It.IsAny<string>()))
        .ReturnsAsync(properties.AsEnumerable());
            _mockMapper.Setup(m => m.Map<IEnumerable<PropertyListDto>>(It.IsAny<IEnumerable<Property>>()))
                       .Returns(new List<PropertyListDto> { new PropertyListDto { Id = 1, Name = "Test Property" } });

            // Act
            var result = await _controller.GetPropertyList();

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result);
            var returnedProperties = Assert.IsType<List<PropertyListDto>>(okResult.Value);
            Assert.Single(returnedProperties);
        }

        [Fact]
        public async Task GetPropertyDetail_ReturnsOkResult_WithPropertyDetail()
        {
            // Arrange
            var property = new Property { Id = 1, Name = "Test Property" };
            _mockUow.Setup(u => u.PropertyRepository.GetPropertyDetailAsync(It.IsAny<int>()))
                    .ReturnsAsync(property);
            _mockMapper.Setup(m => m.Map<PropertyDetailDto>(It.IsAny<Property>()))
                       .Returns(new PropertyDetailDto { Id = 1, Name = "Test Property" });

            // Act
            var result = await _controller.GetPropertyDetail(1);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result);
            var returnedProperty = Assert.IsType<PropertyDetailDto>(okResult.Value);
            Assert.Equal(1, returnedProperty.Id);
        }



    }
}
