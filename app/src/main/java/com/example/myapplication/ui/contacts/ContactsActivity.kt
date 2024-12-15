package com.example.myapplication.ui.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.R

class ContactsActivity : AppCompatActivity() {
    private var contactsListView: ListView? = null
    private var closeButton: Button? = null
    private val contactsList = ArrayList<String>()
    private val contactsPhoneNumbers = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        contactsListView = findViewById(R.id.contactsListView)
        closeButton = findViewById(R.id.closeButton)

        // Check if the contacts permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CODE)
        }

        // Handle close button
        closeButton?.setOnClickListener { finish() }

        // Set up item click listener for the ListView
        contactsListView?.setOnItemClickListener { _, _, position, _ ->
            val phoneNumber = contactsPhoneNumbers[position]
            openMessagingApp(phoneNumber)
        }
    }

    private fun loadContacts() {
        val contentResolver = contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        // Check if the cursor is not null and contains data
        cursor?.use {
            if (it.count > 0) {
                while (it.moveToNext()) {
                    val contactNameColumnIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val contactIdColumnIndex = it.getColumnIndex(ContactsContract.Contacts._ID)

                    // Check if the column indexes are valid
                    if (contactNameColumnIndex != -1 && contactIdColumnIndex != -1) {
                        val contactName = it.getString(contactNameColumnIndex)
                        val contactId = it.getString(contactIdColumnIndex)

                        // Get phone number associated with the contact
                        val phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(contactId),
                            null
                        )

                        phoneCursor?.use { cursorPhone ->
                            if (cursorPhone.moveToFirst()) {
                                val phoneNumberColumnIndex = cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                if (phoneNumberColumnIndex != -1) {
                                    val phoneNumber = cursorPhone.getString(phoneNumberColumnIndex)
                                    contactsList.add("$contactName - $phoneNumber")
                                    contactsPhoneNumbers.add(phoneNumber)
                                } else {
                                    contactsList.add("$contactName - No phone number")
                                }
                            } else {
                                contactsList.add("$contactName - No phone number")
                            }
                        }
                    }
                }
            } else {
                // If cursor is empty, show a message
                contactsList.add("No contacts found")
            }
        }

        // Set up the adapter for the ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactsList)
        contactsListView?.adapter = adapter
    }

    // Function to open the messaging app with the phone number
    private fun openMessagingApp(phoneNumber: String) {
        if (phoneNumber != "No phone number") {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:$phoneNumber")  // URI for SMS with phone number
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "No phone number available for this contact", Toast.LENGTH_SHORT).show()
        }
    }

    // Permission request result handling
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
