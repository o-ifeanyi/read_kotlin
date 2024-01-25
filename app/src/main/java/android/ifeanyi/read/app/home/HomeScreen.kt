package android.ifeanyi.read.app.home

import android.annotation.SuppressLint
import android.ifeanyi.read.core.services.SpeechService
import android.ifeanyi.read.core.util.TextParser
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.rizzi.bouquet.HorizontalPDFReader
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.rememberHorizontalPdfReaderState


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val speechText = remember { mutableStateOf<String?>(null) }

    val pdf = remember { mutableStateOf<Uri?>(null) }
    val docLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        pdf.value = it
    }

    val image = remember { mutableStateOf<Uri?>(null) }
    val imageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            image.value = it
        }

    val url = remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun reset() {
        pdf.value = null
        image.value = null
        url.value = null
        speechText.value = null
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
            )
        ) {
            item {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                    ElevatedButton(onClick = {
                        reset()
                        docLauncher.launch(arrayOf("application/pdf"))
                    }) {
                        Text(text = "Pick document")
                    }
                    ElevatedButton(onClick = {
                        reset()
                        imageLauncher.launch(
                            PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo
                            )
                        )
                    }) {
                        Text(text = "Pick image")
                    }
                    ElevatedButton(onClick = {
                        reset()
                        url.value = ""
                    }) {
                        Text(text = "Enter url")
                    }
                }
            }
            item {
                pdf.value?.let { file ->
                    val pdfState = rememberHorizontalPdfReaderState(
                        resource = ResourceType.Local(file),
                        isZoomEnable = true
                    )

                    HorizontalPDFReader(
                        state = pdfState,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    ElevatedButton(onClick = {
                        TextParser.parsePdf(context, file, speechText)
                    }) {
                        Text(text = "Extract pdf text")
                    }
                }
            }

            item {
                image.value?.let { file ->
                    val painter = rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(data = file)
                            .build()
                    )
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )

                    ElevatedButton(onClick = {
                        TextParser.parseImage(context, file, speechText)
                    }) {
                        Text(text = "Extract image text")
                    }
                }
            }

            item {
                url.value?.let { link ->
                    OutlinedTextField(
                        value = link,
                        onValueChange = { url.value = it },
                        label = { Text("Enter URL") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    ElevatedButton(onClick = {
                        TextParser.parseUrl(link, speechText)
                    }) {
                        Text(text = "Extract website text")
                    }
                }
            }

            item {
                speechText.value?.let {
                    SpeechService.updateText(it)
                    Text(text = "Extracted Text")
                    Box(modifier = Modifier.height(15.dp))
                    Text(text = it)
                }
            }
        }
    }
}