import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homefinder.HomeAdapter
import com.example.homefinder.PropertyListDto
import com.example.homefinder.RetrofitInstance
import com.example.homefinder.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Use View Binding
    private lateinit var homeAdapter: HomeAdapter
    private var homeList: List<PropertyListDto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the RecyclerView using View Binding
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch properties from API
        fetchProperties()
    }

    private fun fetchProperties() {
        // Correct the API service call by using propertyApiService
        RetrofitInstance.propertyApiService.getPropertyList(1).enqueue(object : Callback<List<PropertyListDto>> {
            override fun onResponse(call: Call<List<PropertyListDto>>, response: Response<List<PropertyListDto>>) {
                if (response.isSuccessful) {
                    homeList = response.body() ?: emptyList()
                    homeAdapter = HomeAdapter(homeList)
                    binding.recyclerView.adapter = homeAdapter // Use binding to reference RecyclerView
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load properties", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PropertyListDto>>, t: Throwable) {
                Log.e("MainActivity", "Failed to fetch properties: ${t.message}")
            }
        })
    }
}



