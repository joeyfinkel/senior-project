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

class DBUtils(table: String) {
    private val url = "https://write-now.lesspopmorefizz.com/api/$table"
    private val client = OkHttpClient()

    /**
     * Posts data to the table.
     * @param path The path to the table.
     * If provided, the first section of the url **must** not include a slash.
     * @example For example, `user/add` is correct, but `/user/add` is not.
     */
    private suspend fun getJson(path: String? = null): String? =
        CoroutineScope(Dispatchers.IO).async {
            val url = if (path != null) "$url/$path" else url
            val request = Request.Builder().url(url).get().build()

            Log.d("URL", url)

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

    private fun requestBuilder(requestType: String, request: Request, callback: (Boolean) -> Unit) {
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API Error from `post#onFailure`", e.toString())

                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                Log.d("Response message", response.message)
                Log.d("Response code", response.code.toString())

                if (response.isSuccessful) {
                    Log.d("API Success from `$requestType`", "Posted successfully")
                    Log.d("API Success from `$requestType`", responseBody ?: "No response body")
                    callback(true)
                } else {
                    Log.e(
                        "API Error from `$requestType#onResponse`",
                        "Unexpected response code: ${response.code}"
                    )
                    Log.e(
                        "API Error from `$requestType#onResponse`",
                        responseBody ?: "No response body"
                    )

                    callback(false)
                }

                response.close().apply { Log.d("API Success", "Response closed") }
            }
        })
    }

    /**
     * Gets all the data from the table and returns a list of the data
     * @param path The path to the table. If provided, the first section of the url **must**
     * not include a slash.
     * @param callback A function that takes a JSONObject and returns a T
     * @return A list of T
     */
    suspend fun <T> getAll(path: String? = null, callback: (JSONObject) -> T): List<T> {
        val json = getJson(path)

        return json?.let {
//            Log.d("it", it)
            try {
                val jsonArray = JSONArray(it)

                (0 until jsonArray.length()).map { i ->
                    callback(jsonArray.getJSONObject(i))
                }
            } catch (e: Exception) {
                Log.e("API Error from `getAll`", e.toString())
                emptyList()
            }
        } ?: emptyList()
    }

    /**
     * Posts data to the table
     * @param section The section of the API to post to (e.g. "add")
     * @param json The data to post
     * @param callback A function that takes a Boolean and returns Unit
     * @return Unit
     */
    fun post(section: String, json: JSONObject, callback: (Boolean) -> Unit) {
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val req = Request.Builder().url("$url/$section").post(body).build()

        requestBuilder("post", req, callback)
    }

    fun put(section: String, json: JSONObject, callback: (Boolean) -> Unit) {
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val req = Request.Builder().url("$url/$section").put(body).build()

        requestBuilder("put", req, callback)
    }
}