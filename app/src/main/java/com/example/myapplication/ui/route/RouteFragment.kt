package com.example.myapplication.ui.route

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentRouteBinding

class RouteFragment : Fragment() {

    private var _binding: FragmentRouteBinding? = null
    private lateinit var placesRecyclerView: RecyclerView
    private lateinit var placesAdapter: PlacesAdapter
    private val placesList = mutableListOf<Place>()

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView
        placesRecyclerView = binding.placesContainer
        placesRecyclerView.layoutManager = LinearLayoutManager(context)

        // Sample data for places
        placesList.add(Place("Home", "A cozy place to relax.", "10 minutes"))
        placesList.add(Place("Store", "Shop for essentials.", "5 minutes"))
        placesList.add(Place("Church", "A peaceful place for reflection.", "15 minutes"))
        placesList.add(Place("Restaurant", "Delicious meals to enjoy.", "20 minutes"))

        // Initialize PlacesAdapter and set to RecyclerView
        placesAdapter = PlacesAdapter(placesList)
        placesRecyclerView.adapter = placesAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
