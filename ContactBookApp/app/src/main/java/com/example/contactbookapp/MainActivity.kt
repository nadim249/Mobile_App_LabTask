package com.example.contactbookapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ContactAdapter
    private val contacts = mutableListOf<Contact>()
    private lateinit var tvEmptyState: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView: ListView = findViewById(R.id.listView)
        val searchView: SearchView = findViewById(R.id.searchView)
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        // Initial Data
        contacts.add(Contact("Alice Smith", "01711223344", "alice@test.com"))
        contacts.add(Contact("Bob Johnson", "01855667788", "bob@test.com"))

        adapter = ContactAdapter(this, contacts)
        listView.adapter = adapter
        listView.emptyView = tvEmptyState

        // Search Filtering
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        fabAdd.setOnClickListener { showAddContactDialog() }


        listView.setOnItemClickListener { _, _, position, _ ->
            val contact = adapter.getItem(position)
            Toast.makeText(this, "Name: ${contact?.name}\nEmail: ${contact?.email}", Toast.LENGTH_LONG).show()
        }

        // Delete
        listView.setOnItemLongClickListener { _, _, position, _ ->
            val contact = adapter.getItem(position)
            AlertDialog.Builder(this)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete ${contact?.name}?")
                .setPositiveButton("Yes") { _, _ ->
                    contacts.remove(contact)
                    adapter.notifyDataSetChanged()
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }

    private fun showAddContactDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etPhone = dialogView.findViewById<EditText>(R.id.etPhone)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)

        AlertDialog.Builder(this)
            .setTitle("Add New Contact")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString()
                val phone = etPhone.text.toString()
                val email = etEmail.text.toString()

                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    contacts.add(Contact(name, phone, email))
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Name and Phone required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}