package com.example.homefinder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class HomeAdapter(private val homeList: List<PropertyListDto>,
                  private val onPropertyClick: (PropertyListDto) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_home)
        val priceText: TextView = itemView.findViewById(R.id.text_price)
        val cashbackText: TextView = itemView.findViewById(R.id.text_cashback)
        val addressText: TextView = itemView.findViewById(R.id.text_address)
        val descriptionText: TextView = itemView.findViewById(R.id.text_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home, parent, false)
        return HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = homeList[position]
        Log.d("HomeAdapter", "Binding item at position $position: ${currentItem.name}")

        holder.priceText.text = "$${currentItem.price}"
        holder.cashbackText.text = "Up to ${currentItem.price * 0.01} Cashback"
        holder.addressText.text = "${currentItem.city}, ${currentItem.country}"
        holder.descriptionText.text = "${currentItem.bhk} BHK, ${currentItem.builtArea} sqft"

        // Load Image using Glide with placeholder and error image handling
        Glide.with(holder.itemView.context)
            .load(currentItem.photo)
            .placeholder(R.drawable.placeholder)
            .into(holder.imageView)

        // Set the click listener
        holder.itemView.setOnClickListener {
            onPropertyClick(currentItem)
        }
    }

    override fun getItemCount(): Int {
        Log.d("HomeAdapter", "Displaying ${homeList.size} items")
        return homeList.size
    }

}
