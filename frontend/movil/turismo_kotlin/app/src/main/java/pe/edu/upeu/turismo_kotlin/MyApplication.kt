package pe.edu.upeu.turismo_kotlin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@HiltAndroidApp
class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
    }
}