package com.deserve.nearrestaurant.domain

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object NetworkRequest {

    const val API_KEY = "Define API Key here"

    fun createRequest(url: String): HttpURLConnection {
        val url = URL(url)
        return url.openConnection() as HttpURLConnection
    }

    fun HttpURLConnection.setHeaders(headers: HashMap<String, String>) {
        for (key in headers.keys) {
            this.setRequestProperty(key, headers[key])
        }
    }

    @Throws(Exception::class)
    suspend inline fun <reified T>makeRequest(request: HttpURLConnection): T {
        return withContext(Dispatchers.IO) {
            var data: T? = null
//            val executor = Executors.newSingleThreadExecutor()
//            executor.execute {
                try {
                    request.setRequestProperty("accept", "application/json")
                    val br = BufferedReader(InputStreamReader(request.inputStream))

                    // read the input stream
                    // in this case, I simply read the first line of the stream
                    val line = br.readLine()
                    line?.let {
                        data = gsonConverter<T>(it)
                        return@withContext data as T
                    }
                } catch (e: Exception) {
                    throw e
                } finally {
                    request.disconnect()
                }
//            }
            return@withContext  data as T
        }
    }

    inline fun <reified T>gsonConverter(data: String): T {
        val gson = Gson()
        if (isValidJson(gson, data)) {
            val data = gson.fromJson(data, T::class.java)
            return data
        } else {
            throw JsonParseException("Not valid json data")
        }
    }

    fun isValidJson(gson: Gson, json: String): Boolean {
        try {
            val jsonElement: JsonElement = gson.fromJson(json, JsonElement::class.java)
            if (!jsonElement.isJsonObject && !jsonElement.isJsonArray) return false
        } catch (e: java.lang.Exception) {
            return false
        }
        return true
    }


}