package pe.edu.upeu.turismo_kotlin.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.edu.upeu.turismo_kotlin.modelo.LugarTuristicoResponse
import retrofit2.Response
import retrofit2.http.*

interface RestLugarTuristico {

    // Obtener todos los lugares turísticos
    @GET("/admin/lugarTuristico")
    suspend fun getLugaresTuristicos(
        @Header("Authorization") token: String
    ): Response<List<LugarTuristicoResponse>>

    // Obtener un lugar turístico por ID
    @GET("/admin/lugarTuristico/{idLugarTuristico}")
    suspend fun getLugarTuristicoById(
        @Path("idLugarTuristico") idLugarTuristico: Long,
        @Header("Authorization") token: String
    ): Response<LugarTuristicoResponse>

    // Crear un nuevo lugar turístico
    @POST("/admin/lugarTuristico")
    @Multipart
    suspend fun postLugarTuristico(
        @Part("lugarTuristico") lugarTuristicoJson: RequestBody,
        @Part file: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): Response<LugarTuristicoResponse>

    // Actualizar un lugar turístico
    @PUT("/admin/lugarTuristico/{idLugarTuristico}")
    @Multipart
    suspend fun putLugarTuristico(
        @Path("idLugarTuristico") idLugarTuristico: Long,
        @Part("lugarTuristico") lugarTuristicoJson: RequestBody,
        @Part file: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): Response<LugarTuristicoResponse>

    // Eliminar un lugar turístico
    @DELETE("/admin/lugarTuristico/{idLugarTuristico}")
    suspend fun deleteLugarTuristico(
        @Path("idLugarTuristico") idLugarTuristico: Long,
        @Header("Authorization") token: String
    ): Response<Unit>
}