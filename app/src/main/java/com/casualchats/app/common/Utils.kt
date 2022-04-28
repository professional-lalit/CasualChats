package com.casualchats.app.common

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import java.io.*
import com.casualchats.app.R
import android.content.ContentResolver


object Utils {

    fun Context.showToast(msg: String) {
        Toast.makeText(this, msg, LENGTH_SHORT).show()
    }

    fun fileFromContentUri(context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getMimeType(context, contentUri)
        val fileName =
            getFilename(contentUri) + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getMimeType(context: Context, uri: Uri): String? {
        //Check uri format to avoid null
        val extension: String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //If scheme is a content
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
        }
        return extension
    }

    private fun getFilename(uri: Uri): String {
        return File(uri.path!!).name.split(".")[0]
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

    fun ComponentActivity.closeKeyboard() {
        val view: View = currentFocus!!
        val manager: InputMethodManager? = getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager?
        manager?.hideSoftInputFromWindow(
            view.windowToken, 0
        )
    }

    @DrawableRes
    fun getIconBy(extension: String): Int {
        val audioTypes = listOf("MP3", "WAV", "M4A", "WMA", "AAC", "FLAC")
        val videoTypes = listOf(
            "MP4", "MOV", "WMV", "AVI", "FLV", "MKV",
            "WEBM", "HTML5", "AVCHD", "FLV", "F4V", "SWF"
        )
        val imageTypes = listOf("GIF", "IEF", "JPE", "JPG", "JPEG", "PNG")
        val docTypes = listOf("PDF", "TXT", "RTF", "DOCX", "DOC")

        return when {
            extension.uppercase() in audioTypes -> {
                R.drawable.ic_audio
            }
            extension.uppercase() in videoTypes -> {
                R.drawable.ic_video_cam
            }
            extension.uppercase() in imageTypes -> {
                R.drawable.ic_camera
            }
            extension.uppercase() in docTypes -> {
                R.drawable.ic_document
            }
            else -> {
                R.drawable.ic_unknown_file
            }
        }

    }

}