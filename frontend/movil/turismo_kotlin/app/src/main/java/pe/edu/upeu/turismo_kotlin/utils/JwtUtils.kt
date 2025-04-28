package pe.edu.upeu.turismo_kotlin.utils

import android.util.Base64
import org.json.JSONObject

object JwtUtils {

    fun getRoleFromJwt(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payloadBytes = Base64.decode(parts[1], Base64.URL_SAFE)
            val payload = String(payloadBytes, Charsets.UTF_8)
            val jsonObject = JSONObject(payload)

            jsonObject.getString("role") // Asegúrate que en tu JWT el campo sea exactamente "role"
        } catch (e: Exception) {
            null
        }
    }

    fun getIdUsuarioFromJwt(token: String): Int? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payloadBytes = Base64.decode(parts[1], Base64.URL_SAFE)
            val payload = String(payloadBytes, Charsets.UTF_8)
            val jsonObject = JSONObject(payload)

            jsonObject.getInt("idUsuario")
        } catch (e: Exception) {
            null
        }
    }
}