package android.ifeanyi.read.app.presentation.views.settings

import android.annotation.SuppressLint
import android.ifeanyi.read.app.presentation.components.SettingsItem
import android.ifeanyi.read.core.route.Routes
import android.ifeanyi.read.core.services.AnalyticService
import android.ifeanyi.read.core.theme.AppIcons
import android.ifeanyi.read.core.util.appVersion
import android.ifeanyi.read.core.util.mailTo
import android.ifeanyi.read.core.util.share
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(controller: NavHostController) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    val showWhatsNewSheet = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        AnalyticService.track("view_settings")
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Settings") })
        }
    ) { padding ->
        if (showWhatsNewSheet.value) {
            WhatsNewSheet(showWhatsNewSheet = showWhatsNewSheet)
        }
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                start = 20.dp, end = 20.dp,
                bottom = 200.dp
            ),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                Text(text = "General", fontWeight = FontWeight.SemiBold)
            }
            item {
                Surface(tonalElevation = 1.dp, shape = MaterialTheme.shapes.small) {
                    Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                        SettingsItem(
                            title = "Appearance",
                            icon = AppIcons.theme,
                            color = Color(0xFF0984FF)
                        ) {
                            controller.navigate(Routes.AppearanceScreen.name)
                        }
                        SettingsItem(
                            title = "Text To Speech",
                            icon = AppIcons.waveform,
                            color = Color(0xFFFF365F)
                        ) {
                            controller.navigate(Routes.TextToSpeechScreen.name)
                        }
                    }
                }
            }
            item {
                Text(text = "Support", fontWeight = FontWeight.SemiBold)
            }
            item {
                Surface(tonalElevation = 1.dp, shape = MaterialTheme.shapes.small) {
                    Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                        SettingsItem(
                            title = "Leave A Review",
                            icon = AppIcons.star,
                            color = Color(0xFF0F85FF)
                        ) {
                            uriHandler.openUri(uri = "https://stackoverflow.com/")
                        }
                        SettingsItem(
                            title = "Contact Support",
                            icon = AppIcons.question,
                            color = Color(0xFF30D157)
                        ) {
                            context.mailTo(to = "ifeanyi@gmail.com", subject = "Hey Support")
                        }
                        SettingsItem(
                            title = "Share App",
                            icon = AppIcons.share,
                            color = Color(0xFFFF9E08)
                        ) {
                            context.share("https://stackoverflow.com/")
                        }
                    }
                }
            }
            item {
                Text(text = "About", fontWeight = FontWeight.SemiBold)
            }
            item {
                Surface(tonalElevation = 1.dp, shape = MaterialTheme.shapes.small) {
                    Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                        SettingsItem(
                            title = "What's New",
                            icon = AppIcons.newRelease,
                            color = Color(0xFFFF453A)
                        ) {
                            showWhatsNewSheet.value = true
                        }
                        SettingsItem(
                            title = "About App",
                            icon = AppIcons.about,
                            color = Color.Black,
                        ) {
                            controller.navigate(Routes.AboutAppScreen.name)
                        }
                    }
                }
            }
            item {
                if (context.appVersion != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "VER ${context.appVersion}", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}