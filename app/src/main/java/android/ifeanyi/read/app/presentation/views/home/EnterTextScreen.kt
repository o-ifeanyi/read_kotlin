package android.ifeanyi.read.app.presentation.views.home

import android.annotation.SuppressLint
import android.ifeanyi.read.app.data.models.FileModel
import android.ifeanyi.read.app.data.models.LibraryType
import android.ifeanyi.read.app.presentation.components.AppButton
import android.ifeanyi.read.app.presentation.components.TextFieldComponent
import android.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import android.ifeanyi.read.core.services.SpeechService
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import java.io.File
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EnterTextScreen(
    controller: NavHostController,
    libraryVM: LibraryViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val textLimit = 4000
    val text = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Enter Text", style = MaterialTheme.typography.titleLarge) },
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                start = 20.dp, end = 20.dp,
                bottom = 200.dp
            ),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                TextFieldComponent(
                    value = text,
                    label = { Text("Paste or write something...") },
                    modifier = Modifier.height((config.screenHeightDp * 0.5).dp),
                    maxLines = 30,
                    textLimit = textLimit,
                    supportingText = {
                        Text(
                            text = "${text.value.length}/$textLimit",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                )
            }
            item {
                AppButton(text = "Continue", enabled = text.value.isNotEmpty()) {
                    val path = "${context.filesDir.path}/${Instant.now()}"
                    println(path)
                    val outputFile = File(path)
                    outputFile.writeText(text.value, Charsets.UTF_8)

                    val newUri = outputFile.toUri()
                    val model = FileModel(
                        name = outputFile.name,
                        type = LibraryType.Txt,
                        path = newUri.toString(),
                    )
                    SpeechService.updateModel(model)
                    libraryVM.insertItem(model)

                    controller.popBackStack()
                }
            }
        }
    }
}