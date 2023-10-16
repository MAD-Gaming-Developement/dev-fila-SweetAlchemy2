package br.brlgames.sweetalchemy2.API


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class ApiService : BroadcastReceiver() {
    // Define the API endpoint for fetching data.
    override fun onReceive(context: Context, intent: Intent) {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        if (Listener != null) {
            val isConnected = capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))

            Listener.onNetworkChange(isConnected)

        }
    }
    interface ReceiverListener {
        fun onNetworkChange(isConnected: Boolean)
    }
    companion object {
        lateinit var Listener: ReceiverListener
    }

}
