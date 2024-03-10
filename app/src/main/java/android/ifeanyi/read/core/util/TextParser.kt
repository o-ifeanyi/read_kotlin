package android.ifeanyi.read.core.util

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object TextParser {
    fun parsePdf(context: Context, uri: Uri, page: Int, onComplete: (result: String, pageCount: Int) -> Unit) {
        try {
            PDFBoxResourceLoader.init(context)
            val inputStream = context.contentResolver.openInputStream(uri)
            val doc = PDDocument.load(inputStream)
            val stripper = PDFTextStripper()
            stripper.startPage = page
            stripper.endPage = page
            val result = stripper.getText(doc).replace("\n", " ")
            onComplete.invoke(result, doc.numberOfPages)
            doc.close()
            inputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun parseImage(context: Context, uri: Uri, onComplete: (result: String) -> Unit) {
        try {
            val image = InputImage.fromFilePath(context, uri)

            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            recognizer.process(image)
                .addOnSuccessListener {
                    val result = it.text.replace("\n", " ")
                    onComplete.invoke(result)
                }
                .addOnFailureListener { }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun parseUrl(link: String, onComplete: (result: String) -> Unit) {
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
                val result = doc.text().replace("\n", " ")
                onComplete.invoke(result)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}