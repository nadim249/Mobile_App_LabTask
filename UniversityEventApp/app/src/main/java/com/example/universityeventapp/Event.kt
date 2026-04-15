package com.example.universityeventapp


import java.io.Serializable

data class Event(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val venue: String,
    val category: String,
    val description: String,
    val price: Double,
    val totalSeats: Int,
    var availableSeats: Int,
    val imageRes: Int
) : Serializable
