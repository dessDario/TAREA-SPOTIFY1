package com.example.tarea1.presentation.data.model

data class AlbumModel(
    val id: String,
    val title: String,
    val coverUrl: String,
    val year: Int,
    val type: AlbumType
)
