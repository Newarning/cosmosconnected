package core.connexion

import kotlinx.coroutines.flow.StateFlow

interface ConnexionManager {

    fun networkIsAvailable(): StateFlow<Boolean>

}