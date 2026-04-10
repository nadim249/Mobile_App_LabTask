package com.example.photogalleryapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

class PhotoAdapter(private val context: Context, private var photoList: MutableList<Photo>) : BaseAdapter() {

    var isSelectionMode = false

    override fun getCount(): Int = photoList.size
    override fun getItem(position: Int): Photo = photoList[position]
    override fun getItemId(position: Int): Long = photoList[position].id.toLong()

    fun updateList(newList: List<Photo>) {
        this.photoList = newList.toMutableList()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false)
        val photo = photoList[position]

        val ivPhoto = view.findViewById<ImageView>(R.id.ivPhoto)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val cbSelect = view.findViewById<CheckBox>(R.id.cbSelect)

        ivPhoto.setImageResource(photo.resourceId)
        tvTitle.text = photo.title

        cbSelect.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
        cbSelect.isChecked = photo.isSelected

        return view
    }
}