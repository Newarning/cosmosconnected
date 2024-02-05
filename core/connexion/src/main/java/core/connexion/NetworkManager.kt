package core.connexion

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class NetworkManager @Inject constructor(
    context : Context,
    coroutineScope: CoroutineScope
): ConnexionManager {

    private var  connectivityManager : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _isOnline= MutableStateFlow(isOnline())
    private val isOnline: StateFlow<Boolean> =
        _isOnline
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = isOnline()
            )

    init {

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.requestNetwork(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _isOnline.update { true }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                _isOnline.update { true }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _isOnline.update { false }
            }
        })
    }

    private fun isOnline() : Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                true
            }  else capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            false
        }
    }

    override fun networkIsAvailable(): StateFlow<Boolean> {
        return isOnline
    }
}