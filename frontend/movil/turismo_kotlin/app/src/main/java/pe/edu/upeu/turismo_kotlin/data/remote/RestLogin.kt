package pe.edu.upeu.turismo_kotlin.data.remote

import pe.edu.upeu.turismo_kotlin.modelo.LoginDto
import pe.edu.upeu.turismo_kotlin.modelo.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RestLogin {
    @POST("/auth/login")
    suspend fun login(@Body loginDto: LoginDto): Response<LoginResponse>
}