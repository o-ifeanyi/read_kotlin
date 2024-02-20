package android.ifeanyi.read.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.core.content.ContextCompat
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

val Context.appVersion: String?
    get() {
        return try {
            val info = this.packageManager.getPackageInfo(this.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                "${info.versionName}.${info.longVersionCode}"
            } else {
                "${info.versionName}.${info.versionCode}"
            }
        } catch (exception: Exception) {
            null
        }
    }

fun Context.share(text: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    ContextCompat.startActivity(this, shareIntent, null)
}

fun Context.mailTo(to: String, subject: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "vnd.android.cursor.item/email"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    ContextCompat.startActivity(this, shareIntent, null)
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
get(){
    return try {
        val host = URI(this).host
        val domain = if (host.startsWith("www.")) host.substring(4) else host
        return domain.split(".").first()
    } catch (ex: Exception) {
        ""
    }
}

fun Date.dwdm(locale: Locale): String {
    return SimpleDateFormat("E, d MMM", locale).format(this)
}
