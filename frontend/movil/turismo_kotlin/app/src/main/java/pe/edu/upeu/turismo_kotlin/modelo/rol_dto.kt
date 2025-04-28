package pe.edu.upeu.turismo_kotlin.modelo

data class RolDto(
    val nombre: String
) {
    fun toJson(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre
        )
    }
}
