package com.plusgallery.android.extension

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import com.plusgallery.extension.BaseInterface
import dalvik.system.PathClassLoader

class StoredExtension {
    private lateinit var pkgInfo: PackageInfo
    lateinit var baseClass: BaseInterface
    lateinit var remote: RemoteExtension
    val icon: Int get() = pkgInfo.applicationInfo.icon
    val label: Int get() = pkgInfo.applicationInfo.labelRes
    val packageName: String get() = pkgInfo.packageName
    @Suppress("DEPRECATION")
    val versionCode: Int get() = pkgInfo.versionCode
    val versionName: String get() = pkgInfo.versionName
    val filePath: String get() = pkgInfo.applicationInfo.dataDir

    companion object {
        fun parse(context: Context, path: String): StoredExtension? {
            val manager = context.packageManager
            val extension = StoredExtension()

            val pkg = manager.getPackageArchiveInfo(path, PackageManager.GET_META_DATA
                    or PackageManager.GET_ACTIVITIES) ?: return null
            // Workaround issue https://issuetracker.google.com/issues/36918514
            pkg.applicationInfo.sourceDir = path
            pkg.applicationInfo.publicSourceDir = path
            extension.pkgInfo = pkg

            val basePath = pkg.applicationInfo.metaData.getString(BaseInterface::class.simpleName)
                ?: return null
            val loader = PathClassLoader(path, null, context.classLoader)
            val instance = loader.loadClass(pkg.packageName.plus(basePath)).newInstance()
                ?: return null
            extension.baseClass = instance as BaseInterface
            return extension
        }
    }

    fun hasRemote(): Boolean {
        return this::remote.isInitialized
    }

    fun getResources(context: Context): Resources {
        return context.packageManager.getResourcesForApplication(pkgInfo.applicationInfo)
    }
}