package com.example.homefinder
//
//import android.os.Bundle
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//
//class PropertyDetailActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_property_detail)
//
//        // Get property data from Intent
//        val propertyName = intent.getStringExtra("PROPERTY_NAME")
//        val propertyPrice = intent.getIntExtra("PROPERTY_PRICE", 0)
//        val propertyDescription = intent.getStringExtra("PROPERTY_DESCRIPTION")
//
//        // Display data in views (make sure you have these TextViews in your layout)
//        findViewById<TextView>(R.id.textViewPropertyName).text = propertyName
//        findViewById<TextView>(R.id.textViewPropertyPrice).text = "$$propertyPrice"
//        findViewById<TextView>(R.id.textViewPropertyDescription).text = propertyDescription
//    }
//}
