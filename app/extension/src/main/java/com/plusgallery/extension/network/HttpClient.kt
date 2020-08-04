package com.plusgallery.extension.network

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.closeQuietly
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException

class HttpClient {
    private val client: OkHttpClient = OkHttpClient()

    fun request(request: Http, ret: (r: Any) -> Unit) {
        client.newCall(request.builder.build())
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) = ret(e)
                override fun onResponse(call: Call, response: Response) {
                    ret(response)
                    response.closeQuietly()
                }
            })
    }

    fun download(request: Http, file: File, ret: (r: Any) -> Unit) {
        file.deleteOnExit()
        client.newCall(request.builder.build())
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) = ret(e)
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val sink: BufferedSink = file.sink().buffer()
                        sink.writeAll(response.body!!.source())
                        sink.close()
                    }
                    ret(response)
                    response.closeQuietly()
                }
            })
    }

    fun cancelRequests() {
        for (call in client.dispatcher.queuedCalls()) {
            call.cancel()
        }
    }
}