package pe.edu.upeu.turismo_kotlin.data.remote

import pe.edu.upeu.turismo_kotlin.modelo.RolDto
import pe.edu.upeu.turismo_kotlin.modelo.RolResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RestRol {

    @GET("/admin/rol")  // URL de la API que devuelve todos los roles
    suspend fun getRoles(
        @Header("Authorization") token: String  // Aquí añadimos el token en el header
    ): Response<List<RolResponse>>

    @GET("/admin/rol/{idRol}")  // URL de la API para obtener un rol por su ID
    suspend fun getRolById(
        @Path("idRol") idRol: Int,
        @Header("Authorization") token: String  // Aquí añadimos el token en el header
    ): Response<RolResponse>

    @POST("/admin/rol")  // URL para agregar un nuevo rol
    suspend fun postRol(
        @Body rolDto: RolDto,
        @Header("Authorization") token: String  // Aquí añadimos el token en el header
    ): Response<RolResponse>

    @PUT("/admin/rol/{idRol}")  // URL para actualizar un rol
    suspend fun putRol(
        @Path("idRol") idRol: Int,
        @Body rolDto: RolDto,  // El rolDto es el objeto que contiene los nuevos datos del rol
        @Header("Authorization") token: String  // Aquí añadimos el token en el header
    ): Response<RolResponse>

    @DELETE("/admin/rol/{idRol}")  // URL para eliminar un rol
    suspend fun deleteRol(
        @Path("idRol") idRol: Int,
        @Header("Authorization") token: String  // Aquí añadimos el token en el header
    ): Response<Unit>
}