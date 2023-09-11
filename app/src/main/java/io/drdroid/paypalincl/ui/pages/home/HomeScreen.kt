package io.drdroid.paypalincl.ui.pages.home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import com.google.gson.Gson
import io.drdroid.paypalincl.R
import io.drdroid.paypalincl.data.model.tv_show.ShowModel
import io.drdroid.paypalincl.data.vm.ShowViewModel
import io.drdroid.paypalincl.ui.components.CustomDialog
import io.drdroid.paypalincl.ui.navigation.Screen
import io.drdroid.paypalincl.ui.pages.search.ShowItem
import io.drdroid.paypalincl.ui.theme.OnPrimaryDark
import io.drdroid.paypalincl.utils.Common
import io.drdroid.paypalincl.utils.Utils
import io.drdroid.paypalincl.utils.Utils.isDark
import java.util.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navigateTo: (String, Map<String, Any?>) -> Unit,
    showViewModel: ShowViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val shows: LazyPagingItems<ShowModel> =
        showViewModel.getShows()
            .collectAsLazyPagingItems()

    var notificationDenied by rememberSaveable { mutableStateOf(false) }
    var notificationDialog by remember { mutableStateOf(false) }
    var requestNotificationPermission by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = {
        if (!Utils.hasPermissions(
                context = context,
                permissions = arrayOf(Common.PERMISSIONS.PERMISSION_NOTIFICATION)
            )
            &&
            !notificationDenied
        ) {
            notificationDialog = true
        }
    })

    if (notificationDialog) {
        CustomDialog(
            cancellable = false,
            title = stringResource(R.string.notification_permission_required),
            message = stringResource(R.string.notification_permission_message),
            icon = Icons.Filled.Notifications,
            positiveText = stringResource(id = R.string.grant),
            negativeText = stringResource(id = R.string.not_now),
            onPositiveClick = {
                requestNotificationPermission = true
            },
            onNegativeClick = {
                Toast.makeText(context, ":( Maybe next time", Toast.LENGTH_SHORT).show()
                notificationDenied = true
            },
            onDismiss = {
                notificationDialog = false
            }
        )
    }
    if (requestNotificationPermission) {
        Utils.RequestPermission(
            context = context,
            permission = Common.PERMISSIONS.PERMISSION_NOTIFICATION,
            onPermissionResult = {
                if (it) {
                    Toast.makeText(context, "Nice, you are good to go.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, ":( Permission not granted", Toast.LENGTH_SHORT).show()
                }
                requestNotificationPermission = false
            }
        )
    }

//    var all = stringResource(id = R.string.all)
    var selectedCategory by remember { mutableStateOf(context.getString(R.string.all)) }

    var headerHeight by remember { mutableStateOf(128.dp) }
//    var scrollState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
//        state = scrollState,
    ) {
        item {
            val genres: MutableList<String> =
                shows.itemSnapshotList.items.asSequence().filter { it.genres.isNotEmpty() }
                    .map { showModel -> showModel.genres }.map { l -> l.first() }.toSet()
                    .sorted().toMutableList()
            genres.add(0, stringResource(R.string.all))

            LazyRow(
                modifier = Modifier
                    .height(headerHeight)
            ) {
                items(genres) { item ->
                    CategoryCard(
                        item = item,
                        selected = item == selectedCategory,
                        onClick = {
                            selectedCategory = item
                        }
                    )
                }
            }
        }
        items(
            count = shows.itemCount,
            contentType = shows.itemContentType { "contentType" }
        ) { position ->
            val show = shows[position]
            if (selectedCategory == stringResource(R.string.all) || show?.genres?.contains(
                    selectedCategory
                ) == true
            ) {
                show?.let {
                    ShowItem(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp),
                        show = show,
                        onClick = {
                            navigateTo.invoke(Screen.Details.route, mapOf("show" to Gson().toJson(it)))
                        }
                    )
                }
            }
        }

        when (val state = shows.loadState.refresh) {
            is LoadState.Error -> {
                //TODO Error Item
            }

            is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }

            is LoadState.NotLoading -> {
            }

            else -> {}
        }

        when (val state = shows.loadState.append) {
            is LoadState.Error -> {
                //TODO Pagination Error Item
                state.error.printStackTrace()
            }

            is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (state.endOfPaginationReached) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            androidx.compose.material.Text(
                                text = stringResource(id = R.string.end_of_page),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            else -> {}
        }
    }


//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .fillMaxWidth()
//            .padding(top = 4.dp)
//    ) {
//
//    }
}

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    item: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color by remember { mutableIntStateOf(randomColor()) }

    Card(
        border = if (selected) {
            BorderStroke(width = 4.dp, color = OnPrimaryDark)
        } else {
            null
        },
        colors = CardDefaults.cardColors(
            containerColor = Color(color)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        modifier = modifier
            .aspectRatio(1f)
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item,
                color = if (color.isDark()) {
                    Color.White
                } else {
                    Color.Black
                },
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

fun randomColor(): Int {
    val rnd = Random()
    return android.graphics.Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
}