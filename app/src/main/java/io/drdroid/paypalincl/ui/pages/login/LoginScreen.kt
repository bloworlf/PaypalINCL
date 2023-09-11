package io.drdroid.paypalincl.ui.pages.login

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import io.drdroid.paypalincl.R
import io.drdroid.paypalincl.data.network.response.Response
import io.drdroid.paypalincl.data.network.response.ResponseStatus
import io.drdroid.paypalincl.data.vm.UserViewModel
import io.drdroid.paypalincl.ui.components.ButtonComponent
import io.drdroid.paypalincl.ui.components.EmailFieldComponent
import io.drdroid.paypalincl.ui.components.GoogleButton
import io.drdroid.paypalincl.ui.components.PasswordFieldComponent
import io.drdroid.paypalincl.ui.components.TextFieldComponent
import io.drdroid.paypalincl.ui.navigation.AppNavigationActions
import io.drdroid.paypalincl.ui.theme.PrimaryDark
import io.drdroid.paypalincl.ui.theme.SecondaryDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LoginRegisterScreen(
    navAction: AppNavigationActions
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryDark),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
        ) {
            val tabData = listOf(
                stringResource(id = R.string.login),
                stringResource(id = R.string.register)
            )
            val pagerState = rememberPagerState(
                pageCount = tabData.size,
//                initialPage = 0
            )
            TabLayout(tabData, pagerState)
            TabContent(navAction, pagerState)
        }
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(tabData: List<String>, pagerState: PagerState) {

    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = SecondaryDark,
        divider = {
            Spacer(modifier = Modifier.height(5.dp))
        },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                    .padding(start = 48.dp, end = 48.dp),
                height = 2.dp,
                color = Color.Black
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        tabData.forEachIndexed { index, s ->
            Tab(selected = pagerState.currentPage == index, onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
//                icon = {
//                    Icon(imageVector = s.second, contentDescription = null)
//                },
                text = {
                    Text(
                        text = s,
                        style = MaterialTheme.typography.titleMedium
                    )
                })
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabContent(navAction: AppNavigationActions, pagerState: PagerState) {
//    var loginSize by remember { mutableStateOf(Size.Zero) }
//    var registerSize by remember { mutableStateOf(Size.Zero) }
//    val scope = rememberCoroutineScope()

//    var loginSize = Size.Zero
//    var registerSize = Size.Zero

//    var selectedPage by remember { mutableIntStateOf(pagerState.currentPage) }
//    LaunchedEffect(key1 = Unit, block = {
//        scope.launch {
//            pagerState.scrollToPage(0)
//        }
//    })
//    println("Current Page: ${pagerState.currentPage}")
//    println("Target Page: ${pagerState.targetPage}")

    HorizontalPager(
        state = pagerState,
//        modifier = if ((pagerState.targetPage != 0 && pagerState.targetPage != 1) || loginSize.height == 0f) {
//            Modifier
//        } else {
//            if (pagerState.targetPage == 0 || pagerState.currentPage == 0) {
//                Modifier
//                    .height(with(LocalDensity.current) { loginSize.height.toDp() })
//            } else {
//                Modifier
//                    .height(with(LocalDensity.current) { registerSize.height.toDp() })
//            }
//        }
//        modifier = if (pagerState.targetPage == null) {
//            if (loginSize.height == 0f) {
//                Modifier
//            } else {
//                if (pagerState.currentPage == 0) {
//                    Modifier
//                        .height(with(LocalDensity.current) { loginSize.height.toDp() })
//                } else if (pagerState.currentPage == 1) {
//                    Modifier
//                        .height(with(LocalDensity.current) { registerSize.height.toDp() })
//                } else {
//                    Modifier
//                }
//            }
//        } else {
//            if (pagerState.targetPage == 0) {
//                Modifier
//                    .height(with(LocalDensity.current) { loginSize.height.toDp() })
//            } else if (pagerState.targetPage == 1) {
//                Modifier
//                    .height(with(LocalDensity.current) { registerSize.height.toDp() })
//            } else {
//                Modifier
//            }
//        }
        modifier = Modifier
            .background(color = SecondaryDark)
            .animateContentSize(animationSpec = tween(200))
            .wrapContentHeight()
    ) { index ->
        when (index) {
            0 -> {
                LoginScreen(
                    navAction = navAction
//                    animateHeight = {
//                        loginSize = it
//                    }
                )
            }

            1 -> {
                SignUpScreen(
                    navAction = navAction
//                    animateHeight = {
//                        registerSize = it
//                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    navAction: AppNavigationActions,
//    animateHeight: (Size) -> Unit,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
//    var columnSize by remember {
//        mutableStateOf(Size.Zero)
//    }
//    LaunchedEffect(key1 = columnSize, block = {
//        if (columnSize.height > 0f) {
//            println(columnSize.height)
//            animateHeight.invoke(columnSize)
//        }
//    })

    var inputEmail by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    handleGoogleSignInResult(
                        task = task,
                        userViewModel = userViewModel,
                        onResponse = {
                            if (it.status == ResponseStatus.SUCCESS) {
                                navAction.navigateTo("home")
                            } else {
                                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }

    Column(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
//                columnSize = coordinates.size.toSize()
//                animateHeight.invoke(coordinates.size.toSize())
            }
//            .clip(shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
            .background(color = SecondaryDark)
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            labelValue = stringResource(id = R.string.email),
            textValue = inputEmail,
            onValueChanged = {
                inputEmail = it
            }
        )

        PasswordFieldComponent(
            labelValue = stringResource(id = R.string.password),
            password = inputPassword,
            onValueChanged = {
                inputPassword = it
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        ButtonComponent(
            text = stringResource(id = R.string.login),
            onClick = {
                userViewModel.loginEmailPassword(
                    email = inputEmail,
                    password = inputPassword,
                    onLogin = {
                        if (it.status == ResponseStatus.SUCCESS) {
                            navAction.navigateTo("home")
                        } else {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "OR")

        Spacer(modifier = Modifier.height(8.dp))

        GoogleButton(onClick = {
            startForResult.launch(getGoogleLoginAuth(context).signInIntent)
        })
//            Spacer(modifier = Modifier.height(8.dp))
//            FaceBookButton(onClick = {}, onResult = {})
    }
}

private fun getGoogleLoginAuth(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(context.getString(R.string.gcp_id))
//            .requestId()
//            .requestProfile()
        .build()
    return GoogleSignIn.getClient(context, gso)
}

fun handleGoogleSignInResult(
    task: Task<GoogleSignInAccount>,
    userViewModel: UserViewModel,
    onResponse: (Response<FirebaseUser>) -> Unit
) {
    try {
        val account = task.getResult(ApiException::class.java)
        println(account.idToken)
//        firebaseAuthWithGoogle(account?.idToken)
        userViewModel.googleLogin(
            token = account?.idToken,
            onLogin = onResponse
        )
    } catch (e: ApiException) {
//        Toast.makeText(this, "Sign in failed:\n${e.message}", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun SignUpScreen(
    navAction: AppNavigationActions,
//    animateHeight: (Size) -> Unit,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
//    var columnSize by remember {
//        mutableStateOf(Size.Zero)
//    }
//    LaunchedEffect(key1 = columnSize, block = {
//        if (columnSize.height > 0f) {
//            println(columnSize.height)
//            animateHeight.invoke(columnSize)
//        }
//    })

    var inputFirstName by remember { mutableStateOf("") }
    var inputLastName by remember { mutableStateOf("") }
    var inputEmail by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var inputPasswordConfirm by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
//                columnSize = coordinates.size.toSize()
//                animateHeight.invoke(coordinates.size.toSize())
            }
            .background(color = SecondaryDark)
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            labelValue = stringResource(R.string.first_name),
            textValue = inputFirstName,
            onValueChanged = {
                inputFirstName = it
            }
        )
        TextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            labelValue = stringResource(R.string.last_name),
            textValue = inputLastName,
            onValueChanged = {
                inputLastName = it
            }
        )
        EmailFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            labelValue = stringResource(R.string.email),
            textValue = inputEmail,
            onValueChanged = {
                inputEmail = it
            }
        )

        PasswordFieldComponent(
            labelValue = stringResource(R.string.password),
            password = inputPassword,
            onValueChanged = {
                inputPassword = it
            }
        )

        PasswordFieldComponent(
            labelValue = stringResource(R.string.confirm_password),
            password = inputPasswordConfirm,
            onValueChanged = {
                inputPasswordConfirm = it
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        ButtonComponent(
            text = "Register",
            onClick = {
                if (inputPassword != inputPasswordConfirm) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.passwords_don_t_match), Toast.LENGTH_SHORT
                    ).show()
                    return@ButtonComponent
                }
                userViewModel.createAccount(
                    email = inputEmail,
                    password = inputPassword,
                    firstName = inputFirstName,
                    lastName = inputLastName,
                    onLogin = {
                        if (it.status == ResponseStatus.SUCCESS) {
                            navAction.navigateTo("home")
                        } else {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        )
    }
}

//fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>, repository: Repository) {
//    try {
//        val account = task.getResult(ApiException::class.java)
//        CoroutineScope(Dispatchers.Main).launch {
//            repository.googleLogin(account?.idToken)?.let {
//                AppRouter.navigateTo(Screen.Home)
//            }
//        }
//    } catch (e: ApiException) {
//    }
//}

//@Preview
//@Composable
//fun Preview_LoginScreen() {
//    PaypalINCLTheme {
//        LoginRegisterScreen()
//    }
//}