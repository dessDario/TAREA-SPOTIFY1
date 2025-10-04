package com.example.tarea1.presentation.data.model

data class ArtistModel(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val headerImageUrl: String,
    val monthlyListeners: String,
    val verified: Boolean
)