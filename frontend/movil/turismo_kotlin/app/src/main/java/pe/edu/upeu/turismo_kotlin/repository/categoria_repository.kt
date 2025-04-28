package pe.edu.upeu.turismo_kotlin.repository

import pe.edu.upeu.turismo_kotlin.data.remote.RestCategoria
import pe.edu.upeu.turismo_kotlin.modelo.CategoriaResponse
import pe.edu.upeu.turismo_kotlin.modelo.CategoriaDto
import pe.edu.upeu.turismo_kotlin.utils.TokenUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

interface CategoriaRepository {
    suspend fun getCategorias(): List<CategoriaResponse>
    suspend fun getCategoriaById(idCategoria: Long): CategoriaResponse?
    suspend fun postCategoria(categoriaDto: CategoriaDto, file: MultipartBody.Part?): CategoriaResponse?
    suspend fun putCategoria(idCategoria: Long, categoriaDto: CategoriaDto, file: MultipartBody.Part?): CategoriaResponse?
    suspend fun deleteCategoria(idCategoria: Long): Boolean
}

class CategoriaRepositoryImpl @Inject constructor(
    private val restCategoria: RestCategoria
) : CategoriaRepository {

    override suspend fun getCategorias(): List<CategoriaResponse> {
        val response = restCategoria.getCategorias(TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() ?: emptyList()
        else emptyList()
    }

    override suspend fun getCategoriaById(idCategoria: Long): CategoriaResponse? {
        val response = restCategoria.getCategoriaById(idCategoria, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun postCategoria(
        categoriaDto: CategoriaDto,
        file: MultipartBody.Part?
    ): CategoriaResponse? {
        val jsonCategoria = RequestBody.create(MultipartBody.FORM, categoriaDto.toJson())
        val response = restCategoria.postCategoria(jsonCategoria, file, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun putCategoria(
        idCategoria: Long,
        categoriaDto: CategoriaDto,
        file: MultipartBody.Part?
    ): CategoriaResponse? {
        val jsonCategoria = RequestBody.create(MultipartBody.FORM, categoriaDto.toJson())
        val response = restCategoria.putCategoria(idCategoria, jsonCategoria, file, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun deleteCategoria(idCategoria: Long): Boolean {
        val response = restCategoria.deleteCategoria(idCategoria, TokenUtils.TOKEN_CONTENT)
        return response.isSuccessful
    }
}