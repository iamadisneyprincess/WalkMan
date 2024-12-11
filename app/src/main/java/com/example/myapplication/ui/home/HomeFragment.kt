package com.example.myapplication.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.util.GeoPoint

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val smsPermissionRequestCode = 1
    private val locationPermissionRequestCode = 2

    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: GeoPoint? = null
    private var marker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the map
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osm_prefs", 0))
        mapView = binding.mapView
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Request permissions if not already granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode)
        }

        // Setup panic button to send SMS
        binding.panicButton.setOnClickListener {
            sendEmergencyMessage()
        }

        // Get current location
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        // Check for location permissions
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity(), OnSuccessListener<Location> { location ->
                if (location != null) {
                    currentLocation = GeoPoint(location.latitude, location.longitude)
                    updateMapLocation(currentLocation)
                } else {
                    Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode)
        }
    }

    private fun updateMapLocation(geoPoint: GeoPoint?) {
        geoPoint?.let {
            // Update map center to current location
            mapView.controller.setCenter(it)

            // Remove previous marker if it exists
            marker?.let { mapView.overlays.remove(it) }

            // Add a new marker for current location
            marker = Marker(mapView)
            marker?.position = it
            marker?.title = "Your Location"
            mapView.overlays.add(marker)

            // Optionally, you can update the camera position to focus on the user’s location
            mapView.controller.setCenter(geoPoint)
        }
    }

    private fun sendEmergencyMessage() {
        val emergencyMessage = "I need help! Please come to my location: [Latitude: ${currentLocation?.latitude}, Longitude: ${currentLocation?.longitude}]"
        val emergencyContacts = getEmergencyContacts()

        for (contact in emergencyContacts) {
            sendSms(contact, emergencyMessage)
        }

        Toast.makeText(requireContext(), "Emergency message sent to contacts!", Toast.LENGTH_SHORT).show()
    }

    private fun sendSms(phoneNumber: String, message: String) {
        // Ensure permission is granted before sending SMS
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to send message", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "SMS permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getEmergencyContacts(): List<String> {
        // This should return a list of emergency contacts (e.g., phone numbers)
        return listOf("1234567890")  // Sample contact number
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mapView.onDetach()
    }
}
