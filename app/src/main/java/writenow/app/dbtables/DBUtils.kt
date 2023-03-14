package writenow.app.dbtables

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

open class DBUtils(table: String) {
    private val url = "http://write-now.lesspopmorefizz.com/api/$table"
    private val client = OkHttpClient()

    private suspend fun getJson(): String? = CoroutineScope(Dispatchers.IO).async {
        val request = Request.Builder().url(url).build()

        return@async try {
            val response = client.newCall(request).execute()
            val json = response.body?.string()

            response.close()
            json
        } catch (e: Exception) {
            Log.e("API Error from `getJson`", e.toString())
            null
        }
    }.await()

    suspend fun <T> getAll(callback: (JSONObject) -> T): List<T> {
        val json = getJson()

        return json?.let {
            val jsonArray = JSONArray(it)

            (0 until jsonArray.length()).map { i ->
                callback(jsonArray.getJSONObject(i))
            }
        } ?: emptyList()
    }

    fun post(section: String, json: JSONObject, callback: (Boolean) -> Unit) {
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val req = Request.Builder().url("$url/$section").post(body).build()

        Log.d("URL", "$url/$section")
        Log.d("JSON", json.toString())

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API Error from `post#onFailure`", e.toString())

                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                Log.d("Response message", response.message)
                Log.d("Response code", response.code.toString())

                if (response.isSuccessful) {
                    Log.d("API Success", "Posted successfully")
                    callback(true)
                } else {
                    Log.e(
                        "API Error from `post#onResponse`",
                        "Unexpected response code: ${response.code}"
                    )
                    Log.e("API Error from `post#onResponse`", responseBody ?: "No response body")

                    callback(false)
                }

                response.close().apply { Log.d("API Success", "Response closed") }
            }
        })
    }
}