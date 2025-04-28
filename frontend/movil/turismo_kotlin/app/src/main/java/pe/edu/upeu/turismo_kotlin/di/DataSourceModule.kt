package pe.edu.upeu.turismo_kotlin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pe.edu.upeu.turismo_kotlin.data.remote.RestCategoria
import pe.edu.upeu.turismo_kotlin.data.remote.RestFile
import pe.edu.upeu.turismo_kotlin.data.remote.RestLogin
import pe.edu.upeu.turismo_kotlin.data.remote.RestLugarTuristico
import pe.edu.upeu.turismo_kotlin.data.remote.RestRol
import pe.edu.upeu.turismo_kotlin.data.remote.RestUsuario
import pe.edu.upeu.turismo_kotlin.repository.LoginRepository
import pe.edu.upeu.turismo_kotlin.utils.TokenUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl(): String = TokenUtils.API_URL

    @Singleton
    @Provides
    fun provideRetrofit(@Named("BaseUrl") baseUrl: String): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()
    }

    @Singleton
    @Provides
    fun restLogin(retrofit: Retrofit): RestLogin {
        return retrofit.create(RestLogin::class.java)
    }

    @Singleton
    @Provides
    fun restRol(retrofit: Retrofit): RestRol {
        return retrofit.create(RestRol::class.java)
    }

    @Singleton
    @Provides
    fun restUsuario(retrofit: Retrofit): RestUsuario {
        return retrofit.create(RestUsuario::class.java)
    }

    @Singleton
    @Provides
    fun restFile(retrofit: Retrofit): RestFile {
        return retrofit.create(RestFile::class.java)
    }

    @Singleton
    @Provides
    fun restLugarTuristico(retrofit: Retrofit): RestLugarTuristico {
        return retrofit.create(RestLugarTuristico::class.java)
    }

    @Singleton
    @Provides
    fun restCategoria(retrofit: Retrofit): RestCategoria {
        return retrofit.create(RestCategoria::class.java)
    }
}