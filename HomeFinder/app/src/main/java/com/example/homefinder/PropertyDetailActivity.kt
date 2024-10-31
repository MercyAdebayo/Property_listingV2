package com.example.homefinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PropertyDetailActivity : AppCompatActivity() {

    private lateinit var nameText: TextView
    private lateinit var priceText: TextView
    private lateinit var cityText: TextView
    private lateinit var countryText: TextView
    private lateinit var readyToMoveText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var ageText: TextView
    private lateinit var sellRentText: TextView
    private lateinit var bhkText: TextView
    private lateinit var photoImageView: ImageView
    private lateinit var fabFavorite: FloatingActionButton
    private lateinit var commentsContainer: LinearLayout
    private lateinit var commentInput: EditText
    private lateinit var buttonAddComment: Button
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_detail)


        // Check if the user is logged in, if not redirect to login
        if (!isUserLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Initialize views
        nameText = findViewById(R.id.text_name)
        priceText = findViewById(R.id.text_price)
        cityText = findViewById(R.id.text_city)
        countryText = findViewById(R.id.text_country)
        readyToMoveText = findViewById(R.id.text_ready_to_move)
        descriptionText = findViewById(R.id.text_description)
        ageText = findViewById(R.id.text_age)
        sellRentText = findViewById(R.id.text_sell_rent)
        bhkText = findViewById(R.id.text_bhk)
        photoImageView = findViewById(R.id.image_photo)
        fabFavorite = findViewById(R.id.fab_favorite)
        commentsContainer = findViewById(R.id.commentsContainer)
        commentInput = findViewById(R.id.commentInput)
        buttonAddComment = findViewById(R.id.buttonAddComment)

        // Fetch property ID from intent
        val propertyId = intent.getIntExtra("PROPERTY_ID", -1)
        if (propertyId == -1) {
            Toast.makeText(this, "Invalid property", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        // Favorite button toggle
        fabFavorite.setOnClickListener {
            toggleFavorite()
        }

        // Add comment functionality
        buttonAddComment.setOnClickListener {
            val commentText = commentInput.text.toString().trim()
            if (commentText.isNotEmpty()) {
                addComment(commentText)
                commentInput.text.clear()  // Clear the input field after adding comment
            } else {
                Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch property details
        fetchPropertyDetails(propertyId)
    }

    private fun fetchPropertyDetails(propertyId: Int) {
        RetrofitInstance.propertyApiService.getPropertyDetail(propertyId).enqueue(object : Callback<PropertyDto> {
            override fun onResponse(call: Call<PropertyDto>, response: Response<PropertyDto>) {
                if (response.isSuccessful) {
                    val property = response.body()
                    Log.d("PropertyDetailActivity", "Property response: $property")
                    if (property != null) {
                        displayPropertyDetails(property)
                    }
                } else {
                    Toast.makeText(this@PropertyDetailActivity, "Failed to load property details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PropertyDto>, t: Throwable) {
                Toast.makeText(this@PropertyDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun toggleFavorite() {
        isFavorite = !isFavorite
        val icon = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        fabFavorite.setImageResource(icon)
        Toast.makeText(this, if (isFavorite) "Added to favorites" else "Removed from favorites", Toast.LENGTH_SHORT).show()

    }

    private fun addComment(commentText: String) {
        // Dynamically add a TextView for each new comment
        val commentView = TextView(this).apply {
            text = commentText
            textSize = 16f
            setPadding(8, 8, 8, 8)
        }
        commentsContainer.addView(commentView)
        Toast.makeText(this, "Comment added", Toast.LENGTH_SHORT).show()

    }

    private fun displayPropertyDetails(property: PropertyDto) {
        nameText.text = property.name
        priceText.text = "$${property.price}"
        cityText.text = property.cityName
        countryText.text = property.country
        readyToMoveText.text = if (property.readyToMove) "Yes" else "No"
        descriptionText.text = property.description
        ageText.text = "${property.age} years"
        sellRentText.text = if (property.sellRent == 1) "Sell" else "Rent"
        bhkText.text = "${property.bhk} Bedrooms"

        // Load primary photo using Glide
        val primaryPhotoUrl = property.photos.firstOrNull { it.isPrimary }?.imageUrl
        // Check if primaryPhotoUrl is available
        if (primaryPhotoUrl != null) {
            Glide.with(this)
                .load(primaryPhotoUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(photoImageView)
        } else {
            // If no primary photo, load placeholder directly
            Glide.with(this)
                .load(R.drawable.placeholder)
                .into(photoImageView)
        }
        Log.d("PropertyDetail", "Primary photo URL: $primaryPhotoUrl")


    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)
        return !token.isNullOrEmpty()
    }

}
