package pe.edu.upeu.turismo_kotlin.ui.presentation.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.edu.upeu.turismo_kotlin.modelo.LoginDto
import pe.edu.upeu.turismo_kotlin.modelo.LoginResponse
import pe.edu.upeu.turismo_kotlin.repository.LoginRepository
import pe.edu.upeu.turismo_kotlin.utils.JwtUtils
import pe.edu.upeu.turismo_kotlin.utils.TokenUtils
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepository
) : ViewModel(){


    private val _isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val _islogin: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val islogin: LiveData<Boolean> get() = _islogin



    val isError=MutableLiveData<Boolean>(false)

    val listUser = MutableLiveData<LoginResponse>()
    suspend fun loginSys(toData: LoginDto) {
        Log.i("LOGIN", toData.username)
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _islogin.postValue(false)

            val response = loginRepo.login(toData).body()

            delay(1500L)
            response?.let {
                TokenUtils.TOKEN_CONTENT = "Bearer ${it.token}"
                TokenUtils.ROL = JwtUtils.getRoleFromJwt(it.token) ?: "UNKNOWN"
                listUser.postValue(it)

                Log.i("DATAXDMP", TokenUtils.TOKEN_CONTENT)
                Log.i("RoleCheck", "El rol es: ${TokenUtils.ROL}")
                if (TokenUtils.TOKEN_CONTENT != "Bearer null") {
                    TokenUtils.USER_LOGIN = toData.username
                    _islogin.postValue(true)
                } else {
                    isError.postValue(true)
                    delay(1500L)
                    isError.postValue(false)
                }
            }
            _isLoading.postValue(false)
        }
    }
}