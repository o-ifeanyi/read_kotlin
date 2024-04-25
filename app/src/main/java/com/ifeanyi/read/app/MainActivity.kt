package com.ifeanyi.read.app

import com.ifeanyi.read.app.presentation.components.BottomNavigationBarComponent
import com.ifeanyi.read.app.presentation.components.PlayerComponent
import com.ifeanyi.read.app.presentation.views.speech.SpeechScreen
import com.ifeanyi.read.core.route.Router
import com.ifeanyi.read.core.services.AnalyticService
import com.ifeanyi.read.core.services.AppStateService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.ifeanyi.read.core.theme.ReadTheme
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ifeanyi.read.app.presentation.components.LoaderComponent
import com.ifeanyi.read.core.enums.ActivityType
import com.ifeanyi.read.core.services.notificationService
import com.ifeanyi.read.core.util.changeIcon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onDestroy() {
        notificationService.destroy()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AnalyticService.init(this)
        notificationService.init(this)



        setContent {
            ReadTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val controller = rememberNavController()
                    val navBackStackEntry = controller.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry.value?.destination

                    var expanded by remember { mutableStateOf(false) }

                    Router(
                        controller = controller,
                        onIconChangeRed = { this@MainActivity.changeIcon(ActivityType.MainActivity) },
                        onIconChangePurple = { this@MainActivity.changeIcon(ActivityType.MainActivityPurple) },
                        onIconChangeWhite = { this@MainActivity.changeIcon(ActivityType.MainActivityWhite) }
                    )

                    Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                        AnimatedContent(
                            targetState = expanded,
                            label = "Animated Player",
                            transitionSpec = {
                                slideInVertically(
                                    animationSpec = tween(300),
                                    initialOffsetY = { 0 }) togetherWith
                                        slideOutVertically(animationSpec = tween(300))
                            }
                        ) { isExpanded ->
                            if (isExpanded) {
                                SpeechScreen { expanded = false }
                            }
                        }

                        PlayerComponent(expanded = expanded) { expanded = true }

                        BottomNavigationBarComponent(controller, currentDestination)
                    }

                    val snackBar = AppStateService.snackBar.collectAsState().value

                    AnimatedVisibility(
                        modifier = Modifier.align(Alignment.TopCenter),
                        visible = snackBar.hasMessage,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Snackbar(modifier = Modifier.padding(15.dp)) {
                            Text(text = snackBar.message)
                        }
                    }

                    val loader = AppStateService.loader.collectAsState().value

                    AnimatedVisibility(
                        modifier = Modifier.align(Alignment.Center),
                        visible = loader.isLoading,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        LoaderComponent(text = loader.message)
                    }
                }
            }
        }
    }
}
