package com.example.myapplication.ui.route

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemPlaceBinding

class PlacesAdapter(private val places: List<Place>) :
    RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        // Inflate the item layout using view binding
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        // Bind data to ViewHolder
        val place = places[position]
        holder.bind(place)
    }

    override fun getItemCount(): Int = places.size

    // ViewHolder class to bind data
    inner class PlaceViewHolder(private val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(place: Place) {
            // Bind place data to the UI components
            binding.placeName.text = place.name
            binding.placeDescription.text = place.description
            binding.placeTime.text = place.walkingTime
        }
    }
}
