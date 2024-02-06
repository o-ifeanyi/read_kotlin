package android.ifeanyi.read.app.presentation.views.home

import android.annotation.SuppressLint
import android.ifeanyi.read.app.data.models.LibraryModel
import android.ifeanyi.read.app.data.models.LibraryType
import android.ifeanyi.read.app.presentation.components.TextFieldComponent
import android.ifeanyi.read.app.presentation.components.TileButtonComponent
import android.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import android.ifeanyi.read.core.services.SpeechService
import android.ifeanyi.read.core.util.getName
import android.ifeanyi.read.core.util.trimUrl
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(libraryViewModel: LibraryViewModel) {
    val context = LocalContext.current

    fun handleUriToModel(uri: Uri, type: LibraryType) {
        if (uri.path == null) return
        val input = context.contentResolver.openInputStream(uri) ?: return
        val outputFile = context.filesDir.resolve(uri.getName(context))
        input.copyTo(outputFile.outputStream())
        input.close()
        val newUri = outputFile.toUri()
        val model = LibraryModel(
            name = outputFile.name,
            type = type,
            path = newUri.toString(),
            progress = 0,
            parent = ""
        )
        SpeechService.updateModel(context, model)
        libraryViewModel.insertItem(model)
    }

    val docLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        it?.let { uri ->
            handleUriToModel(uri, LibraryType.Pdf)
        }
    }

    val imageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri ->
                handleUriToModel(uri, LibraryType.Image)
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
                start = 15.dp, end = 15.dp,
                bottom = 200.dp
            ),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                TileButtonComponent(
                    asset = {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.InsertDriveFile, contentDescription = "",modifier = Modifier
                            .size(50.dp))
                    },
                    title = "Pick document",
                    subtitle = "SizeTransform defines how the"
                ) {
                    reset()
                    docLauncher.launch(arrayOf("application/pdf"))
                }
            }

            item {
                TileButtonComponent(
                    asset = {
                        Icon(imageVector = Icons.Rounded.Image, contentDescription = "",modifier = Modifier
                            .size(50.dp))
                    },
                    title = "Pick image",
                    subtitle = "SizeTransform defines how the"
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
                    asset = {
                        Icon(imageVector = Icons.Rounded.Link, contentDescription = "",modifier = Modifier
                            .size(50.dp))
                    },
                    title = "Paste web link",
                    subtitle = "SizeTransform defines how the"
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
                            val model = LibraryModel(
                                name = url.value.trimUrl,
                                type = LibraryType.Url,
                                path = url.value,
                                progress = 0,
                                parent = ""
                            )
                            SpeechService.updateModel(context, model)
                            libraryViewModel.insertItem(model)
                        }
                    )
                }
            }

        }
    }
}