package core.connexion.di

import android.content.Context
import core.connexion.ConnexionManager
import core.connexion.NetworkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ConnexionModule {

    @Singleton
    @Provides
    fun providesApplicationContext(@ApplicationContext context: Context) = context


    @Singleton
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnexionBindingModule {

    @Singleton
    @Binds
    abstract fun bindRepository(networkManager: NetworkManager): ConnexionManager

}