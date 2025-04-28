package pe.edu.upeu.turismo_kotlin.modelo

import com.google.gson.Gson

data class LugarTuristicoDto(
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    val imagenUrl: String
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}