package android.ifeanyi.read.app.presentation.views.settings

import android.annotation.SuppressLint
import android.ifeanyi.read.core.route.Routes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
                top = padding.calculateTopPadding()
            )
        ) {
            item {
                ElevatedButton(onClick = { controller.navigate(Routes.SettingsTwoScreen.name) }) {
                    Text(text = "Go to settings two")
                }
            }
        }
    }
}