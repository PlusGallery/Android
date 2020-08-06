package com.plusgallery.android.extension

class RemoteExtension {
    var icon: String = ""
    var label: String = ""
    var packageName: String = ""
    var versionCode: Int = 0
    var versionName: String = ""
    var filePath: String = ""

    companion object {
        const val REMOTE_ROOT = "https://raw.githubusercontent.com/PlusGallery/Extension/Test"
    }

    fun iconFullPath(): String {
        return REMOTE_ROOT.plus(icon)
    }

    fun fileFullPath(): String {
        return REMOTE_ROOT.plus(filePath)
    }
}