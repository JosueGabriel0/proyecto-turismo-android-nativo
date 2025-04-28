package pe.edu.upeu.turismo_kotlin.modelo

import java.time.LocalDate
import java.time.LocalDateTime

data class CategoriaResponse(
    val idCategoria: Long,
    val nombre: String,
    val imagenUrl: String,
    val lugarTuristico: LugarTuristicoResponse,
    val emprendimientos: List<EmprendimientoResponse>,
    val fechaCreacionCategoria: String,
    val fechaModificacionCategoria: String
)