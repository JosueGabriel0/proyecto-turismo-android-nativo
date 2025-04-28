package pe.edu.upeu.turismo_kotlin.repository

import pe.edu.upeu.turismo_kotlin.data.remote.RestUsuario
import pe.edu.upeu.turismo_kotlin.utils.TokenUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.edu.upeu.turismo_kotlin.modelo.UsuarioDto
import pe.edu.upeu.turismo_kotlin.modelo.UsuarioResponse
import retrofit2.Response
import javax.inject.Inject

interface UsuarioRepository {
    suspend fun getUsuariosCompleto(): List<UsuarioResponse>
    suspend fun getUsuarioCompletoById(idUsuario: Long): UsuarioResponse?
    suspend fun postUsuarioCompleto(usuarioDto: UsuarioDto, file: MultipartBody.Part?): UsuarioResponse?
    suspend fun putUsuarioCompleto(idUsuario: Long, usuarioDto: UsuarioDto, file: MultipartBody.Part?): UsuarioResponse?
    suspend fun deleteUsuarioCompleto(idUsuario: Long): Boolean
}

class UsuarioRepositoryImpl @Inject constructor(
    private val restUsuario: RestUsuario
) : UsuarioRepository {

    override suspend fun getUsuariosCompleto(): List<UsuarioResponse> {
        val response = restUsuario.getUsuariosCompleto(TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() ?: emptyList()
        else emptyList()
    }

    override suspend fun getUsuarioCompletoById(idUsuario: Long): UsuarioResponse? {
        val response = restUsuario.getUsuarioCompletoById(idUsuario, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun postUsuarioCompleto(
        usuarioDto: UsuarioDto,
        file: MultipartBody.Part?
    ): UsuarioResponse? {
        // Convertir UsuarioCompletoDto a JSON y enviar junto con el archivo
        val jsonUsuario = RequestBody.create(MultipartBody.FORM, usuarioDto.toJson())  // Crear RequestBody para el JSON

        val response = restUsuario.postUsuarioCompleto(jsonUsuario, file, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun putUsuarioCompleto(
        idUsuario: Long,
        usuarioDto: UsuarioDto,
        file: MultipartBody.Part?
    ): UsuarioResponse? {
        // Convertir UsuarioCompletoDto a JSON y enviar junto con el archivo
        val jsonUsuario = RequestBody.create(MultipartBody.FORM, usuarioDto.toJson())  // Crear RequestBody para el JSON

        val response = restUsuario.putUsuarioCompleto(idUsuario, jsonUsuario, file, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun deleteUsuarioCompleto(idUsuario: Long): Boolean {
        val response = restUsuario.deleteUsuarioCompleto(idUsuario, TokenUtils.TOKEN_CONTENT)
        return response.isSuccessful
    }
}