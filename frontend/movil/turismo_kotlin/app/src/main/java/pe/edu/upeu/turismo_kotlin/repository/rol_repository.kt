package pe.edu.upeu.turismo_kotlin.repository

import pe.edu.upeu.turismo_kotlin.data.remote.RestRol
import pe.edu.upeu.turismo_kotlin.modelo.RolDto
import pe.edu.upeu.turismo_kotlin.modelo.RolResponse
import pe.edu.upeu.turismo_kotlin.utils.TokenUtils
import retrofit2.Response
import javax.inject.Inject

//"RolRepository es una interfaz que define operaciones para manejar roles (obtener, crear, actualizar y eliminar).
//RolRepositoryImpl es la implementación real que usa RestRol para hacer las peticiones a la API
// y devuelve los resultados."

interface RolRepository {
    suspend fun getRoles(): List<RolResponse>
    suspend fun getRolById(idRol: Int): RolResponse?
    suspend fun postRol(rolDto: RolDto): RolResponse?
    suspend fun putRol(idRol: Int, rolDto: RolDto): RolResponse?
    suspend fun deleteRol(idRol: Int): Boolean
}

class RolRepositoryImpl @Inject constructor(
    private val restRol: RestRol
) : RolRepository {

    override suspend fun getRoles(): List<RolResponse> {
        val response = restRol.getRoles(TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() ?: emptyList()
        else emptyList()
    }

    override suspend fun getRolById(idRol: Int): RolResponse? {
        val response = restRol.getRolById(idRol, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun postRol(rolDto: RolDto): RolResponse? {
        val response = restRol.postRol(rolDto, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun putRol(idRol: Int, rolDto: RolDto): RolResponse? {
        val response = restRol.putRol(idRol, rolDto, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun deleteRol(idRol: Int): Boolean {
        val response = restRol.deleteRol(idRol, TokenUtils.TOKEN_CONTENT)
        return response.isSuccessful
    }
}