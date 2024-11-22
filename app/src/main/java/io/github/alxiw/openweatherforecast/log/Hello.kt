package io.github.alxiw.openweatherforecast.log

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor.Logger

object Hello {

    private const val TAG = "HELLO"

    val okHttpLogger = object : Logger {
        override fun log(message: String) {
            Log.d(TAG, message)
        }
    }

    fun d(tag: String, message: String) {
        Log.d(TAG, "$tag – $message")
    }

    fun e(tag: String, message: String) {
        Log.e(TAG, "$tag – $message")
    }

    fun i(tag: String, message: String) {
        Log.i(TAG, "$tag – $message")
    }

    fun w(tag: String, message: String) {
        Log.w(TAG, "$tag – $message")
    }
}
