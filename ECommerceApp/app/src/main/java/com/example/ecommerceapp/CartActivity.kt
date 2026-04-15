package com.example.ecommerceapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val cartItems = intent.getSerializableExtra("cart_items") as? ArrayList<Product> ?: arrayListOf()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCart)
        val txtTotal = findViewById<TextView>(R.id.txtTotal)

        val adapter = ProductAdapter(cartItems) { product ->

        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val total = cartItems.sumOf { it.price }
        txtTotal.text = "Total: $$total"
    }
}