package com.example.ecommerceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class ProductAdapter(
    private var products: MutableList<Product>,
    private val onCartClick: (Product) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isGrid = false

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgProduct)
        val name: TextView = view.findViewById(R.id.txtName)
        val category: TextView = view.findViewById(R.id.txtCategory)
        val price: TextView = view.findViewById(R.id.txtPrice)
        val rating: android.widget.RatingBar = view.findViewById(R.id.ratingBar)
        val btn: Button = view.findViewById(R.id.btnCart)
    }

    inner class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgProduct)
        val name: TextView = view.findViewById(R.id.txtName)
        val price: TextView = view.findViewById(R.id.txtPrice)
        val btn: ImageButton = view.findViewById(R.id.btnCart)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGrid) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_list, parent, false)
            ListViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_grid, parent, false)
            GridViewHolder(v)
        }
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = products[position]

        if (holder is ListViewHolder) {
            holder.img.setImageResource(product.imageRes)
            holder.name.text = product.name
            holder.category.text = product.category
            holder.price.text = "$${product.price}"
            holder.rating.rating = product.rating
            holder.btn.setOnClickListener { onCartClick(product) }
        } else if (holder is GridViewHolder) {
            holder.img.setImageResource(product.imageRes)
            holder.name.text = product.name
            holder.price.text = "$${product.price}"
            holder.btn.setOnClickListener { onCartClick(product) }
        }
    }

    fun updateList(newList: List<Product>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = products.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return products[oldItemPosition].id == newList[newItemPosition].id
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return products[oldItemPosition] == newList[newItemPosition]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        products.clear()
        products.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(pos: Int): Product {
        val item = products.removeAt(pos)
        notifyItemRemoved(pos)
        return item
    }

    fun restoreItem(item: Product, pos: Int) {
        products.add(pos, item)
        notifyItemInserted(pos)
    }

    fun swapItems(from: Int, to: Int) {
        Collections.swap(products, from, to)
        notifyItemMoved(from, to)
    }
}