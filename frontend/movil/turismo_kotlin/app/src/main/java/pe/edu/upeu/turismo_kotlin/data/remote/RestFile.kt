package pe.edu.upeu.turismo_kotlin.data.remote

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RestFile {
    @GET("/filePerfil/file/{fileName}")
    fun downloadFile(@Path("fileName") fileName: String): Call<ResponseBody>
}
