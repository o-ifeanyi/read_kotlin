package com.ifeanyi.read.core.route

import com.ifeanyi.read.app.presentation.views.home.HomeScreen
import com.ifeanyi.read.app.presentation.views.home.EnterTextScreen
import com.ifeanyi.read.app.presentation.views.library.FolderScreen
import com.ifeanyi.read.app.presentation.views.library.LibraryScreen
import com.ifeanyi.read.app.presentation.views.setting.AboutAppScreen
import com.ifeanyi.read.app.presentation.views.setting.AppearanceScreen
import com.ifeanyi.read.app.presentation.views.setting.SettingsScreen
import com.ifeanyi.read.app.presentation.views.setting.TextToSpeechScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Router(controller: NavHostController) {

    NavHost(navController = controller, startDestination = Routes.HomeScreen.name) {
        composable(Routes.HomeScreen.name) {
            HomeScreen(controller = controller)
        }
        composable(Routes.EnterTextScreen.name) {
            EnterTextScreen(controller = controller)
        }
        composable(Routes.LibraryScreen.name) {
            LibraryScreen(controller = controller)
        }
        val folderRoute = "${Routes.FolderScreen.name}/{id}/{name}"
        composable(folderRoute) {
            val id = UUID.fromString(it.arguments?.getString("id"))
            val name = it.arguments?.getString("name")
            FolderScreen(id = id, name = name ?: "Folder")
        }
        composable(Routes.SettingsScreen.name) {
            SettingsScreen(controller = controller)
        }
        composable(Routes.AboutAppScreen.name) {
            AboutAppScreen()
        }
        composable(Routes.TextToSpeechScreen.name) {
            TextToSpeechScreen()
        }
        composable(Routes.AppearanceScreen.name) {
            AppearanceScreen()
        }
    }
}

val NavHostController.parentRoute: Routes
    get() = this.currentBackStackEntry.let {
        val route = it?.destination?.route?.split("/")?.first()?: Routes.HomeScreen.name

        val parentRoute = when (Routes.valueOf(route)) {
            Routes.HomeScreen -> Routes.HomeScreen
            Routes.EnterTextScreen -> Routes.HomeScreen
            Routes.LibraryScreen -> Routes.LibraryScreen
            Routes.FolderScreen -> Routes.LibraryScreen
            Routes.SettingsScreen -> Routes.SettingsScreen
            Routes.AboutAppScreen -> Routes.SettingsScreen
            Routes.TextToSpeechScreen -> Routes.SettingsScreen
            Routes.AppearanceScreen -> Routes.SettingsScreen
        }
        return parentRoute
    }
