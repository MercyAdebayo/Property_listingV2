package com.example.homefinder

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = application

    // LiveData for the list of favorite properties
    private val _favoriteProperties = MutableLiveData<List<FavoriteDto>>()
    val favoriteProperties: LiveData<List<FavoriteDto>> get() = _favoriteProperties

    // LiveData to notify the UI about the result of favorite actions
    private val _favoriteActionResult = MutableLiveData<String>()
    val favoriteActionResult: LiveData<String> get() = _favoriteActionResult

    // Function to load favorite properties
    fun loadFavorites() {
        val token = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getString("TOKEN", null)

        if (token != null) {
            RetrofitInstance.authService.getUserFavorites("Bearer $token").enqueue(object : Callback<List<FavoriteDto>> {
                override fun onResponse(call: Call<List<FavoriteDto>>, response: Response<List<FavoriteDto>>) {
                    if (response.isSuccessful) {
                        _favoriteProperties.value = response.body() ?: emptyList()
                    } else {
                        _favoriteActionResult.value = "Failed to load favorites."
                    }
                }

                override fun onFailure(call: Call<List<FavoriteDto>>, t: Throwable) {
                    _favoriteActionResult.value = "Error: ${t.message}"
                }
            })
        } else {
            _favoriteActionResult.value = "User not authenticated. Please log in."
        }
    }

    // Function to add a property to the favorites
    fun addFavorite(propertyId: Int) {
        val token = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getString("TOKEN", null)

        if (token != null) {
            RetrofitInstance.authService.addFavorite(propertyId, "Bearer $token").enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        _favoriteActionResult.value = "Property added to favorites."
                        loadFavorites() // Refresh favorites
                    } else {
                        _favoriteActionResult.value = "Failed to add property to favorites."
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    _favoriteActionResult.value = "Error: ${t.message}"
                }
            })
        } else {
            _favoriteActionResult.value = "User not authenticated. Please log in."
        }
    }

    // Function to remove a property from the favorites
    fun removeFavorite(propertyId: Int) {
        val token = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getString("TOKEN", null)

        if (token != null) {
            RetrofitInstance.authService.removeFavorite(propertyId, "Bearer $token").enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        _favoriteActionResult.value = "Property removed from favorites."
                        loadFavorites() // Refresh favorites
                    } else {
                        _favoriteActionResult.value = "Failed to remove property from favorites."
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    _favoriteActionResult.value = "Error: ${t.message}"
                }
            })
        } else {
            _favoriteActionResult.value = "User not authenticated. Please log in."
        }
    }
}
