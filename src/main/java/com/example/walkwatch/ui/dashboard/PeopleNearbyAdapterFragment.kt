package com.example.walkwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PeopleNearbyAdapter(private val people: List<String>) :
    RecyclerView.Adapter<PeopleNearbyAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val callButton: Button = view.findViewById(R.id.callButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_people_nearby_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val personName = people[position]
        holder.nameTextView.text = personName
        holder.callButton.setOnClickListener {
            // Add phone call logic here
        }
    }

    override fun getItemCount() = people.size
}
