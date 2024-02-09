package android.ifeanyi.read.app.presentation.views.settings

import android.annotation.SuppressLint
import android.ifeanyi.read.app.presentation.components.SettingsItem
import android.ifeanyi.read.core.theme.AppIcons
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(controller: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Settings") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                start = 15.dp, end = 15.dp,
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
                        )
                        SettingsItem(
                            title = "Speaking Voice",
                            icon = AppIcons.speaker,
                            color = Color(0xFFFF365F)
                        )
                        SettingsItem(
                            title = "Speech Rate",
                            icon = AppIcons.speechRate,
                            color = Color(0xFFFF9E08)
                        )
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
                            title = "Leave a Review",
                            icon = AppIcons.star,
                            color = Color(0xFF0F85FF)
                        )
                        SettingsItem(
                            title = "Contact Support",
                            icon = AppIcons.question,
                            color = Color(0xFF30D157)
                        )
                        SettingsItem(
                            title = "Share This App",
                            icon = AppIcons.share,
                            color = Color(0xFF63E6E2)
                        )
                    }
                }
            }
            item {
                Text(text = "Legal", fontWeight = FontWeight.SemiBold)
            }
            item {
                Surface(tonalElevation = 1.dp, shape = MaterialTheme.shapes.small) {
                    Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                        SettingsItem(
                            title = "Privacy Policy",
                            icon = AppIcons.shield,
                            color = Color(0XFFBF5AF2)
                        )
                        SettingsItem(
                            title = "Terms of Service",
                            icon = AppIcons.doc,
                            color = Color(0xFF63D2FF)
                        )
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
                        )
                        SettingsItem(
                            title = "About This App",
                            icon = AppIcons.about,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}