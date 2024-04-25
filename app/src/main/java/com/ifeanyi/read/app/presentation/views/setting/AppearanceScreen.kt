package com.ifeanyi.read.app.presentation.views.setting

import androidx.compose.foundation.Image
import com.ifeanyi.read.app.presentation.components.SettingsItem
import com.ifeanyi.read.app.presentation.viewmodel.SettingsViewModel
import com.ifeanyi.read.core.enums.AppTheme
import com.ifeanyi.read.core.enums.DisplayStyle
import com.ifeanyi.read.core.theme.AppIcons
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ifeanyi.read.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AppearanceScreen(
    settingsVM: SettingsViewModel = hiltViewModel(),
    onIconChangeRed: () -> Unit,
    onIconChangePurple: () -> Unit,
    onIconChangeWhite: () -> Unit
) {
    val state = settingsVM.state.collectAsState().value
    val config = LocalConfiguration.current

    val showTheme = remember { mutableStateOf(false) }
    val showDisplay = remember { mutableStateOf(false) }

    val iconsMap = mapOf(
        R.drawable.red_logo to onIconChangeRed,
        R.drawable.white_logo to onIconChangeWhite,
        R.drawable.purple_logo to onIconChangePurple
    )


    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Appearance") })
        }
    ) { padding ->
        if (showTheme.value) {
            ThemeDialog(padding = padding, showTheme = showTheme, settingsVM = settingsVM)
        }
        if (showDisplay.value) {
            DisplayDialog(padding = padding, showDisplay = showDisplay, settingsVM = settingsVM)
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
                Surface(tonalElevation = 1.dp, shape = MaterialTheme.shapes.small) {
                    Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                        SettingsItem(
                            title = "Theme Mode",
                            icon = when (state.theme) {
                                AppTheme.System -> AppIcons.theme
                                AppTheme.Dark -> AppIcons.dark
                                AppTheme.Light -> AppIcons.light
                            },
                            color = Color(0XFFBF5AF2),
                            trailing = { Text(text = state.theme.name) }
                        ) {
                            showTheme.value = true
                        }
                        SettingsItem(
                            title = "Display Style",
                            icon = when (state.displayStyle) {
                                DisplayStyle.Grid -> AppIcons.gridView
                                DisplayStyle.List -> AppIcons.listView
                            },
                            color = Color(0xFFFF365F),
                            trailing = { Text(text = state.displayStyle.name) }
                        ) {
                            showDisplay.value = true
                        }
                    }
                }
            }
            item {
                Text(text = "Change Icon", fontWeight = FontWeight.SemiBold)
            }
            item {
                Surface(tonalElevation = 1.dp, shape = MaterialTheme.shapes.small) {
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                        maxItemsInEachRow = 3,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        iconsMap.map { icon ->
                            Button(
                                onClick = icon.value,
                                modifier = Modifier
                                    .size(((config.screenWidthDp - 91) / 3).dp),
                                shape = MaterialTheme.shapes.small,
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = icon.key),
                                    contentDescription = "App Icon $icon",
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

