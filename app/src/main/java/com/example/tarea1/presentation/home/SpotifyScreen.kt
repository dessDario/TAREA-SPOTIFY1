package com.example.tarea1.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tarea1.presentation.data.model.* // <-- importa ArtistModel, SongModel, AlbumModel, AlbumType, Info, PlayList y mappers

/* ---------- PALETA colores chi */
private val BgTop = Color(0xFF181818)
private val BgBottom = Color(0xFF0E0E0E)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFB3B3B3)
private val DividerGray = Color(0xFF2A2A2A)
private val SpotifyGreen = Color(0xFF1DB954)

private val MINI_PLAYER_HEIGHT = 56.dp
private val BOTTOM_BAR_HEIGHT = 80.dp

/* ---------- MOCKS  ---------- */
private val mockArtist = ArtistModel(
    id = "art_dua",
    name = "Dua Lipa",
    avatarUrl = "https://picsum.photos/seed/dua-avatar/200",
    headerImageUrl = "https://picsum.photos/seed/dua-cover/900/600",
    monthlyListeners = "45.1M monthly listeners",
    verified = true
)

private val mockSongs: List<SongModel> = listOf(
    SongModel("s1","Training Season", listOf("Dua Lipa"),"Radical Optimism","https://picsum.photos/seed/training-season/300","3:29"),
    SongModel("s2","Houdini",         listOf("Dua Lipa"),"Radical Optimism","https://picsum.photos/seed/houdini/300","3:04"),
    SongModel("s3","Illusion",        listOf("Dua Lipa"),"Radical Optimism","https://picsum.photos/seed/illusion/300","3:08"),
    SongModel("s4","Dance The Night", listOf("Dua Lipa"),"Barbie: The Album","https://picsum.photos/seed/dance-the-night/300","2:56"),
    SongModel("s5","Levitating",      listOf("Dua Lipa"),"Future Nostalgia","https://picsum.photos/seed/levitating/300","3:24"),
    SongModel("s6","New Rules",       listOf("Dua Lipa"),"Dua Lipa","https://picsum.photos/seed/new-rules/300","3:29"),
    SongModel("s7","Physical",        listOf("Dua Lipa"),"Future Nostalgia","https://picsum.photos/seed/physical/300","3:13"),
)

@Suppress("unused")
private val mockReleases: List<AlbumModel> = listOf(
    AlbumModel("alb1","Radical Optimism","https://picsum.photos/seed/radical-optimism/400",2024,AlbumType.ALBUM),
    AlbumModel("alb2","Future Nostalgia","https://picsum.photos/seed/future-nostalgia/400",2020,AlbumType.ALBUM),
)

/*  ENTRADA ÚNICA  */
@Composable
fun SpotifyScreen() {
    val scheme = darkColorScheme(
        primary = SpotifyGreen,
        background = BgBottom,
        surface = BgBottom
    )
    MaterialTheme(colorScheme = scheme) {
        // Adaptamos los mocks a tu UI (PlayList/Info) con el mapper
        val playlist = remember { mockArtist.toPlayList(mockSongs) }

        OnePlaylistScreen(
            playlist = playlist,
            onPlay = {},
            onTrackClick = {}
        )
    }
}

/* SCREEN  */
@Composable
fun OnePlaylistScreen(
    playlist: PlayList,
    onPlay: () -> Unit,
    onTrackClick: (Info) -> Unit,
    modifier: Modifier = Modifier
) {
    val navInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgTop, BgBottom)))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = MINI_PLAYER_HEIGHT + BOTTOM_BAR_HEIGHT + navInset + 16.dp
            )
        ) {
            item {
                Header(
                    title = playlist.title,
                    subtitle = playlist.subtitle,
                    coverUrl = playlist.coverUrl,
                    artistAvatarUrl = mockArtist.avatarUrl, // avatar desde el modelo del paquete
                    onPlay = onPlay
                )
            }

            itemsIndexed(playlist.tracks, key = { _, t -> t.title }) { index, t ->
                TrackRow(number = index + 1, track = t, onClick = { onTrackClick(t) })
                Divider(color = DividerGray, thickness = 0.6.dp)
            }

            item { Spacer(Modifier.height(16.dp)) }
        }

        BottomNavBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )

        MiniPlayerBar(
            title = "Dreamer",
            artist = "Martin Garrix",
            onTap = { /* abrir player */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = BOTTOM_BAR_HEIGHT + navInset)
                .fillMaxWidth(),
            imagePainter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data("https://picsum.photos/seed/miniplayer/120")
                    .crossfade(true)
                    .build()
            )
        )
    }
}

/*  PARTES UI  */
@Composable
private fun Header(
    title: String,
    subtitle: String,
    coverUrl: String,
    artistAvatarUrl: String?,
    onPlay: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(coverUrl)
                    .crossfade(true)
                    .build(),
                placeholder = ColorPainter(DividerGray),
                error = ColorPainter(DividerGray),
                contentDescription = "cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color(0x99000000),
                            0.65f to Color.Transparent,
                            1f to BgBottom.copy(alpha = 0.95f)
                        )
                    )
            )
            if (artistAvatarUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(artistAvatarUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = ColorPainter(DividerGray),
                    error = ColorPainter(DividerGray),
                    contentDescription = "artist",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 12.dp)
                        .size(64.dp)
                        .clip(CircleShape)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(title, color = TextPrimary, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(6.dp))
            Text(subtitle, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(6.dp))
            Text("34,480,238 saves • 2h 38m", color = TextSecondary, style = MaterialTheme.typography.labelLarge)
        }

        Spacer(Modifier.height(12.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.FavoriteBorder, null, tint = TextPrimary)
            Spacer(Modifier.width(16.dp))
            Icon(Icons.Outlined.Download, null, tint = TextPrimary)
            Spacer(Modifier.width(16.dp))
            Icon(Icons.Outlined.Share, null, tint = TextPrimary)

            Spacer(Modifier.weight(1f))

            FloatingActionButton(
                onClick = onPlay,
                containerColor = SpotifyGreen,
                shape = CircleShape
            ) {
                Icon(Icons.Rounded.PlayArrow, contentDescription = "Play", tint = Color.Black)
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun TrackRow(number: Int, track: Info, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$number", color = TextSecondary, modifier = Modifier.width(24.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.coverUrl)
                .crossfade(true)
                .build(),
            placeholder = ColorPainter(DividerGray),
            error = ColorPainter(DividerGray),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(6.dp))
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                track.title,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                track.artist,
                color = TextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(Icons.Outlined.MoreVert, null, tint = TextSecondary)
    }
}

/* ---------- MINI-PLAYER ---------- */
@Composable
private fun MiniPlayerBar(
    title: String,
    artist: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    imagePainter: Painter
) {
    Surface(
        color = Color(0xFF1A1A1A),
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
        modifier = modifier.clickable { onTap() }
    ) {
        Row(
            Modifier
                .height(MINI_PLAYER_HEIGHT)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(artist, color = TextSecondary, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Rounded.PlayArrow, null, tint = TextPrimary)
        }
    }
}

/* ---------- BOTTOM NAV BAR (Material3) ---------- */
@Composable
private fun BottomNavBar(modifier: Modifier = Modifier, selectedIndex: Int = 0) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFF121212),
        tonalElevation = 2.dp
    ) {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = { },
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
            label = { Text("Home") },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = { },
            icon = { Icon(Icons.Outlined.Search, contentDescription = "Search") },
            label = { Text("Search") },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = { },
            icon = { Icon(Icons.Outlined.LibraryMusic, contentDescription = "Your Library") },
            label = { Text("Your Library") },
            alwaysShowLabel = true
        )
    }
}
