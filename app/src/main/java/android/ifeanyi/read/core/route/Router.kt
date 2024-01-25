package android.ifeanyi.read.core.route

import android.ifeanyi.read.app.home.HomeScreen
import android.ifeanyi.read.app.home.HomeTwoScreen
import android.ifeanyi.read.app.library.LibraryScreen
import android.ifeanyi.read.app.library.LibraryTwoScreen
import android.ifeanyi.read.app.settings.SettingsScreen
import android.ifeanyi.read.app.settings.SettingsTwoScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Router(controller: NavHostController) {
    NavHost(navController = controller, startDestination = Routes.HomeScreen.name) {
        composable(Routes.HomeScreen.name) {
            HomeScreen()
        }
        composable(Routes.HomeTwoScreen.name) {
            HomeTwoScreen(controller = controller)
        }
        composable(Routes.LibraryScreen.name) {
            LibraryScreen(controller = controller)
        }
        composable(Routes.LibraryTwoScreen.name) {
            LibraryTwoScreen()
        }
        composable(Routes.SettingsScreen.name) {
            SettingsScreen(controller = controller)
        }
        composable(Routes.SettingsTwoScreen.name) {
            SettingsTwoScreen()
        }
    }
}

val NavHostController.parentRoute: Routes get() = this.currentBackStackEntry.let {
    val parentRoute = when (Routes.valueOf(it?.destination?.route ?: Routes.HomeScreen.name)) {
        Routes.HomeScreen -> Routes.HomeScreen
        Routes.HomeTwoScreen -> Routes.HomeScreen
        Routes.LibraryScreen -> Routes.LibraryScreen
        Routes.LibraryTwoScreen -> Routes.LibraryScreen
        Routes.SettingsScreen -> Routes.SettingsScreen
        Routes.SettingsTwoScreen -> Routes.SettingsScreen
    }
    return parentRoute
}
