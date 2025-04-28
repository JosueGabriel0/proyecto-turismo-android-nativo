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
import pe.edu.upeu.turismo_kotlin.modelo.CategoriaDto
import pe.edu.upeu.turismo_kotlin.modelo.CategoriaResponse
import pe.edu.upeu.turismo_kotlin.repository.CategoriaRepository
import pe.edu.upeu.turismo_kotlin.repository.FileRepository
import javax.inject.Inject

@HiltViewModel
class AdminCategoriaViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository,
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _categorias = MutableStateFlow<List<CategoriaResponse>>(emptyList())
    val categorias: StateFlow<List<CategoriaResponse>> = _categorias.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Obtener todas las categorías
    fun obtenerCategorias() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = categoriaRepository.getCategorias()
                _categorias.value = response
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Crear una nueva categoría
    fun crearCategoria(categoriaDto: CategoriaDto, file: MultipartBody.Part? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val nuevaCategoria = categoriaRepository.postCategoria(categoriaDto, file)
                nuevaCategoria?.let {
                    _categorias.value = _categorias.value + it
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Actualizar una categoría
    fun actualizarCategoria(idCategoria: Long, categoriaDto: CategoriaDto, file: MultipartBody.Part? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("ACTUALIZAR", "Intentando actualizar categoría con ID=$idCategoria")
                val actualizada = categoriaRepository.putCategoria(idCategoria, categoriaDto, file)
                actualizada?.let {
                    Log.d("ACTUALIZAR", "Categoría actualizada exitosamente: $it")
                    _categorias.value = _categorias.value.map { categoria ->
                        if (categoria.idCategoria == idCategoria) it else categoria
                    }
                } ?: run {
                    Log.e("ACTUALIZAR", "La respuesta fue nula al actualizar")
                }
            } catch (e: Exception) {
                Log.e("ACTUALIZAR", "Error al actualizar categoría: ${e.localizedMessage}")
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Eliminar una categoría
    fun eliminarCategoria(idCategoria: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("ELIMINAR", "Intentando eliminar categoría con ID=$idCategoria")
                val eliminado = categoriaRepository.deleteCategoria(idCategoria)
                if (eliminado) {
                    Log.d("ELIMINAR", "Categoría eliminada exitosamente")
                    _categorias.value = _categorias.value.filter { it.idCategoria != idCategoria }
                } else {
                    Log.e("ELIMINAR", "No se pudo eliminar la categoría")
                }
            } catch (e: Exception) {
                Log.e("ELIMINAR", "Error al eliminar categoría: ${e.localizedMessage}")
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Descargar imagen de una categoría
    fun obtenerImagenCategoria(fileName: String, onSuccess: (ResponseBody) -> Unit, onError: (String) -> Unit) {
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