package com.example.walkwatch.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.walkwatch.databinding.FragmentCrimeDataBinding
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer

class CrimeDataFragment : Fragment() {

    private var _binding: FragmentCrimeDataBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentCrimeDataBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize MapView
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { googleMap ->
            // Add map customization and markers here
            Toast.makeText(context, "Map is ready!", Toast.LENGTH_SHORT).show()
        }

        // Set up UI elements
        val textView: TextView = binding.appHeader
        val panicButton = binding.panicButton

        // Observe ViewModel data
        dashboardViewModel.text.observe(viewLifecycleOwner) { text ->
            textView.text = text
        }

        // Set Panic Button Click Listener
        panicButton.setOnClickListener {
            Toast.makeText(context, "Panic Button Pressed!", Toast.LENGTH_SHORT).show()
            // Add further logic (e.g., triggering an alert or sharing location)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
