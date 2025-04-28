package pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import pe.edu.upeu.turismo_kotlin.modelo.LugarTuristicoDto
import pe.edu.upeu.turismo_kotlin.modelo.LugarTuristicoResponse
import pe.edu.upeu.turismo_kotlin.repository.FileRepository
import pe.edu.upeu.turismo_kotlin.repository.LugarTuristicoRepository
import javax.inject.Inject

@HiltViewModel
class AdminLugarTuristicoViewModel @Inject constructor(
    private val lugarTuristicoRepository: LugarTuristicoRepository,
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _lugaresTuristicos = MutableStateFlow<List<LugarTuristicoResponse>>(emptyList())
    val lugaresTuristicos: StateFlow<List<LugarTuristicoResponse>> = _lugaresTuristicos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Obtener todos los lugares turísticos
    fun obtenerLugaresTuristicos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = lugarTuristicoRepository.getLugaresTuristicos()
                _lugaresTuristicos.value = response
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Crear un nuevo lugar turístico
    fun crearLugarTuristico(lugarTuristicoDto: LugarTuristicoDto, file: MultipartBody.Part? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val nuevoLugar = lugarTuristicoRepository.postLugarTuristico(lugarTuristicoDto, file)
                nuevoLugar?.let {
                    _lugaresTuristicos.value = _lugaresTuristicos.value + it
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Actualizar un lugar turístico
    fun actualizarLugarTuristico(idLugarTuristico: Long, lugarTuristicoDto: LugarTuristicoDto, file: MultipartBody.Part? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("ACTUALIZAR", "Intentando actualizar lugar con ID=$idLugarTuristico")
                val actualizado = lugarTuristicoRepository.putLugarTuristico(idLugarTuristico, lugarTuristicoDto, file)
                actualizado?.let {
                    Log.d("ACTUALIZAR", "Lugar actualizado exitosamente: $it")
                    _lugaresTuristicos.value = _lugaresTuristicos.value.map { lugar ->
                        if (lugar.idLugar == idLugarTuristico) it else lugar
                    }
                } ?: run {
                    Log.e("ACTUALIZAR", "La respuesta fue nula al actualizar")
                }
            } catch (e: Exception) {
                Log.e("ACTUALIZAR", "Error al actualizar lugar: ${e.localizedMessage}")
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Eliminar un lugar turístico
    fun eliminarLugarTuristico(idLugarTuristico: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("ELIMINAR", "Intentando eliminar lugar con ID=$idLugarTuristico")
                val eliminado = lugarTuristicoRepository.deleteLugarTuristico(idLugarTuristico)
                if (eliminado) {
                    Log.d("ELIMINAR", "Lugar eliminado exitosamente")
                    _lugaresTuristicos.value = _lugaresTuristicos.value.filter { it.idLugar != idLugarTuristico }
                } else {
                    Log.e("ELIMINAR", "No se pudo eliminar el lugar")
                }
            } catch (e: Exception) {
                Log.e("ELIMINAR", "Error al eliminar lugar: ${e.localizedMessage}")
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Descargar imagen de un lugar turístico
    fun obtenerImagenLugar(fileName: String, onSuccess: (ResponseBody) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = fileRepository.getImage(fileName)
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    } ?: onError("No se encontró la imagen")
                } else {
                    onError("Error al descargar la imagen")
                }
            } catch (e: Exception) {
                onError("Error de conexión: ${e.localizedMessage}")
            }
        }
    }
}