package pe.edu.upeu.turismo_kotlin.modelo

import com.google.gson.Gson

data class CategoriaDto(
    val nombre: String,
    val nombreLugar: String
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}