package com.example.homefinder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homefinder.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        // Initialize RecyclerView with adapter
        setupRecyclerView()

        // Observe favorites from the ViewModel
        viewModel.favoriteProperties.observe(this, Observer { favorites ->
            if (favorites != null) {
                // Map FavoriteDto to PropertyDto before submitting to the adapter
                val propertyList = favorites.map { it.property }
                favoritesAdapter.submitList(propertyList)
            } else {
                Toast.makeText(this, "No favorites found", Toast.LENGTH_SHORT).show()
            }
        })

        // Load favorite properties
        viewModel.loadFavorites()
    }

    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter { propertyId ->
            // Launch PropertyDetailActivity when a favorite item is clicked
            val intent = Intent(this, PropertyDetailActivity::class.java)
            intent.putExtra("PROPERTY_ID", propertyId)
            startActivity(intent)
        }
        binding.recyclerViewFavorites.apply {
            adapter = favoritesAdapter
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
        }
    }
}
