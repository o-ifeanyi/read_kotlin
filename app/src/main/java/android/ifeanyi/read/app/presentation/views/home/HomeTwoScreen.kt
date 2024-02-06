package android.ifeanyi.read.app.presentation.views.home

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeTwoScreen(controller: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Title", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { controller.popBackStack() }) {

                    }
                }
            )
        }
    ) {
        Text(text = "Home Two Screen")
    }
}