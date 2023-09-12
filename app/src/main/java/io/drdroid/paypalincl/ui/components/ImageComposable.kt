package io.drdroid.paypalincl.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.facebook.share.model.ShareMessengerGenericTemplateContent.ImageAspectRatio
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.palette.PalettePlugin
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import io.drdroid.paypalincl.ui.pages.view.FullScreen
import io.drdroid.paypalincl.ui.pages.view.FullScreenState
import io.drdroid.paypalincl.ui.pages.view.LocalFullScreenComposableReference
import io.drdroid.paypalincl.ui.pages.view.LocalMutableFullScreenState
import io.drdroid.paypalincl.ui.theme.PaypalINCLTheme

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imgUrl: String?,
    onClicked: () -> Unit,
    updatable: Boolean = false,
    onImageSelected: (Uri) -> Unit = {}
) {
    val context = LocalContext.current
    var imageState by remember { mutableStateOf(imgUrl) }

//    var localToggleState by remember { mutableStateOf(FullScreenState.Inactive) }
//    if (localToggleState == FullScreenState.Active) {
//        LocalFullScreenComposableReference
//            .current
//            .composableReference = {
//            ViewImageScreen(url = imageState) {
//                localToggleState = FullScreenState.Inactive
//            }
//        }
//
//        LocalMutableFullScreenState.current.state.value = localToggleState
//    }

    Box(
        modifier = modifier
            .clickable {
                onClicked.invoke()
//                localToggleState = FullScreenState.Active
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageState.isNullOrEmpty()) {
            Image(
                imageVector = Icons.Filled.Person,
                contentDescription = "",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .fillMaxHeight()
//                    .size(128.dp)
                    .clip(CircleShape)                       // clip to the circle shape
                    .border(2.dp, Color.Gray, CircleShape)   // add a border (optional)
            )
        } else {
            CoilImage( // GlideImage, FrescoImage
                imageModel = { imageState },
                modifier = Modifier
//                    .size(128.dp)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape),
                component = rememberImageComponent {
                    // shows a shimmering effect when loading an image.
                    +ShimmerPlugin(
                        baseColor = Color(0xFF424242),
                        highlightColor = Color(0xA3C2C2C2)
                    )
                    +PlaceholderPlugin.Failure(source = Icons.Filled.ImageNotSupported)
                },
                // shows an error text message when request failed.
                failure = {
                    Text(text = "image request failed.")
                }
            )
        }

        if (updatable) {
            SelectImageButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                context = context,
                onImageSelected = {
                    imageState = it.toString()
                    onImageSelected(it)
                }
            )
        }
    }
}

@Composable
fun DisplayImage(
    modifier: Modifier = Modifier,
//    context: Context,
    shape: Shape = RoundedCornerShape(12.dp),
    imgUrl: String?,
    aspectRatio: Float = 1f,
    onPaletteExported: (Palette) -> Unit,
    onImageClicked: () -> String?,
) {
    var imageState by remember { mutableStateOf(imgUrl) }
    var originalState by remember { mutableStateOf<String?>(null) }

    var localToggleState by remember { mutableStateOf(FullScreenState.Inactive) }
    if (localToggleState == FullScreenState.Active) {
        LocalFullScreenComposableReference
            .current
            .composableReference = {
            ViewImageScreen(url = originalState) {
                localToggleState = FullScreenState.Inactive
            }
        }

        LocalMutableFullScreenState.current.state.value = localToggleState
    }

    Box(
        modifier = modifier
            .clickable {
                originalState = onImageClicked.invoke()
                localToggleState = FullScreenState.Active
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageState.isNullOrEmpty()) {
            Image(
                imageVector = Icons.Filled.ImageNotSupported,
                contentDescription = "",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(aspectRatio)
                    //                    .size(128.dp)
                    .clip(shape)                       // clip to the circle shape
//                        .border(2.dp, Color.Gray, CircleShape)   // add a border (optional)
            )
        } else {
            CoilImage( // GlideImage, FrescoImage
                imageModel = { imageState },
                modifier = Modifier
                    //                    .size(128.dp)
                    .fillMaxHeight()
                    .aspectRatio(aspectRatio)
                    .clip(shape)
//                        .border(2.dp, Color.Gray, CircleShape)
                ,
                component = rememberImageComponent {
                    // shows a shimmering effect when loading an image.
                    +ShimmerPlugin(
                        baseColor = Color(0xFF424242),
                        highlightColor = Color(0xA3C2C2C2)
                    )
                    +PlaceholderPlugin.Failure(source = Icons.Filled.ImageNotSupported)
                    +PalettePlugin {
//                        palette = it
                        onPaletteExported.invoke(it)
                    }
                },
                // shows an error text message when request failed.
                failure = {
                    Text(text = "image request failed.")
                }
            )
        }

        //        if (updatable) {
        //            SelectImageButton(
        //                modifier = Modifier
        //                    .align(Alignment.BottomCenter),
        //                context = context,
        //                onImageSelected = {
        //                    imageState = it.toString()
        //                    onImageSelected(it)
        //                }
        //            )
        //        }
    }

}

@Composable
fun ViewImageScreen(url: String?, exitFullScreen: () -> Unit) {
//    var primaryColor by remember { mutableStateOf(Color.Black) }
    FullScreen(innerBoxSizeFraction = 1f) {
        val fullScreenStateRetrieve = LocalMutableFullScreenState.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CoilImage(
                imageModel = { url },
                imageOptions = ImageOptions(
                    alignment = Alignment.Center,
                    contentDescription = "",
                ),
                modifier = Modifier
//                .aspectRatio(9f / 16f)
//                    .fillMaxSize()
                    .align(Alignment.Center),
                component = rememberImageComponent {
                    +ShimmerPlugin(
                        baseColor = Color(0xFF424242),
                        highlightColor = Color(0xA3C2C2C2)
                    )
                    +PlaceholderPlugin.Failure(Icons.Filled.ImageNotSupported)
//                    +PalettePlugin {
//                        primaryColor = it.dominantSwatch?.rgb
//                    }
                },
                failure = {
                    androidx.compose.material.Text(text = "image request failed.")
                }
            )

            FloatingActionButton(
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.White,
//                    contentColor = Color.Black
//                ),
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 4.dp)
//                    .clip(RoundedCornerShape(0.dp))
                ,
                onClick = {
                    fullScreenStateRetrieve.state.value = FullScreenState.Inactive
                    exitFullScreen.invoke()
                }
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
            }
        }
    }
}

@Preview
@Composable
fun Preview_ProfileImage() {
    PaypalINCLTheme {
        ProfileImage(imgUrl = null,
            onClicked = {})
    }
}

@Preview
@Composable
fun Preview_DisplayImage() {
    PaypalINCLTheme {
        DisplayImage(
            imgUrl = null,
            onPaletteExported = {},
            onImageClicked = {
                ""
            },
        )
    }
}