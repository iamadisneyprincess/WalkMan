package com.example.walkwatch.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to the Walk Watch Dashboard"
    }
    val text: LiveData<String> = _text

    private val _riskLevel = MutableLiveData<String>().apply {
        value = "Low" // Default risk level
    }
    val riskLevel: LiveData<String> = _riskLevel

    // Function to update risk level dynamically
    fun updateRiskLevel(level: String) {
        _riskLevel.value = level
    }
}
