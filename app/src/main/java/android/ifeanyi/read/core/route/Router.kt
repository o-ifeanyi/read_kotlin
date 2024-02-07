package android.ifeanyi.read.core.route

import android.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import android.ifeanyi.read.app.presentation.views.home.HomeScreen
import android.ifeanyi.read.app.presentation.views.home.HomeTwoScreen
import android.ifeanyi.read.app.presentation.views.library.FolderScreen
import android.ifeanyi.read.app.presentation.views.library.LibraryScreen
import android.ifeanyi.read.app.presentation.views.settings.SettingsScreen
import android.ifeanyi.read.app.presentation.views.settings.SettingsTwoScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import java.util.UUID

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
            LibraryScreen(controller = controller, libraryViewModel = libraryViewModel)
        }
        val folderRoute = "${Routes.FolderScreen.name}/{id}/{name}"
        composable(folderRoute) {
            val id = UUID.fromString(it.arguments?.getString("id"))
            val name = it.arguments?.getString("name")
            FolderScreen(id = id, name = name ?: "Folder", libraryViewModel = libraryViewModel)
        }
        composable(Routes.SettingsScreen.name) {
            SettingsScreen(controller = controller)
        }
        composable(Routes.SettingsTwoScreen.name) {
            SettingsTwoScreen()
        }
    }
}

val NavHostController.parentRoute: Routes
    get() = this.currentBackStackEntry.let {
        val route = it?.destination?.route?.split("/")?.first()?: Routes.HomeScreen.name

        val parentRoute = when (Routes.valueOf(route)) {
            Routes.HomeScreen -> Routes.HomeScreen
            Routes.HomeTwoScreen -> Routes.HomeScreen
            Routes.LibraryScreen -> Routes.LibraryScreen
            Routes.FolderScreen -> Routes.LibraryScreen
            Routes.SettingsScreen -> Routes.SettingsScreen
            Routes.SettingsTwoScreen -> Routes.SettingsScreen
        }
        return parentRoute
    }
