package com.ifeanyi.read.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import com.ifeanyi.read.BuildConfig
import java.net.URI
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale


val Locale.flagEmoji: String?
    get() {
        return try {
            val firstLetter = Character.codePointAt(country, 0) - 0x41 + 0x1F1E6
            val secondLetter = Character.codePointAt(country, 1) - 0x41 + 0x1F1E6
            String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
        } catch (exception: Exception) {
            null
        }
    }
val appVersion: String
    get() {
        return "${BuildConfig.VERSION_NAME}+${BuildConfig.VERSION_CODE}"
    }

fun Context.share(text: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    this.startActivity(shareIntent, null)
}

fun Context.mailTo(to: String, subject: String) {
    val selectorIntent = Intent(Intent.ACTION_SENDTO)
    selectorIntent.setData(Uri.parse("mailto:"))

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        selector = selectorIntent
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    this.startActivity(shareIntent)
}

fun Uri.getName(context: Context): String {
    val returnCursor = context.contentResolver.query(this, null, null, null, null)
        ?: return LocalDateTime.now().toString()
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val fileName = returnCursor.getString(nameIndex)
    returnCursor.close()
    return fileName.split(".").first()
}

val String.trimUrl: String
    get() {
        return try {
            val host = URI(this).host
            val domain = if (host.startsWith("www.")) host.substring(4) else host
            return domain.split(".").first()
        } catch (ex: Exception) {
            ""
        }
    }

val String.formatted: String
    get() {
        val first = this.replace(Regex("[*]"), "")
        return first.replace(Regex("[\\s+]"), " ")
    }

fun Date.dwdm(locale: Locale): String {
    return SimpleDateFormat("E, d MMM", locale).format(this)
}
