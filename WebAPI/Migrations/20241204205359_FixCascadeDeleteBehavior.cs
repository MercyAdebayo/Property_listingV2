using Microsoft.EntityFrameworkCore.Migrations;

namespace WebAPI.Migrations
{
    public partial class FixCascadeDeleteBehavior : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Comments_Properties_PropertyId",
                table: "Comments");

            migrationBuilder.AddForeignKey(
                name: "FK_Comments_Properties_PropertyId",
                table: "Comments",
                column: "PropertyId",
                principalTable: "Properties",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Comments_Properties_PropertyId",
                table: "Comments");

            migrationBuilder.AddForeignKey(
                name: "FK_Comments_Properties_PropertyId",
                table: "Comments",
                column: "PropertyId",
                principalTable: "Properties",
                principalColumn: "Id");
        }
    }
}
