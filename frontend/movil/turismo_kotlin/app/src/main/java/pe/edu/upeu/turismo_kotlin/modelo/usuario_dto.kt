package pe.edu.upeu.turismo_kotlin.modelo

import com.google.gson.Gson
import java.time.LocalDate

data class UsuarioDto (
    var username: String? = null,
    var password: String? = null,
    var estadoCuenta: String? = null,
    var nombreRol: String? = null,
    var nombres: String? = null,
    var apellidos: String? = null,
    var tipoDocumento: String? = null,
    var numeroDocumento: String? = null,
    var telefono: String? = null,
    var direccion: String? = null,
    var correoElectronico: String? = null,
    var fotoPerfil: String? = null,
    var fechaNacimiento: String? = null,
){
    fun toJson(): String {
        return Gson().toJson(this)
    }
}