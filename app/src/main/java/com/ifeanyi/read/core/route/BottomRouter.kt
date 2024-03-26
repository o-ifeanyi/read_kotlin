package com.ifeanyi.read.core.route

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

sealed class BottomRouter(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit,
    val inactiveIcon: @Composable () -> Unit
) {

    data object Home : BottomRouter(
        route = Routes.HomeScreen.name,
        label = "Home",
        icon = {
            Icon(imageVector = Icons.Default.Home, contentDescription = "Home Tab")
        },
        inactiveIcon = {
            Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home Tab")
        }
    )

    data object Library : BottomRouter(
        route = Routes.LibraryScreen.name,
        label = "Library",
        icon = {
            Icon(imageVector = Icons.AutoMirrored.Filled.LibraryBooks, contentDescription = "Library Tab")
        },
        inactiveIcon = {
            Icon(imageVector = Icons.AutoMirrored.Outlined.LibraryBooks, contentDescription = "Library Tab")
        }
    )

    data object Settings : BottomRouter(
        route = Routes.SettingsScreen.name,
        label = "Settings",
        icon = {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings Tab")
        },
        inactiveIcon = {
            Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings Tab")
        }
    )
}

val bottomNavItems = listOf(
    BottomRouter.Home,
    BottomRouter.Library,
    BottomRouter.Settings
)