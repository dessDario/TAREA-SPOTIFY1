package com.example.tarea1.presentation.data.model

fun ArtistModel.toPlayList(songs: List<SongModel>): PlayList =
    PlayList(
        title = "Best of $name",
        subtitle = "Pop bangers & viral hits from ${name.split(" ").first()}.",
        coverUrl = headerImageUrl,
        tracks = songs.map { it.toInfo() }
    )

fun SongModel.toInfo(): Info =
    Info(
        title = title,
        artist = artists.joinToString(),
        coverUrl = coverUrl
    )
