package com.plusgallery.android.extension

import android.content.Context
import com.google.gson.Gson
import com.plusgallery.extension.network.Http
import com.plusgallery.extension.network.HttpClient
import okhttp3.Response
import java.io.File
import kotlin.collections.HashMap

class ExtensionManager(private val context: Context) {
    private val storedMap = HashMap<String, StoredExtension>()
    private val remoteMap = HashMap<String, RemoteExtension>()

    companion object {
        const val REMOTE_INDEX = "${RemoteExtension.REMOTE_ROOT}/index.json"
    }

    fun storedArray(): Array<StoredExtension> {
        return storedMap.values.toTypedArray()
    }

    fun remoteArray(): Array<RemoteExtension> {
        return remoteMap.values.toTypedArray()
    }

    fun fetchStored() {
        context.cacheDir.listFiles()?.forEach { file ->
            val extension = StoredExtension.parse(context, file.absolutePath)
                ?: return@forEach // Continue if cannot parse
            // Add only if not existing
            if (!storedMap.containsKey(extension.packageName))
                storedMap[extension.packageName] = extension
        }
    }

    fun fetchRemote(call: (result: Boolean) -> Unit) {
        HttpClient().request(Http.get(REMOTE_INDEX)) { r ->
            if (r is Response && r.isSuccessful) {
                Gson().fromJson(r.body?.string(),
                    Array<RemoteExtension>::class.java).forEach {
                    val stored = storedMap[it.packageName]
                    if (stored != null) {
                        // In case of already installed
                        stored.remote = it
                    } else {
                        // In case of not installed
                        remoteMap[it.packageName] = it
                    }
                }
                call(true)
            } else {
                call(false)
            }
        }
    }

    fun uninstall(extension: StoredExtension) {
        // Copy as a remote one
        if (extension.hasRemote())
            remoteMap[extension.packageName] = extension.remote
        // Delete stored link and file
        storedMap.remove(extension.packageName)
        File(extension.filePath).delete()
    }

    fun install(context: Context, extension: RemoteExtension, invoker: (success: Boolean) -> Unit) {
        val file = File(context.cacheDir, extension.packageName)
        HttpClient().download(Http.get(extension.fileFullPath()), file) {
            if (it is Response && it.isSuccessful) {
                // Parse extension if possible
                val stored = StoredExtension.parse(context, file.absolutePath)
                if (stored == null) {
                    invoker(false)
                    return@download
                }
                // Remove remote link
                remoteMap.remove(extension.packageName)
                // Add stored link
                stored.remote = extension
                storedMap[stored.packageName] = stored
                invoker(true)
            } else {
                invoker(false)
            }
        }
    }
}