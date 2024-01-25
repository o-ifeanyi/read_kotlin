package android.ifeanyi.read.app.settings

import android.annotation.SuppressLint
import android.ifeanyi.read.core.services.SnackBarService
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsTwoScreen() {
    Scaffold {
        Column {
            Text(text = "Settings Two Screen")
            ElevatedButton(onClick = { SnackBarService.displayMessage("This is a sample message") }) {
                Text(text = "Show snack bar")
            }
        }
    }
}