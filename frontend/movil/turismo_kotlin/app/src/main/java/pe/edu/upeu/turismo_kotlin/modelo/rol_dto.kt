package pe.edu.upeu.turismo_kotlin.modelo

//"Rol es una clase (RolDto) que representa un rol de usuario en el sistema.
//Tiene un atributo nombre (tipo String) que guarda el nombre del rol.
data class RolDto(
    val nombre: String
) {
    fun toJson(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre
        )
    }
}
