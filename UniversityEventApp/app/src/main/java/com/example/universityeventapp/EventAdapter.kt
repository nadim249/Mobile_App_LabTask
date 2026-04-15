package com.example.universityeventapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView


class EventAdapter(private var events: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBanner: ImageView = view.findViewById(R.id.ivEventBanner)
        val tvTitle: TextView = view.findViewById(R.id.tvEventTitle)
        val tvDate: TextView = view.findViewById(R.id.tvEventDate)
        val tvSeats: TextView = view.findViewById(R.id.tvAvailableSeats)
        val tvPrice: TextView = view.findViewById(R.id.tvEventPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.tvTitle.text = event.title
        holder.tvDate.text = "${event.date} • ${event.venue}"
        holder.tvSeats.text = "Seats: ${event.availableSeats}/${event.totalSeats}"
        holder.tvPrice.text = "$${String.format("%.2f", event.price)}"
        holder.ivBanner.setImageResource(event.imageRes)

        // Pass data to Screen 3
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EventDetailActivity::class.java)
            intent.putExtra("EVENT_DATA", event)
            
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                holder.ivBanner,
                "event_image_transition"
            )
            context.startActivity(intent, options.toBundle())
        }
    }

    override fun getItemCount() = events.size

    fun updateData(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }
}