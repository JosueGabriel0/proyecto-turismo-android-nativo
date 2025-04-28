package pe.edu.upeu.turismo_kotlin.repository

import pe.edu.upeu.turismo_kotlin.data.remote.RestLugarTuristico
import pe.edu.upeu.turismo_kotlin.modelo.LugarTuristicoResponse
import pe.edu.upeu.turismo_kotlin.utils.TokenUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.edu.upeu.turismo_kotlin.modelo.LugarTuristicoDto
import retrofit2.Response
import javax.inject.Inject

interface LugarTuristicoRepository {
    suspend fun getLugaresTuristicos(): List<LugarTuristicoResponse>
    suspend fun getLugarTuristicoById(idLugarTuristico: Long): LugarTuristicoResponse?
    suspend fun postLugarTuristico(lugarTuristicoDto: LugarTuristicoDto, file: MultipartBody.Part?): LugarTuristicoResponse?
    suspend fun putLugarTuristico(idLugarTuristico: Long, lugarTuristicoDto: LugarTuristicoDto, file: MultipartBody.Part?): LugarTuristicoResponse?
    suspend fun deleteLugarTuristico(idLugarTuristico: Long): Boolean
}

class LugarTuristicoRepositoryImpl @Inject constructor(
    private val restLugarTuristico: RestLugarTuristico
) : LugarTuristicoRepository {

    override suspend fun getLugaresTuristicos(): List<LugarTuristicoResponse> {
        val response = restLugarTuristico.getLugaresTuristicos(TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() ?: emptyList()
        else emptyList()
    }

    override suspend fun getLugarTuristicoById(idLugarTuristico: Long): LugarTuristicoResponse? {
        val response = restLugarTuristico.getLugarTuristicoById(idLugarTuristico, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun postLugarTuristico(
        lugarTuristicoDto: LugarTuristicoDto,
        file: MultipartBody.Part?
    ): LugarTuristicoResponse? {
        val jsonLugarTuristico = RequestBody.create(MultipartBody.FORM, lugarTuristicoDto.toJson())
        val response = restLugarTuristico.postLugarTuristico(jsonLugarTuristico, file, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun putLugarTuristico(
        idLugarTuristico: Long,
        lugarTuristicoDto: LugarTuristicoDto,
        file: MultipartBody.Part?
    ): LugarTuristicoResponse? {
        val jsonLugarTuristico = RequestBody.create(MultipartBody.FORM, lugarTuristicoDto.toJson())
        val response = restLugarTuristico.putLugarTuristico(idLugarTuristico, jsonLugarTuristico, file, TokenUtils.TOKEN_CONTENT)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun deleteLugarTuristico(idLugarTuristico: Long): Boolean {
        val response = restLugarTuristico.deleteLugarTuristico(idLugarTuristico, TokenUtils.TOKEN_CONTENT)
        return response.isSuccessful
    }
}