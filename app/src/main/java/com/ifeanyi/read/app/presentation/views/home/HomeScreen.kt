package com.ifeanyi.read.app.presentation.views.home

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import com.ifeanyi.read.app.data.models.FileModel
import com.ifeanyi.read.app.data.models.LibraryType
import com.ifeanyi.read.app.presentation.components.ListTileComponent
import com.ifeanyi.read.app.presentation.viewmodel.LibraryViewModel
import com.ifeanyi.read.app.presentation.viewmodel.SettingsViewModel
import com.ifeanyi.read.app.presentation.views.setting.WhatsNewSheet
import com.ifeanyi.read.core.route.Routes
import com.ifeanyi.read.core.services.AnalyticService
import com.ifeanyi.read.core.services.SpeechService
import com.ifeanyi.read.core.theme.AppIcons
import com.ifeanyi.read.core.util.getName
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.ifeanyi.read.core.services.AppStateService
import com.ifeanyi.read.core.util.TextParser


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    controller: NavHostController,
    libraryVM: LibraryViewModel = hiltViewModel(),
    settingsVM: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val settingState = settingsVM.state.collectAsState().value

    val showUrlSheet = remember { mutableStateOf(false) }

    fun handleUriToModel(uri: Uri, type: LibraryType) {
        if (uri.path == null) return
        val input = context.contentResolver.openInputStream(uri) ?: return
        val outputFile = context.filesDir.resolve(uri.getName(context))
        input.copyTo(outputFile.outputStream())
        input.close()
        val newUri = outputFile.toUri()
        val model = FileModel(
            name = outputFile.name,
            type = type,
            path = newUri.toString(),
        )
        SpeechService.updateModel(model) {
            libraryVM.insertItem(it)
        }
    }

    val docLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        it?.let { uri ->
            handleUriToModel(uri, LibraryType.Pdf)
            AnalyticService.track("select_doc")
        }
    }

    val imageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri ->
                handleUriToModel(uri, LibraryType.Img)
                AnalyticService.track("select_image")
            }
        }

    val notificationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { }
    )

    val options = remember {
        GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(2)
            .setResultFormats(RESULT_FORMAT_JPEG)
            .setScannerMode(SCANNER_MODE_FULL)
            .build()
    }
    val scanner = remember { GmsDocumentScanning.getClient(options) }

    val scannerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == RESULT_OK) {
                    GmsDocumentScanningResult.fromActivityResultIntent(result.data)?.pages?.let { pages ->
                        AnalyticService.track("scan_page")
                        AppStateService.displayLoader()
                        TextParser.scansToPdf(context, pages) { file ->
                            AppStateService.removeLoader()
                            if (file == null) return@scansToPdf
                            val model = FileModel(
                                name = file.name,
                                type = LibraryType.Scan,
                                path = file.toUri().toString(),
                            )
                            SpeechService.updateModel(model) {
                                libraryVM.insertItem(it)
                            }
                        }
                    }
                }
            }
        )

    LaunchedEffect(key1 = Unit) {
        SpeechService.initSpeechService(context) {
            libraryVM.updateItem(it)
        }
        notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Home") })
        }
    ) { padding ->
        if (showUrlSheet.value) {
            EnterUrlSheet(showUrlSheet, libraryVM)
        }
        if (settingState.showWhatsNew.value) {
            WhatsNewSheet(showWhatsNewSheet = settingState.showWhatsNew)
        }
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                start = 20.dp, end = 20.dp,
                bottom = 200.dp
            ),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                ListTileComponent(
                    asset = {
                        Icon(
                            imageVector = AppIcons.doc, contentDescription = "", modifier = Modifier
                                .size(50.dp)
                        )
                    },
                    title = "Pick document",
                    subtitle = "Listen to the content of a PDF",
                    onClick = { docLauncher.launch(arrayOf("application/pdf")) }
                )
            }

            item {
                ListTileComponent(
                    asset = {
                        Icon(
                            imageVector = AppIcons.image,
                            contentDescription = "",
                            modifier = Modifier
                                .size(50.dp)
                        )
                    },
                    title = "Pick image",
                    subtitle = "Listen to the content of an image",
                    onClick = {
                        imageLauncher.launch(
                            PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo
                            )
                        )
                    }
                )
            }

            item {
                ListTileComponent(
                    asset = {
                        Icon(
                            imageVector = AppIcons.scan,
                            contentDescription = "",
                            modifier = Modifier
                                .size(50.dp)
                        )
                    },
                    title = "Scan page",
                    subtitle = "Listen to the content of a page",
                    onClick = {
                        scanner.getStartScanIntent(context as Activity)
                            .addOnSuccessListener { intentSender ->
                                scannerLauncher.launch(
                                    IntentSenderRequest.Builder(intentSender).build()
                                )
                            }.addOnFailureListener {
                                // handle errors here.
                            }
                    }
                )
            }

            item {
                ListTileComponent(
                    asset = {
                        Icon(
                            imageVector = AppIcons.text,
                            contentDescription = "",
                            modifier = Modifier
                                .size(50.dp)
                        )
                    },
                    title = "Paste or write text",
                    subtitle = "Listen to the content of the text",
                    onClick = {
                        controller.navigate(Routes.EnterTextScreen.name)
                    }
                )
            }

            item {
                ListTileComponent(
                    asset = {
                        Icon(
                            imageVector = AppIcons.link,
                            contentDescription = "",
                            modifier = Modifier
                                .size(50.dp)
                        )
                    },
                    title = "Paste web link",
                    subtitle = "Listen to the content of a website",
                    onClick = { showUrlSheet.value = true }
                )
            }
        }
    }
}
