package android.ifeanyi.read.app

import android.ifeanyi.read.app.common.components.BottomNavigationBarComponent
import android.ifeanyi.read.app.common.components.PlayerComponent
import android.ifeanyi.read.core.route.Router
import android.ifeanyi.read.core.services.SnackBarService
import android.ifeanyi.read.core.services.SpeechService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import android.ifeanyi.read.core.theme.ReadTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReadTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val controller = rememberNavController()
                    val navBackStackEntry = controller.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry.value?.destination

                    val context = LocalContext.current
                    val speechState = SpeechService.state.collectAsState().value


                    Router(controller = controller)

                    Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                        AnimatedVisibility(
                            visible = speechState.canPlay,
                            enter = slideInVertically { it },
                            exit = slideOutVertically { it },
                        ) {
                            PlayerComponent(
                                isPlaying = speechState.isPlaying,
                                onPlay = {
                                    SpeechService.play(context)
                                }, onStop = {
                                    SpeechService.stop()
                                }
                            )
                        }
                        BottomNavigationBarComponent(controller, currentDestination)
                    }

                    val state = SnackBarService.state.collectAsState().value

                    AnimatedVisibility(
                        modifier = Modifier.align(Alignment.TopCenter),
                        visible = state.hasMessage,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Snackbar(modifier = Modifier.padding(15.dp)) {
                            Text(text = state.message)
                        }
                    }
                }
            }
        }
    }
}
