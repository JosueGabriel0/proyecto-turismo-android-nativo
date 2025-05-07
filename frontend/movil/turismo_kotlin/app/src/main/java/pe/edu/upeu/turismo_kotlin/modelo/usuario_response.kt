package pe.edu.upeu.turismo_kotlin.modelo

data class UsuarioResponse(
    val idUsuario: Long,  // ID del usuario es un entero
    val username: String,
    val password: String,
    val estado: String,  // El estado es un String (puedes usar un Enum si tiene valores fijos)
    val rol: RolDto,  // Rol es un objeto, no String
    val persona: Persona,  // Persona es un objeto, no String
    val bitacoraAccesoList: List<Any>,  // BitacoraAcceso es una clase (ajusta según sea necesario)
    val noticias: List<Any>,  // Noticias es una lista de objetos Noticia
    val fechaCreacionUsuario: String,  // Usamos Date para fechas
    val fechaModificacionUsuario: String?,  // Nullable, ya que puede ser null
)

data class Persona(
    val idPersona: Long,
    val nombres: String,
    val apellidos: String,
    val tipoDocumento: String,
    val numeroDocumento: String,
    val telefono: String,
    val direccion: String,
    val correoElectronico: String,
    val fotoPerfil: String?,  // Foto de perfil es nullable (puede ser null)
    val fechaNacimiento: String,  // Usamos Date para la fecha de nacimiento
    val fechaCreacionPersona: String,  // Fecha de creación como Date
    val fechaModificacionPersona: String?  // Nullable, porque puede no haber modificación
)