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
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tarea1.presentation.data.model.Info
import com.example.tarea1.presentation.data.model.PlayList

/* ---------- PALETA ---------- */
private val BgTop = Color(0xFF181818)
private val BgBottom = Color(0xFF0E0E0E)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFB3B3B3)
private val DividerGray = Color(0xFF2A2A2A)
private val SpotifyGreen = Color(0xFF1DB954)

/* Alturas base para cálculos */
private val MINI_PLAYER_HEIGHT = 56.dp
private val BOTTOM_BAR_HEIGHT = 80.dp // altura de NavigationBar en Material3

/* ---------- DATA: Dua Lipa (URLs que siempre responden) ---------- */
private const val DUA_AVATAR = "https://picsum.photos/seed/dua-avatar/200"
private const val DUA_COVER  = "https://picsum.photos/seed/dua-cover/900/600"

private fun demoDuaLipa() = PlayList(
    title = "Best of Dua Lipa",
    subtitle = "Pop bangers & viral hits from Dua.",
    coverUrl = DUA_COVER,
    tracks = listOf(
        Info("Training Season", "Dua Lipa", "https://picsum.photos/seed/training-season/300"),
        Info("Houdini", "Dua Lipa", "https://picsum.photos/seed/houdini/300"),
        Info("Illusion", "Dua Lipa", "https://picsum.photos/seed/illusion/300"),
        Info("Dance The Night", "Dua Lipa", "https://picsum.photos/seed/dance-the-night/300"),
        Info("Levitating", "Dua Lipa", "https://picsum.photos/seed/levitating/300"),
        Info("New Rules", "Dua Lipa", "https://picsum.photos/seed/new-rules/300"),
        Info("Physical", "Dua Lipa", "https://picsum.photos/seed/physical/300"),
    )
)

/* ---------- ENTRADA ÚNICA ---------- */
@Composable
fun SpotifyScreen() {
    val scheme = darkColorScheme(
        primary = SpotifyGreen,
        background = BgBottom,
        surface = BgBottom
    )
    MaterialTheme(colorScheme = scheme) {
        OnePlaylistScreen(
            playlist = remember { demoDuaLipa() },
            onPlay = {},
            onTrackClick = {}
        )
    }
}

/* ---------- SCREEN ---------- */
@Composable
fun OnePlaylistScreen(
    playlist: PlayList,
    onPlay: () -> Unit,
    onTrackClick: (Info) -> Unit,
    modifier: Modifier = Modifier
) {
    // padding adicional igual al alto real de los gestos del sistema
    val navInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgTop, BgBottom)))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            // deja espacio para mini-player + bottom bar + insets
            contentPadding = PaddingValues(
                bottom = MINI_PLAYER_HEIGHT + BOTTOM_BAR_HEIGHT + navInset + 16.dp
            )
        ) {
            item {
                Header(
                    title = playlist.title,
                    subtitle = playlist.subtitle,
                    coverUrl = playlist.coverUrl,
                    artistAvatarUrl = DUA_AVATAR,
                    onPlay = onPlay
                )
            }

            itemsIndexed(playlist.tracks, key = { _, t -> t.title }) { index, t ->
                TrackRow(number = index + 1, track = t, onClick = { onTrackClick(t) })
                Divider(color = DividerGray, thickness = 0.6.dp)
            }

            item { Spacer(Modifier.height(16.dp)) }
        }

        // Bottom bar anclada abajo (NavigationBar maneja insets automáticamente)
        BottomNavBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )

        // Mini-player encima de la bottom bar + insets
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

/* ---------- PARTES UI ---------- */
@Composable
private fun Header(
    title: String,
    subtitle: String,
    coverUrl: String,
    artistAvatarUrl: String?,
    onPlay: () -> Unit
) {
    Column {
        // Imagen grande con overlay
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
        // OJO: NavigationBar ya maneja WindowInsets.navigationBars
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
