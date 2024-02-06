package android.ifeanyi.read.core.route

import android.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import android.ifeanyi.read.app.presentation.views.home.HomeScreen
import android.ifeanyi.read.app.presentation.views.home.HomeTwoScreen
import android.ifeanyi.read.app.presentation.views.library.LibraryScreen
import android.ifeanyi.read.app.presentation.views.settings.SettingsScreen
import android.ifeanyi.read.app.presentation.views.settings.SettingsTwoScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Router(controller: NavHostController) {
    val libraryViewModel: LibraryViewModel = hiltViewModel()
    NavHost(navController = controller, startDestination = Routes.HomeScreen.name) {
        composable(Routes.HomeScreen.name) {
            HomeScreen(libraryViewModel)
        }
        composable(Routes.HomeTwoScreen.name) {
            HomeTwoScreen(controller = controller)
        }
        composable(Routes.LibraryScreen.name) {
            LibraryScreen(libraryViewModel)
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
        Routes.SettingsScreen -> Routes.SettingsScreen
        Routes.SettingsTwoScreen -> Routes.SettingsScreen
    }
    return parentRoute
}
