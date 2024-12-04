package com.example.walkwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.walkwatch.databinding.FragmentProfileBinding // Adjust this to match your actual binding class name

class ProfileSecFragment : Fragment() {

    // Declare the binding variable as nullable and private
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!! // Non-nullable getter for safe access

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using ViewBinding and assign to _binding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Use the binding property safely to access views
        binding.someTextView.text = "Welcome to the Profile Section"
        binding.someButton.setOnClickListener {
            // Handle button click
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding when the view is destroyed to avoid memory leaks
        _binding = null
    }
}
