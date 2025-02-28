package gupta.saurabh.github.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkUtils {

    companion object {

        fun isInternetAvailable(context: Context?): Boolean {

            var result = false

            context?.let {

                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

                cm?.run {

                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {

                        result = when {

                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

                            else -> false
                        }
                    }
                }
            }

            return result
        }
    }
}