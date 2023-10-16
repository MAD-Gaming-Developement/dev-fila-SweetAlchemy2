package br.brlgames.sweetalchemy2.API

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

object Volley {
    private var mRequestQueue: RequestQueue? = null
    @JvmStatic
    fun init(context: Context?) {
        mRequestQueue = Volley.newRequestQueue(context)
    }

    @JvmStatic
    val requestQueue: RequestQueue?
        get() = if (mRequestQueue != null) {
            mRequestQueue
        } else {
            throw IllegalStateException("RequestQueue not initialized")
        }
}