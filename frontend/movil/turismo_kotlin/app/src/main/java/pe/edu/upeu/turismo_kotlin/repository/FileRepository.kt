package pe.edu.upeu.turismo_kotlin.repository

import okhttp3.ResponseBody
import pe.edu.upeu.turismo_kotlin.data.remote.RestFile
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface FileRepository {
    suspend fun getImage(fileName: String): Response<ResponseBody>
}

class FileRepositoryImpl @Inject constructor(
    private val restFile: RestFile
) : FileRepository {

    override suspend fun getImage(fileName: String): Response<ResponseBody> {
        return try {
            withContext(Dispatchers.IO) {   // <--- SOLUCIÓN: mover a hilo IO
                val call: Call<ResponseBody> = restFile.downloadFile(fileName)
                call.execute()  // ya no hay problema porque ahora está en hilo IO
            }
        } catch (e: Exception) {
            Log.e("FileRepository", "Excepción al descargar la imagen: ${e.message}")
            Response.error(500, ResponseBody.create(null, "Error al descargar la imagen"))
        }
    }
}
