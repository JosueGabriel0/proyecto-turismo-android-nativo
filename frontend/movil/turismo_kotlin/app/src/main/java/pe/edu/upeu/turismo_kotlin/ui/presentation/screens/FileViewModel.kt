package pe.edu.upeu.turismo_kotlin.ui.presentation.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import pe.edu.upeu.turismo_kotlin.repository.FileRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val imageBitmap: StateFlow<Bitmap?> get() = _imageBitmap

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun getImage(fileName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response: Response<ResponseBody> = fileRepository.getImage(fileName)
                if (response.isSuccessful) {
                    // Validamos el tipo de contenido de la respuesta
                    val contentType = response.headers()["Content-Type"]
                    if (contentType != null && contentType.startsWith("image/")) {
                        // Es una imagen, procesamos los bytes
                        response.body()?.let { body ->
                            val bytes = body.bytes()
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            _imageBitmap.value = bitmap
                        } ?: run {
                            _errorMessage.value = "Imagen vacía"
                        }
                    } else {
                        // No es una imagen, mostramos un error
                        _errorMessage.value = "El archivo descargado no es una imagen"
                    }
                } else {
                    _errorMessage.value = "Error en descarga: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Excepción: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}