package com.plusgallery.android.extension

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

class ExtensionManager(private val ctx: Context) {
    private val pkgName = "com.plusgallery.extension"
    private val pkgVersion = 1.2

    fun fetchInstalled() {
        val extPkgs = ctx.packageManager.getInstalledPackages(
            PackageManager.GET_CONFIGURATIONS).filter { validExtension(it) }
        if (extPkgs.isEmpty()) return
    }

    private fun validExtension(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.reqFeatures.orEmpty().any { it.name == pkgName }
    }
}