package com.ifeanyi.read.app.presentation.views.setting

import com.ifeanyi.read.app.presentation.components.SettingsItem
import com.ifeanyi.read.core.theme.AppIcons
import com.ifeanyi.read.core.util.Constants
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ifeanyi.read.core.util.appVersion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppScreen() {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "About App") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                start = 20.dp, end = 20.dp,
                bottom = 200.dp
            ),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                Surface(tonalElevation = 1.dp, shape = MaterialTheme.shapes.small) {
                    Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                        SettingsItem(
                            title = "Privacy Policy",
                            icon = AppIcons.shield,
                            color = Color(0XFFBF5AF2)
                        ) {
                            uriHandler.openUri(uri = Constants.privacyLink)
                        }
                        SettingsItem(
                            title = "Terms of Service",
                            icon = AppIcons.doc,
                            color = Color(0xFF63D2FF)
                        ) {
                            uriHandler.openUri(uri = Constants.termsLink)
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "VER $appVersion",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}