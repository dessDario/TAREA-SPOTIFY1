package com.example.tarea1.presentation.data.model

data class SongModel(
    val id: String,
    val title: String,
    val artists: List<String>,
    val album: String,
    val coverUrl: String,
    val duration: String,
    val isExplicit: Boolean = false
)
