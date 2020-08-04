package com.plusgallery.extension.network

import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody

class Http(url: String, method: String? = null, body: RequestBody? = null) {
    internal val builder: Request.Builder = Request.Builder()

    init {
        builder.url(url).addHeader("User-Agent", userAgent)
        if (method != null)
            builder.method(method, body)
    }

    companion object {
        val userAgent: String = System.getProperty("http.agent")!!

        fun get(url: String): Http {
            return Http(url)
        }

        fun post(url: String, params: Map<String, String>): Http {
            val body = FormBody.Builder()
            for (item in params)
                body.add(item.key, item.value)
            val result = Http(url)
            result.builder.post(body.build())
            return result
        }
    }
}