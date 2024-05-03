package com.ifeanyi.read.core.util

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.ifeanyi.read.BuildConfig
import com.ifeanyi.read.core.services.AnalyticService
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant

object TextParser {
    fun parsePdf(
        context: Context,
        uri: Uri,
        page: Int,
        onComplete: (result: String, pageCount: Int, error: Exception?) -> Unit
    ) {
        try {
            PDFBoxResourceLoader.init(context)
            val inputStream = context.contentResolver.openInputStream(uri)
            val doc = PDDocument.load(inputStream)
            val stripper = PDFTextStripper()
            stripper.startPage = page
            stripper.endPage = page
            val result = stripper.getText(doc).formatted
            doc.close()
            inputStream?.close()
            onComplete.invoke(result, doc.numberOfPages, null)
        } catch (e: IOException) {
            onComplete.invoke("An error occurred while reading pdf", 0, e)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun parseImage(
        context: Context,
        uri: Uri,
        onComplete: (result: String, generated: Boolean?, error: Exception?) -> Unit
    ) {
        try {
            val image = InputImage.fromFilePath(context, uri)

            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            recognizer.process(image)
                .addOnSuccessListener {
                    val result = it.text.formatted

                    if (result.isNotEmpty()) {
                        onComplete.invoke(result, false, null)
                    } else {
                        GlobalScope.launch(Dispatchers.IO) {
                            try {
                                AnalyticService.track("describe_image")
                                val generativeModel = GenerativeModel(
                                    modelName = "gemini-pro-vision",
                                    apiKey = BuildConfig.GEMINI_KEY
                                )

                                val inputContent = content {
                                    image(image.bitmapInternal!!)
                                    text(Constants.describeImagePrompt)
                                }

                                val response = generativeModel.generateContent(inputContent)
                                onComplete.invoke(response.text!!, true, null)
                                AnalyticService.track("describe_image_success")
                            } catch (e: IOException) {
                                e.printStackTrace()
                                onComplete.invoke(
                                    "An error occurred while describing image",
                                    null,
                                    e
                                )
                            }
                        }
                    }

                }
                .addOnFailureListener {
                    onComplete.invoke("An error occurred while reading image", null, it)
                }

        } catch (e: IOException) {
            e.printStackTrace()
            onComplete.invoke("An error occurred while reading image", null, e)
        }
    }

    fun scansToPdf(
        context: Context,
        images: List<GmsDocumentScanningResult.Page>,
        onComplete: (file: File?) -> Unit
    ) {
        try {
            val document = PdfDocument()
            val completeProcess = {
                val path = "${context.filesDir.path}/${Instant.now()}.pdf"
                val outputFile = File(path)
                document.writeTo(outputFile.outputStream())
                document.close()
                onComplete.invoke(outputFile)
            }

            for ((index, image) in images.withIndex()) {
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                recognizer.process(InputImage.fromFilePath(context, image.imageUri))
                    .addOnSuccessListener {
                        val result = it.text
                        val pageInfo = PdfDocument.PageInfo.Builder(
                            PDRectangle.A4.width.toInt(),
                            PDRectangle.A4.height.toInt(),
                            index + 1
                        ).create()

                        val page = document.startPage(pageInfo)
                        val paint = Paint()
                        paint.textSize = 12f
                        val textHeight = paint.descent() - paint.ascent()
                        var yPos = 50F
                        for (line in result.split("\n")) {
                            page.canvas.drawText(line.formatted, 50F, yPos, paint)
                            yPos += textHeight
                        }

                        document.finishPage(page)

                        if (index == images.lastIndex) {
                            completeProcess()
                        }
                    }
                    .addOnFailureListener {
                        if (index == images.lastIndex) {
                            completeProcess()
                        }
                        println("FAILURE ON CREATING PAGE: $it")
                    }
            }
        } catch (e: IOException) {
            println("FAILURE ON SCAN TO PDF: $e")
            onComplete.invoke(null)
        }
    }

    fun parseText(uri: Uri, onComplete: (result: String, error: Exception?) -> Unit) {
        try {
            val file = File(uri.path ?: "")
            onComplete.invoke(file.readText().formatted, null)
        } catch (e: IOException) {
            e.printStackTrace()
            onComplete.invoke("An error occurred while reading text", e)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun parseUrl(link: String, onComplete: (result: String, error: Exception?) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(link)

                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))

                val htmlStringBuilder = StringBuilder()
                var line: String?

                while (bufferedReader.readLine().also { line = it } != null) {
                    htmlStringBuilder.append(line)
                }

                val htmlString = htmlStringBuilder.toString()

                val doc = Jsoup.parse(htmlString)
                val result = doc.text().formatted
                onComplete.invoke(result, null)
            } catch (e: IOException) {
                e.printStackTrace()
                onComplete.invoke("An error occurred while reading url", e)
            }
        }
    }
}