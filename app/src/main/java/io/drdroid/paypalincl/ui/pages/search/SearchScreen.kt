package io.drdroid.paypalincl.ui.pages.search

import android.text.Html
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import io.drdroid.paypalincl.data.model.tv_show.ShowModel
import io.drdroid.paypalincl.data.vm.ShowViewModel
import io.drdroid.paypalincl.ui.components.DisplayImage
import io.drdroid.paypalincl.ui.components.SearchBar
import io.drdroid.paypalincl.ui.navigation.Screen
import io.drdroid.paypalincl.ui.theme.PrimaryDark
import io.drdroid.paypalincl.ui.theme.SecondaryDark
import io.drdroid.paypalincl.utils.Utils.badgeLayout
import io.drdroid.paypalincl.utils.Utils.isDark
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navigateTo: (String, Map<String, Any?>) -> Unit,
    showViewModel: ShowViewModel = hiltViewModel(),
) {
    val shows by showViewModel.searchLiveData.observeAsState()
    val loading by showViewModel.isLoading.observeAsState(initial = false)

//    val scrollState = rememberScrollState()
//    if (scrollState.canScrollForward) {
//
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(color = SecondaryDark)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                //search bar
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
                    onSearch = { showViewModel.search(q = it) }
                )
            }
            if (loading) {
                item {
                    CircularProgressIndicator()
                }
            }
            items(shows.orEmpty()) { show ->
                ShowItem(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp),
                    show = show.show,
                    score = show.score,
                    onClick = {
                        navigateTo.invoke(Screen.Details.route, mapOf("show" to Gson().toJson(show.show)))
                    }
                )
            }
        }
    }
}

@Composable
fun ShowItem(
    modifier: Modifier = Modifier,
    score: Double? = null,
    show: ShowModel,
    onClick: () -> Unit,
) {
    val coroutine = rememberCoroutineScope()
    var dominantColor by remember { mutableStateOf(SecondaryDark) }
    val dominantColorAnim = remember { Animatable(dominantColor) }

    var secondaryColor by remember { mutableStateOf(PrimaryDark) }
    val secondaryColorAnim = remember { Animatable(secondaryColor) }

    LaunchedEffect(key1 = dominantColor, block = {
        coroutine.launch {
            dominantColorAnim.animateTo(
                targetValue = dominantColor,
                animationSpec = tween(500)
            )
        }
    })
    LaunchedEffect(key1 = secondaryColor, block = {
        coroutine.launch {
            secondaryColorAnim.animateTo(
                targetValue = secondaryColor,
                animationSpec = tween(500)
            )
        }
    })
    Card(
        colors = CardDefaults.cardColors(
            containerColor = dominantColorAnim.value
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(164.dp)
            .clickable { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DisplayImage(
                modifier = Modifier/*.weight(3f)*/,
                imgUrl = show.image?.medium,
                onPaletteExported = {
                    dominantColor = if (it.dominantSwatch?.rgb == null) {
                        SecondaryDark
                    } else {
                        Color(it.dominantSwatch?.rgb!!)
                    }
                    secondaryColor = if (it.darkVibrantSwatch?.rgb == null) {
                        PrimaryDark
                    } else {
                        Color(it.darkVibrantSwatch?.rgb!!)
                    }
                },
                onImageClicked = {
                    show.image?.original
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, top = 4.dp)
                /*.weight(4f)*/,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                LazyRow(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 4.dp, end = 2.dp)
                ) {
                    items(show.genres) { genre ->
                        Text(
                            color = if (secondaryColorAnim.value.isDark()) Color.White else Color.Black,
                            text = genre,
                            fontSize = TextUnit(10f, TextUnitType.Sp),
                            modifier = Modifier
                                .background(
                                    color = secondaryColorAnim.value,
                                    shape = CircleShape
                                )
                                .padding(4.dp)
                                .badgeLayout()
                                .clickable { /* Todo */ }
                                .align(Alignment.End)
                        )
                    }
                }
                Text(
                    text = show.name ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (dominantColorAnim.value.isDark()) Color.White else Color.Black,
                )

                Text(
                    color = if (dominantColorAnim.value.isDark()) Color.White else Color.Black,
                    text = show.language ?: "",
                    style = MaterialTheme.typography.titleSmall

                )
                show.premiered?.let {
                    Text(
                        color = if (dominantColorAnim.value.isDark()) Color.White else Color.Black,
                        text = "${show.premiered} : ${
                            if (show.status == "Ended") {
                                show.ended
                            } else {
                                "ongoing"
                            }
                        }",
//                        style = MaterialTheme.typography.titleSmall
                    )
                }


                show.summary?.let {
                    Text(
                        color = if (dominantColorAnim.value.isDark()) Color.White else Color.Black,
                        text = Html.fromHtml(show.summary).toString(),
                        letterSpacing = TextUnit(1f, TextUnitType.Sp),
                        lineHeight = TextUnit(14f, TextUnitType.Sp),
                        maxLines = 3,
                        fontSize = TextUnit(12f, TextUnitType.Sp),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}