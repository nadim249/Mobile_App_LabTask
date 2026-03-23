package com.example.contactbookapp

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat

class ContactAdapter(context: Context, private var contactList: MutableList<Contact>) :
    ArrayAdapter<Contact>(context, 0, contactList), Filterable {

    private var filteredList: MutableList<Contact> = contactList

    override fun getCount(): Int = filteredList.size
    override fun getItem(position: Int): Contact? = filteredList[position]

    private class ViewHolder(view: View) {
        val tvAvatar: TextView = view.findViewById(R.id.tvAvatar)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvPhone: TextView = view.findViewById(R.id.tvPhone)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val contact = getItem(position)
        if (contact != null) {
            holder.tvName.text = contact.name
            holder.tvPhone.text = contact.phone
            holder.tvAvatar.text = contact.initial.toString()

            val colors = intArrayOf(R.color.blue, R.color.green, R.color.orange, R.color.pink, R.color.teal_700)
            val colorRes = colors[contact.initial.code % colors.size]
            holder.tvAvatar.background.setColorFilter(
                ContextCompat.getColor(context, colorRes), PorterDuff.Mode.SRC_IN
            )

        }
        return view
    }
    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""

                val resultList = if (query.isEmpty()) {
                    contactList.toMutableList()
                } else {
                    contactList.filter {
                        it.name.lowercase().contains(query)
                    }.toMutableList()
                }

                return FilterResults().apply {
                    values = resultList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as MutableList<Contact>
                notifyDataSetChanged()
            }
        }
    }
}