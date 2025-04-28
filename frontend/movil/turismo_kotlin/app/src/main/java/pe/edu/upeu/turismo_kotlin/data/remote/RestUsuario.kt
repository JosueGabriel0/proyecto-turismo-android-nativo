package pe.edu.upeu.turismo_kotlin.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.edu.upeu.turismo_kotlin.modelo.UsuarioResponse
import retrofit2.Response
import retrofit2.http.*

interface RestUsuario {

    // Obtener todos los usuarios completos
    @GET("/admin/usuarioCompleto")
    suspend fun getUsuariosCompleto(
        @Header("Authorization") token: String  // Añadimos el token en el header
    ): Response<List<UsuarioResponse>>

    // Obtener un usuario completo por ID
    @GET("/admin/usuarioCompleto/{idUsuario}")
    suspend fun getUsuarioCompletoById(
        @Path("idUsuario") idUsuario: Long,
        @Header("Authorization") token: String  // Añadimos el token en el header
    ): Response<UsuarioResponse>

    // Crear un nuevo usuario completo
    @POST("/admin/usuarioCompleto")
    @Multipart
    suspend fun postUsuarioCompleto(
        @Part("usuario") usuarioJson: RequestBody,  // El JSON del usuario
        @Part file: MultipartBody.Part?,  // El archivo de foto de perfil
        @Header("Authorization") token: String  // Añadimos el token en el header
    ): Response<UsuarioResponse>

    // Actualizar un usuario completo
    @PUT("/admin/usuarioCompleto/{idUsuario}")
    @Multipart
    suspend fun putUsuarioCompleto(
        @Path("idUsuario") idUsuario: Long,
        @Part("usuario") usuarioJson: RequestBody,  // El JSON con los nuevos datos del usuario
        @Part file: MultipartBody.Part?,  // El archivo de foto de perfil (opcional)
        @Header("Authorization") token: String  // Añadimos el token en el header
    ): Response<UsuarioResponse>

    // Eliminar un usuario completo por ID
    @DELETE("/admin/usuarioCompleto/{idUsuario}")
    suspend fun deleteUsuarioCompleto(
        @Path("idUsuario") idUsuario: Long,
        @Header("Authorization") token: String  // Añadimos el token en el header
    ): Response<Unit>
}