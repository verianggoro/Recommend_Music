package com.pembelajar.musicrecommendation.commons

import android.content.Context
import android.net.Uri
import com.google.common.collect.Multimap
import com.pembelajar.musicrecommendation.R
import okhttp3.*
import java.io.IOException
import java.io.PrintWriter
import java.io.Reader
import java.util.concurrent.TimeUnit

object OkHttpHelper {
    private var client: OkHttpClient? = null
    private val TAG = OkHttpHelper::class.java.simpleName

    fun getClient(): OkHttpClient {
        if (client == null) {
            client = OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }
        return client!!
    }

    @Throws(IOException::class, OkHttpException::class)
    fun executePost(
        url: String,
        params: HashMap<String, String>? = null,
        form: HashMap<String, String?>? = null,
        headers: Map<String, String>? = null
    ): String? {
        val builtUri = Uri.parse(url).buildUpon()
        val builder = FormBody.Builder()
        val buildReq = Request.Builder()
        val iterator = form?.entries!!.iterator()
        val iteratorParams = params?.entries!!.iterator()
        while (iteratorParams.hasNext()){
            val pairParams = iteratorParams.next() as Map.Entry<*,*>
            builtUri.appendQueryParameter(pairParams.key.toString(), pairParams.value.toString())
        }
        while (iterator.hasNext()) {
            val pair = iterator.next() as Map.Entry<*, *>
            builder.add(pair.key.toString(), pair.value.toString())
        }

        if (!headers.isNullOrEmpty()) {
            for (key in headers.keys) {
                headers[key]?.let { buildReq.addHeader(key, it) }
            }
        }
        val formBody = builder.build()
        val uri = builtUri.build()
        val request = buildReq.url(uri.toString()).post(formBody).build()
        val response = getClient().newCall(request).execute()
        return getStringBody(response, response.body())
    }

    @Throws(IOException::class, OkHttpException::class)
    fun executePostSeat(
        url: String,
        params: HashMap<String, String>? = null,
        form: Multimap<String, String?>? = null,
        headers: Map<String, String>? = null
    ): String? {
        val buildUri = Uri.parse(url).buildUpon()
        val builder = FormBody.Builder()
        val buildReq = Request.Builder()
        val iterator = form?.entries()!!.iterator()
        val iteratorParams = params?.entries!!.iterator()
        while (iteratorParams.hasNext()){
            val pairParams = iteratorParams.next() as Map.Entry<*,*>
            buildUri.appendQueryParameter(pairParams.key.toString(), pairParams.value.toString())
        }
        while (iterator.hasNext()) {
            val pair = iterator.next() as Map.Entry<*, *>
            builder.add(pair.key.toString(), pair.value.toString())
        }

        if (!headers.isNullOrEmpty()) {
            for (key in headers.keys) {
                headers[key]?.let { buildReq.addHeader(key, it) }
            }
        }
        val formBody = builder.build()
        val uri = buildUri.build()
        val request = buildReq.url(uri.toString()).post(formBody).build()
        val response = getClient().newCall(request).execute()
        return getStringBody(response, response.body())
    }

    @Throws(IOException::class, OkHttpException::class)
    fun executeGet(
        url: String,
        params: HashMap<String, String>? = null,
        form: HashMap<String, String?>? = null,
        headers: Map<String, String>? = null
    ): String? {
        val builtUri = Uri.parse(url).buildUpon()
        val builder = FormBody.Builder()
        val buildReq = Request.Builder()
        val iterator = form?.entries!!.iterator()
        val iteratorParams = params?.entries!!.iterator()
        while (iteratorParams.hasNext()){
            val pairParams = iteratorParams.next() as Map.Entry<*, *>
            builtUri.appendQueryParameter(pairParams.key.toString(), pairParams.value.toString())
        }
        while (iterator.hasNext()) {
            val pair = iterator.next() as Map.Entry<*, *>
            builder.add(pair.key.toString(), pair.value.toString())
        }
        if (!headers.isNullOrEmpty()) {
            for (key in headers.keys) {
                headers[key]?.let { buildReq.addHeader(key, it) }
            }
        }
        val uri = builtUri.build()
        val request = buildReq.url(uri.toString()).build()
        val response = getClient().newCall(request).execute()
        return getStringBody(response, response.body())
    }

    fun cancelExecute(){
        getClient().dispatcher()
    }

    fun getStatusMessage(statusCode: Int): String {
        return if (statusCode == 400) {
            "Bad request"
        } else if (statusCode == 401) {
            "Unauthorized"
        } else if (statusCode == 404) {
            "Page not found"
        } else if (statusCode == 500) {
            "Internal server error"
        } else {
            "Status: $statusCode"
        }
    }

    @Throws(IOException::class, OkHttpException::class)
    fun getStringBody(response: Response, body: ResponseBody?): String {
        if (response.code() != 200 && response.code() != 201 && response.code() != 204) {
            throw OkHttpException(response)
        }

        if (body != null) {
            val builder = StringBuilder()
            val buffer = CharArray(4096)
            var reader: Reader? = null
            var totalLength: Long = 0
            var readLength: Int

            try {
                reader = body.charStream()
                readLength = reader.read(buffer)

                while (readLength > 0) {
                    builder.append(buffer, 0, readLength)
                    totalLength += readLength.toLong()
                    readLength = reader.read(buffer)
                }
            } finally {
                reader?.close()
            }

            return builder.toString()
        } else {
            return ""
        }
    }

    class OkHttpException @Throws(IOException::class)
    constructor(response: Response) : Exception(getStatusMessage(response.code())) {
        private val statusMessage: String
        private var bodyString: String? = null
        private val method: String
        private val url: String
        val statusCode: Int
        val body: ByteArray?

        init {
            val body = response.body()
            this.statusCode = response.code()
            this.statusMessage = response.message()
            this.method = response.request().method()
            this.url = response.request().url().toString()
            this.body = body?.bytes()
        }

        fun getBodyString(): String {
            if (this.bodyString == null) {
                if (this.body == null || this.body.size == 0) {
                    this.bodyString = this.method + " " + this.url + "\nStatus: " + this.statusCode + " " + this.statusMessage
                } else {
                    this.bodyString =
                        this.method + " " + this.url + "\nStatus: " + this.statusCode + " " + this.statusMessage + "\n" + String(this.body)
                }
            }
            return this.bodyString!!
        }

        override fun printStackTrace(s: PrintWriter) {
            s.write(this.getBodyString())
            super.printStackTrace(s)
        }
    }

    fun getUserUnderstandableError(context: Context, throwable: Throwable): String {
        return if (throwable is OkHttpException) {
            when {
                throwable.body == null -> context.getString(R.string.failed_connect_to_server)
                throwable.message.isNullOrBlank() -> context.getString(R.string.unknown_error)
                else -> throwable.message!!
            }
        } else {
            if (throwable.message.isNullOrBlank()) {
                throwable.javaClass.toString()
            } else {
                throwable.message!!
            }
        }
    }

}