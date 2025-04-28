package pe.edu.upeu.turismo_kotlin.modelo

import java.time.LocalDate
import java.time.LocalDateTime

data class LugarTuristicoResponse(
    val idLugar: Long,
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    val imagenUrl: String,
    val categorias: List<CategoriaResponse>,
    val resenas: List<ResenaResponse>,
    val fechaCreacionLugarTuristico: String,
    val fechaModificacionLugarTuristico: String
)

data class CategoriaResponse(
    val idCategoria: Long,
    val nombre: String,
    val lugarTuristico: String,
    val emprendimientos: List<EmprendimientoResponse>,
    val fechaCreacionCategoria: String,
    val fechaModificacionCategoria: String
)

data class EmprendimientoResponse(
    val idEmprendimiento: Long,
    val nombre: String,
    val descripcion: String,
    val contacto: String,
    val imagenUrl: String,
    val categoria: String,
    val hotel: HotelResponse,
    val resenas: List<String>,
    val fechaCreacionEmprendimiento: String,
    val fechaModificacionEmprendimiento: String
)

data class HotelResponse(
    val idHotel: Long,
    val nombre: String,
    val descripcion: String,
    val direccion: String,
    val contacto: String,
    val capacidadTotal: Int,
    val emprendimiento: String,
    val reservas: List<ReservaResponse>,
    val fechaCreacionHotel: String,
    val fechaModificacionHotel: String
)

data class ReservaResponse(
    val idReserva: Long,
    val hotel: String,
    val nombreCliente: String,
    val emailCliente: String,
    val fechaCheckin: LocalDate,
    val fechaCheckout: LocalDate,
    val fechaReserva: LocalDate,
    val estado: String, // puede ser un enum en el futuro
    val fechaCreacionReserva: String,
    val fechaModificacionReserva: String
)

data class ResenaResponse(
    val idResena: Long,
    val nombreUsuario: String,
    val comentario: String,
    val calificacion: Int,
    val fecha: LocalDate,
    val lugar: String,
    val emprendimiento: EmprendimientoResponse?,
    val fechaCreacionResena: String,
    val fechaModificacionResena: String
)