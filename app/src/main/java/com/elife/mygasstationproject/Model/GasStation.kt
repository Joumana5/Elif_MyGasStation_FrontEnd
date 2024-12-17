package com.elife.mygasstationproject.Model

data class GasStation(
    val id: String,
    val name: String,
    val available: Boolean,
    val yearOfCreation: Int,
    val address: String,
    val location: String,
    val manager: String?
)
