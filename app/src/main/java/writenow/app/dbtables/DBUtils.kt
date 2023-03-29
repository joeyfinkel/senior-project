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
            val link = if (path != null) "$url/$path" else url
            val request = Request.Builder().url(link).get().build()

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

        if (json.equals("No results found")) {
            return emptyList()
        }

        return json?.let {
            try {
                val jsonArray = JSONArray(it)

                (0 until jsonArray.length()).map { i ->
                    callback(jsonArray.getJSONObject(i))
                }
            } catch (e: Exception) {
                Log.e("API Error from `getAll` ($path)", e.toString())
                emptyList()
            }
        } ?: emptyList()
    }

    /**
     * Posts data to the table.
     *
     * @param section The section of the API to post to (e.g. "add").
     * @param callback A function that takes a Boolean and returns Unit.
     * @return Unit.
     */
    fun post(section: String, callback: (Boolean) -> Unit) {
        val body = JSONObject().toString().toRequestBody("application/json".toMediaTypeOrNull())
        val req = Request.Builder().url("$url/$section").post(body).build()

        requestBuilder("post", req, callback)
    }

    /**
     * Posts data to the table.
     *
     * @param section The section of the API to post to (e.g. "add").
     * @param json The data to post.
     * @param callback A function that takes a Boolean and returns Unit.
     * @return Unit.
     */
    fun post(section: String, json: JSONObject, callback: (Boolean) -> Unit) {
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val req = Request.Builder().url("$url/$section").post(body).build()

        requestBuilder("post", req, callback)
    }

    /**
     * Puts data to the table.
     *
     * @param section The section of the API to put to (e.g. "update").
     * @param json The data to put.
     * @param callback A function that takes a Boolean and returns Unit.
     * @return Unit.
     */
    fun put(section: String, json: JSONObject, callback: (Boolean) -> Unit) {
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val req = Request.Builder().url("$url/$section").put(body).build()

        requestBuilder("put", req, callback)
    }

    /**
     * Deletes data from the table.
     *
     * @param section The section of the API to delete from (e.g. "delete").
     * @param json The data to delete.
     * @param callback A function that takes a Boolean and returns Unit.
     * @return Unit.
     */
    fun delete(section: String, json: JSONObject, callback: (Boolean) -> Unit) {
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val req = Request.Builder().url("$url/$section").delete(body).build()

        requestBuilder("delete", req, callback)
    }
}