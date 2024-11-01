package com.example.homefinder.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homefinder.model.PropertyListDto
import com.example.homefinder.R
import com.example.homefinder.utils.RetrofitInstance
import com.example.homefinder.adapters.HomeAdapter
import com.example.homefinder.databinding.ActivityMainBinding
import com.example.homefinder.model.CityDto
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeAdapter: HomeAdapter
    private var homeList: List<PropertyListDto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up BottomNavigationView
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.selectedItemId = R.id.navigation_home

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_notifications -> {
                    startActivity(Intent(this, NotificationsActivity::class.java))
                    true
                }
                R.id.navigation_favorite -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.navigation_account -> {
                    startActivity(Intent(this, AccountActivity::class.java))
                    true
                }
                R.id.navigation_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Fetch properties from API
        fetchProperties()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu) // Inflate filter icon
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                showFilterBottomSheet() // Show filter options on filter button click
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchProperties(sellRent: Int? = null, cityId: Int? = null, sortBy: String? = null) {
        RetrofitInstance.propertyApiService.getPropertyList(sellRent, cityId, sortBy).enqueue(object : Callback<List<PropertyListDto>> {
            override fun onResponse(call: Call<List<PropertyListDto>>, response: Response<List<PropertyListDto>>) {
                if (response.isSuccessful) {
                    homeList = response.body() ?: emptyList()
                    homeAdapter = HomeAdapter(homeList){ selectedProperty ->
                        val intent = Intent(this@MainActivity, PropertyDetailActivity::class.java)
                        intent.putExtra("PROPERTY_ID", selectedProperty.id)
                        startActivity(intent)
                    }
                    binding.recyclerView.adapter = homeAdapter
                } else {
                    Log.e("MainActivity", "Error loading properties: ${response.code()} - ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity, "Failed to load properties. Code: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PropertyListDto>>, t: Throwable) {
                Log.e("MainActivity", "Failed to fetch properties: ${t.message}")
            }
        })
    }

    private fun showFilterBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)
        bottomSheetDialog.setContentView(view)

        val radioGroupSellRent = view.findViewById<RadioGroup>(R.id.radioGroupSellRent)
        val spinnerCity = view.findViewById<Spinner>(R.id.spinnerCity)
        val radioGroupSort = view.findViewById<RadioGroup>(R.id.radioGroupSort)
        val buttonApply = view.findViewById<Button>(R.id.buttonApply)
        val buttonReset = view.findViewById<Button>(R.id.buttonReset)

        // Fetch cities dynamically
        fetchCities { fetchedCities ->
            // Populate spinner with fetched cities
            val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fetchedCities.map { it.name })
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCity.adapter = cityAdapter

            // Set up button click listener after cities are loaded
            buttonApply.setOnClickListener {
                val sellRent = when (radioGroupSellRent.checkedRadioButtonId) {
                    R.id.radioSell -> 1
                    R.id.radioRent -> 2
                    else -> null
                }
                val selectedCityName = spinnerCity.selectedItem.toString()
                val selectedCityId = fetchedCities.firstOrNull { it.name == selectedCityName }?.id
                val sortBy = when (radioGroupSort.checkedRadioButtonId) {
                    R.id.radioSortDate -> "date"
                    R.id.radioSortAlphabetical -> "alphabet"
                    else -> null
                }

                applyFilters(sellRent, selectedCityId, sortBy)
                bottomSheetDialog.dismiss()
            }
        }
        // Set up button click listener for Reset button
        buttonReset.setOnClickListener {
            // Reset Sell/Rent radio group to "All"
            radioGroupSellRent.clearCheck()
            radioGroupSellRent.check(R.id.radioAll)

            // Reset city spinner to the first item
            spinnerCity.setSelection(0)

            // Reset Sort By radio group to no selection
            radioGroupSort.clearCheck()

            // clear any filters already applied in your data fetch function
            applyFilters(null, null, null)
        }

        bottomSheetDialog.show()
    }

    private fun applyFilters(sellRent: Int?, cityId: Int?, sortBy: String?) {
        fetchProperties(sellRent, cityId, sortBy)
    }


    // Function to fetch cities and update the spinner
    private fun fetchCities(onCitiesFetched: (List<CityDto>) -> Unit) {
        RetrofitInstance.propertyApiService.getCities().enqueue(object : Callback<List<CityDto>> {
            override fun onResponse(call: Call<List<CityDto>>, response: Response<List<CityDto>>) {
                if (response.isSuccessful) {
                    val cities = response.body() ?: emptyList()
                    onCitiesFetched(cities)
                } else {
                    Log.e("MainActivity", "Error loading cities: ${response.code()}")
                    Toast.makeText(this@MainActivity, "Failed to load cities", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CityDto>>, t: Throwable) {
                Log.e("MainActivity", "Failed to load cities: ${t.message}")
                Toast.makeText(this@MainActivity, "Failed to load cities", Toast.LENGTH_SHORT).show()
            }
        })
    }



}
