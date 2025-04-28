package pe.edu.upeu.turismo_kotlin.repository

import pe.edu.upeu.turismo_kotlin.data.remote.RestLogin
import pe.edu.upeu.turismo_kotlin.modelo.LoginDto
import pe.edu.upeu.turismo_kotlin.modelo.LoginResponse
import retrofit2.Response
import javax.inject.Inject

interface LoginRepository {
    suspend fun login(loginDto: LoginDto): Response<LoginResponse>
}

class LoginRepositoryImpl @Inject constructor(private val restLogin: RestLogin): LoginRepository {
    override suspend fun login(loginDto: LoginDto): Response<LoginResponse> {
        return restLogin.login(loginDto)
    }
}