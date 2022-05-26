package me.dion.blink.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.JsonObject
import com.google.gson.JsonParser

object FileUtil {
    private const val FILE_NAME = "metadata.json"

    fun saveData(metadata: String, context: Context) {
        val fos = context.openFileOutput(FILE_NAME, MODE_PRIVATE)
        fos.write(metadata.encodeToByteArray())
    }

    fun loadData(context: Context): JsonObject {
        val fin = context.openFileInput(FILE_NAME)
        val bytes = fin.readBytes()
        return JsonParser.parseString(String(bytes)).asJsonObject
    }
}