package pe.edu.upeu.turismo_kotlin.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.edu.upeu.turismo_kotlin.modelo.CategoriaResponse
import retrofit2.Response
import retrofit2.http.*

interface RestCategoria {

    // Obtener todas las categorías
    @GET("/admin/categoria")
    suspend fun getCategorias(
        @Header("Authorization") token: String
    ): Response<List<CategoriaResponse>>

    // Obtener una categoría por ID
    @GET("/admin/categoria/{idCategoria}")
    suspend fun getCategoriaById(
        @Path("idCategoria") idCategoria: Long,
        @Header("Authorization") token: String
    ): Response<CategoriaResponse>

    // Crear una nueva categoría
    @POST("/admin/categoria")
    @Multipart
    suspend fun postCategoria(
        @Part("categoria") categoriaJson: RequestBody,
        @Part file: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): Response<CategoriaResponse>

    // Actualizar una categoría existente
    @PUT("/admin/categoria/{idCategoria}")
    @Multipart
    suspend fun putCategoria(
        @Path("idCategoria") idCategoria: Long,
        @Part("categoria") categoriaJson: RequestBody,
        @Part file: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): Response<CategoriaResponse>

    // Eliminar una categoría
    @DELETE("/admin/categoria/{idCategoria}")
    suspend fun deleteCategoria(
        @Path("idCategoria") idCategoria: Long,
        @Header("Authorization") token: String
    ): Response<Unit>
}