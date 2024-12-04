package com.example.walkwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CommunityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_community, container, false)

        // Setup RecyclerView for displaying "People Near You"
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Dummy data for people nearby
        val peopleNearby = listOf("Person 1", "Person 2", "Person 3", "Person 4", "Person 5")
        val adapter = PeopleNearbyAdapter(peopleNearby)
        recyclerView.adapter = adapter

        return rootView
    }
}
