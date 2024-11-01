package com.example.homefinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class FavoritesAdapter(
    private val onPropertyClick: (Int) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    private var favoriteProperties = mutableListOf<PropertyListDto>()

    fun submitList(properties: List<PropertyListDto>) {
        favoriteProperties = properties.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val property = favoriteProperties[position]
        holder.bind(property)
        holder.itemView.setOnClickListener { onPropertyClick(property.id) }
    }

    override fun getItemCount(): Int = favoriteProperties.size

    inner class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val propertyName: TextView = view.findViewById(R.id.text_property_name)
        private val propertyPrice: TextView = view.findViewById(R.id.text_property_price)
        private val propertyCity: TextView = view.findViewById(R.id.text_property_city)

        fun bind(property: PropertyListDto) {
            propertyName.text = property.name
            propertyPrice.text = "$${property.price}"
            propertyCity.text = property.city
        }
    }
}
