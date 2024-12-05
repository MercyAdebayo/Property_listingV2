package com.example.homefinder.ui

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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.homefinder.model.PropertyDto
import com.example.homefinder.R
import com.example.homefinder.model.CommentDto
import com.example.homefinder.utils.RetrofitInstance
import com.example.homefinder.viewmodels.FavoritesViewModel
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
    private lateinit var viewModel: FavoritesViewModel

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

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(FavoritesViewModel::class.java)

        // Fetch property ID from intent
        val propertyId = intent.getIntExtra("PROPERTY_ID", -1)
        if (propertyId == -1) {
            Toast.makeText(this, "Invalid property", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.isPropertyInFavorites(propertyId) { isInFavorites ->

            val icon = if (isInFavorites) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            fabFavorite.setImageResource(icon)
        }


        // Observe the favorite action result and update UI accordingly
        viewModel.favoriteActionResult.observe(this) { result ->
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }

        // Favorite button toggle

        fabFavorite.setOnClickListener {

            viewModel.isPropertyInFavorites(propertyId) { isInFavorites ->
                if (isInFavorites) {
                    viewModel.removeFavorite(propertyId)
                    println("Property is in favorites.")
                } else {
                    viewModel.addFavorite(propertyId)
                    println("Property is not in favorites.")
                }
                updateFavoriteIcon(isInFavorites)
            }
        }

        // Fetch comments when the activity starts
        RetrofitInstance.propertyApiService.getComments(propertyId).enqueue(object : Callback<List<CommentDto>> {
            override fun onResponse(call: Call<List<CommentDto>>, response: Response<List<CommentDto>>) {
                if (response.isSuccessful) {
                    val comments = response.body()
                    if (comments != null) {
                        // Display comments in UI
                        commentsContainer.removeAllViews() // Clear any existing comments
                        for (comment in comments) {
                            // Create a comment text that includes the username
                            val commentWithUserName = "${comment.text} - ${comment.userName} "

                            // Dynamically add a TextView for each new comment
                            val commentView = TextView(this@PropertyDetailActivity).apply {
                                text = commentWithUserName
                                textSize = 16f
                                setPadding(8, 8, 8, 8)
                            }
                            commentsContainer.addView(commentView)
                        }
                    }
                } else {
                    Toast.makeText(this@PropertyDetailActivity, "Failed to load comments", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CommentDto>>, t: Throwable) {
                Toast.makeText(this@PropertyDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Add comment when button is clicked

        buttonAddComment.setOnClickListener {
            val commentText = commentInput.text.toString().trim()
            if (commentText.isNotEmpty()) {
                val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                val userName = sharedPreferences.getString("USERNAME", null)
                val token = sharedPreferences.getString("TOKEN", null) // Fetch the token



                if (userName != null && token != null) { // Check if both username and token are available
                    // Get the current timestamp
                    val createdAt = convertTimestampToDateTime(System.currentTimeMillis())

                    // Create the CommentDto using username and other data
                    val commentDto = CommentDto(
                        text = commentText,
                        userName = userName,
                        createdAt = createdAt
                    )


                    // Add the Authorization header with the Bearer token
                    val authHeader = "Bearer $token"

                    // Make the API call with the Authorization header
                    RetrofitInstance.propertyApiService.addComment(propertyId, commentDto, authHeader).enqueue(object : Callback<CommentDto> {
                        override fun onResponse(call: Call<CommentDto>, response: Response<CommentDto>) {
                            if (response.isSuccessful) {
                                addComment(commentText)

                                // Clear the comment input field after posting the comment
                                commentInput.text.clear()
                            } else {

                                Toast.makeText(this@PropertyDetailActivity, "Failed to add comment.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<CommentDto>, t: Throwable) {
                            Toast.makeText(this@PropertyDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(this, "User not logged in or token is missing.", Toast.LENGTH_SHORT).show()
                }
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

    private fun updateFavoriteIcon(isFavorite: Boolean) {
       // var isFavorite = viewModel.isPropertyInFavorites(propertyId)
        val icon = if (!isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        fabFavorite.setImageResource(icon)
        Toast.makeText(this, if (isFavorite) "Removed from favorites" else "Added to favorites", Toast.LENGTH_SHORT).show()

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
    // Function to convert timestamp to ISO 8601 format
    fun convertTimestampToDateTime(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault())
        sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
        return sdf.format(java.util.Date(timestamp))
    }

}
