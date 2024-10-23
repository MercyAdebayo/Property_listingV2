package com.example.homefinder


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homefinder.databinding.ActivityNotificationsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var notificationsAdapter: NotificationsAdapter
    private var notificationsList: List<NotificationDto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize View Binding
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the RecyclerView
        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(this)

        // Fetch notifications
        fetchNotifications()
    }

    // Function to fetch notifications from API
    private fun fetchNotifications() {
        RetrofitInstance.notificationApiService.getNotifications().enqueue(object : Callback<List<NotificationDto>> {
            override fun onResponse(call: Call<List<NotificationDto>>, response: Response<List<NotificationDto>>) {
                if (response.isSuccessful) {
                    notificationsList = response.body() ?: emptyList()
                    notificationsAdapter = NotificationsAdapter(notificationsList)
                    binding.recyclerViewNotifications.adapter = notificationsAdapter
                } else {
                    Toast.makeText(this@NotificationsActivity, "Failed to load notifications", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<NotificationDto>>, t: Throwable) {
                Toast.makeText(this@NotificationsActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}



