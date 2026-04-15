package com.example.universityeventapp

object BookingManager {
    private val bookedEvents = mutableListOf<Event>()

    fun addBooking(event: Event) {
        if (!bookedEvents.any { it.id == event.id }) {
            bookedEvents.add(event)
        }
    }

    fun getBookedEvents(): List<Event> {
        return bookedEvents
    }
}