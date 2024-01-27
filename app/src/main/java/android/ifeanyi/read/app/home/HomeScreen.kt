package android.ifeanyi.read.app.home

import android.annotation.SuppressLint
import android.ifeanyi.read.app.common.components.TextFieldComponent
import android.ifeanyi.read.app.common.components.TileButtonComponent
import android.ifeanyi.read.core.services.SpeechService
import android.ifeanyi.read.core.util.TextParser
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    val context = LocalContext.current

    val docLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        it?.let { uri ->
            TextParser.parsePdf(context, uri) { text ->
                SpeechService.updateText(text)
            }
        }
    }

    val imageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri ->
                TextParser.parseImage(context, uri) { text ->
                    SpeechService.updateText(text)
                }
            }

        }

    val url = remember { mutableStateOf("") }
    val showTextField = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    fun reset() {
        showTextField.value = false
        focusManager.clearFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Home") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                start = 15.dp,
                end = 15.dp,
                bottom = 200.dp
            ),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                TileButtonComponent(
                    title = "Pick document",
                    subtitle = "SizeTransform defines how the size should animate between the.\""
                ) {
                    reset()
                    docLauncher.launch(arrayOf("application/pdf"))
                }
            }

            item {
                TileButtonComponent(
                    title = "Pick image",
                    subtitle = "SizeTransform defines how the size should animate between the.\""
                ) {
                    reset()
                    imageLauncher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo
                        )
                    )
                }
            }

            item {
                TileButtonComponent(
                    title = "Paste web link",
                    subtitle = "SizeTransform defines how the size should animate between the.\""
                ) {
                    reset()
                    showTextField.value = true
                }
            }

            item {
                if (showTextField.value) {
                    TextFieldComponent(
                        value = url,
                        label = { Text("Web link") },
                        onImeAction = {
                            TextParser.parseUrl(url.value) { text ->
                                SpeechService.updateText(text)
                                showTextField.value = false
                            }
                        }
                    )
                }
            }

        }
    }
}