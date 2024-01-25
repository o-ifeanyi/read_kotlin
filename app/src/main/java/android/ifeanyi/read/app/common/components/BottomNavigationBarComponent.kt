package android.ifeanyi.read.app.common.components

import android.ifeanyi.read.core.route.bottomNavItems
import android.ifeanyi.read.core.route.parentRoute
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBarComponent(controller: NavHostController, currentDestination: NavDestination?) {
    NavigationBar {

        val parentRoute = remember(controller.currentBackStackEntry) {
            derivedStateOf {
                controller.parentRoute
            }
        }

        bottomNavItems.forEach { screen ->
            val selected = parentRoute.value.name == screen.route
            NavigationBarItem(
                selected = selected,
                icon = if (selected) screen.icon else screen.inactiveIcon,
                label = { Text(text = screen.label) },
                onClick = {
                    if (selected && currentDestination?.route != parentRoute.value.name) {
                        controller.popBackStack()
                    }else {
                        controller.navigate(screen.route) {
                            popUpTo(controller.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.inversePrimary
                )
            )
        }
    }
}