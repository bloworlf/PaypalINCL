package io.drdroid.paypalincl.ui.pages.details

import android.text.Html
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import io.drdroid.paypalincl.R
import io.drdroid.paypalincl.data.model.tv_show.EpisodeModel
import io.drdroid.paypalincl.data.model.tv_show.SeasonModel
import io.drdroid.paypalincl.data.model.tv_show.ShowModel
import io.drdroid.paypalincl.data.vm.SeasonViewModel
import io.drdroid.paypalincl.ui.components.DisplayImage
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    show: ShowModel,
    onColorChanged: (Palette) -> Unit,
    seasonViewModel: SeasonViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    seasonViewModel.getShowSeasons(show.id)
    val seasons by seasonViewModel.seasons.observeAsState()

    val episodes by seasonViewModel.episodes.observeAsState()

    var titleTextColor by remember { mutableStateOf(Color.White) }
    var bodyTextColor by remember { mutableStateOf(Color.White) }

    var selectedSeason by remember { mutableIntStateOf(-1) }

    val seasonListState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        //show description
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = show.name ?: "",
                    style = MaterialTheme.typography.displaySmall,
                    color = titleTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )

                DisplayImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    imgUrl = show.image?.original,
                    onPaletteExported = {
                        onColorChanged.invoke(it)
                        titleTextColor = Color(it.darkVibrantSwatch?.titleTextColor ?: 0)
                        bodyTextColor = Color(it.darkVibrantSwatch?.bodyTextColor ?: 0)
                    },
                    aspectRatio = 16 / 9f,
                    onImageClicked = { show.image?.original }
                )

                show.summary?.let {
                    Text(
                        text = Html.fromHtml(it).toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = bodyTextColor,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        //show seasons
        item {
            LazyRow(
                state = seasonListState,
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(seasons.orEmpty()) { position, season ->
                    SeasonItem(
                        modifier = Modifier.padding(8.dp),
                        season = season,
                        selected = position == selectedSeason,
                        onItemClick = {
                            scope.launch {
                                seasonListState.animateScrollToItem(position, 0)
                            }
                            selectedSeason = position
                            seasonViewModel.getEpisodesSeason(season.id)
                        }
                    )
                }
            }
        }

        items(episodes.orEmpty()) { episode ->
            EpisodeItem(
                modifier = Modifier.padding(8.dp),
                episode = episode,
                onItemClick = {}
            )
        }
    }
}

@Composable
fun SeasonItem(
    modifier: Modifier = Modifier,
    season: SeasonModel,
    selected: Boolean,
    onItemClick: () -> Unit
) {
    val coroutine = rememberCoroutineScope()

    var primaryColor by remember { mutableStateOf(Color.White) }
    val primaryColorAnim = remember { Animatable(primaryColor) }
    LaunchedEffect(key1 = primaryColor, block = {
        coroutine.launch {
            primaryColorAnim.animateTo(
                targetValue = primaryColor,
                animationSpec = tween(500)
            )
        }
    })
    var textColor by remember { mutableStateOf(Color.White) }
    val textColorAnim = remember { Animatable(textColor) }
    LaunchedEffect(key1 = textColor, block = {
        coroutine.launch {
            textColorAnim.animateTo(
                targetValue = textColor,
                animationSpec = tween(500)
            )
        }
    })

    val bgCol = if (selected) primaryColorAnim.value else Color.White
    val txtCol = if (selected) textColorAnim.value else Color.Black

    Card(
        colors = CardDefaults.cardColors(
            containerColor = bgCol
        ),
        modifier = modifier
            .height(100.dp)
            .width(300.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = if (selected) {
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        } else {
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = bgCol)
                .clickable { onItemClick.invoke() }
//                .aspectRatio(21 / 9f)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            season.image?.let { img ->
                DisplayImage(
//                aspectRatio = 9/16f,
                    modifier = Modifier
                        .weight(4f)
                        .fillMaxWidth()
//                    .fillMaxHeight()
                    ,
                    imgUrl = img.medium,
                    shape = RectangleShape,
                    onPaletteExported = {
                        primaryColor = Color(it.dominantSwatch?.rgb ?: Color.White.toArgb())
                        textColor =
                            Color(it.dominantSwatch?.titleTextColor ?: Color.Black.toArgb())
                    },
                    onImageClicked = { img.original }
                )
            }

            Text(
                color = txtCol,
                text = stringResource(R.string.season, season.number!!),
                modifier = Modifier.weight(6f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }

}

@Composable
fun EpisodeItem(
    modifier: Modifier = Modifier,
    episode: EpisodeModel,
    onItemClick: () -> Unit
) {
    Card(
        modifier = modifier
//            .height(100.dp),
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onItemClick.invoke() }
                .aspectRatio(21 / 9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DisplayImage(
                aspectRatio = 1f,
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight(),
                imgUrl = episode.image?.medium,
                shape = RectangleShape,
                onPaletteExported = {
                },
                onImageClicked = { episode.image?.original }
            )

            Column(
                modifier = Modifier
                    .weight(4f)
                    .padding(start = 4.dp)
            ) {
                episode.name?.let {
                    Text(
                        text = it,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Text(text = "${episode.runtime} min")

                episode.summary?.let {
                    Text(
                        text = Html.fromHtml(it).toString(),
                        letterSpacing = TextUnit(1f, TextUnitType.Sp),
                        lineHeight = TextUnit(14f, TextUnitType.Sp),
                        maxLines = 3,
                        fontSize = TextUnit(12f, TextUnitType.Sp),
                    )
                }

            }
        }
    }
}