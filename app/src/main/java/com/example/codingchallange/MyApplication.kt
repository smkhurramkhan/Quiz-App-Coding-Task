package com.example.codingchallange

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.jetbrains.annotations.NotNull
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {

    val kTag = "khurramTag"
    override fun onCreate() {
        super.onCreate()
        instance = this
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    @NotNull message: String,
                    t: Throwable?
                ) {
                    super.log(priority, String.format(kTag, tag), message, t)
                }
            })
        }
    }

    companion object {
        @JvmStatic
        var instance: MyApplication? = null
    }
}