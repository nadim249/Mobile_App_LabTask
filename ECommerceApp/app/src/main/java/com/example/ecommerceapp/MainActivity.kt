package com.example.ecommerceapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ProductAdapter
    private var productList = mutableListOf<Product>()
    private var currentCategory = "All"
    private var isGrid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val search = findViewById<EditText>(R.id.searchView)
        val toggle = findViewById<ImageView>(R.id.btnToggle)
        val btnCart = findViewById<ImageView>(R.id.btnCart)
        val txtCartBadge = findViewById<TextView>(R.id.txtCartBadge)
        val emptyState = findViewById<LinearLayout>(R.id.emptyState)

        productList = generateProducts()
        setupCategories(search)

        adapter = ProductAdapter(productList.toMutableList()) { product ->
            product.inCart = true
            updateCartBadge(txtCartBadge)
            Toast.makeText(this, "Added to cart: ${product.name}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnCart.setOnClickListener {
            val cartItems = productList.filter { it.inCart }
            val intent = android.content.Intent(this, CartActivity::class.java)
            intent.putExtra("cart_items", ArrayList(cartItems))
            startActivity(intent)
        }

        toggle.setOnClickListener {
            isGrid = !isGrid
            adapter.isGrid = isGrid

            recyclerView.layoutManager = if (isGrid)
                GridLayoutManager(this, 2)
            else
                LinearLayoutManager(this)

            toggle.setImageResource(
                if (isGrid) android.R.drawable.ic_menu_agenda
                else android.R.drawable.ic_menu_sort_by_size
            )
            adapter.notifyDataSetChanged()
        }

        search.addTextChangedListener { s ->
            applyFilters(s.toString(), emptyState)
        }

        val helper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.swapItems(vh.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val position = vh.adapterPosition
                val deleted = adapter.removeItem(position)
                
                if (deleted.inCart) updateCartBadge(txtCartBadge)

                Snackbar.make(recyclerView, "${deleted.name} removed", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        adapter.restoreItem(deleted, position)
                        if (deleted.inCart) updateCartBadge(txtCartBadge)
                    }.show()
            }
        })

        helper.attachToRecyclerView(recyclerView)
    }

    private fun applyFilters(query: String, emptyState: View) {
        val filtered = productList.filter { product ->
            val matchesCategory = if (currentCategory == "All") true else product.category == currentCategory
            val matchesSearch = product.name.contains(query, ignoreCase = true) || product.category.contains(query, ignoreCase = true)
            matchesCategory && matchesSearch
        }
        adapter.updateList(filtered)
        emptyState.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateCartBadge(badge: TextView) {
        val count = productList.count { it.inCart }
        if (count > 0) {
            badge.text = count.toString()
            badge.visibility = View.VISIBLE
        } else {
            badge.visibility = View.GONE
        }
    }

    private fun setupCategories(search: EditText) {
        val categories = listOf("All", "Electronics", "Clothing", "Books", "Food", "Toys")
        val categoryLayout = findViewById<LinearLayout>(R.id.categoryLayout)
        val emptyState = findViewById<LinearLayout>(R.id.emptyState)

        categories.forEach { category ->
            val btn = android.widget.Button(this).apply {
                text = category
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 8, 0)
                }
                setOnClickListener {
                    currentCategory = category
                    applyFilters(search.text.toString(), emptyState)
                }
            }
            categoryLayout.addView(btn)
        }
    }

    private fun generateProducts(): MutableList<Product> {
        return mutableListOf(
            Product(1, "Smartphone", 599.99, 4.5f, "Electronics", R.drawable.smartphone),
            Product(2, "Cotton T-Shirt", 19.99, 4.0f, "Clothing", R.drawable.cloth),
            Product(3, "Android Dev Book", 45.0, 5.0f, "Books", R.drawable.book),
            Product(4, "Wireless Headphones", 120.0, 4.8f, "Electronics", R.drawable.headphn),
            Product(5, "Running Shoes", 85.0, 4.2f, "Clothing", R.drawable.cloth),
            Product(6, "Gaming Mouse", 60.0, 4.6f, "Electronics", R.drawable.smartphone),
            Product(7, "Coffee Mug", 12.0, 4.3f, "Food", R.drawable.cart),
            Product(8, "Action Figure", 25.0, 4.7f, "Toys", R.drawable.cart)
        )
    }
}