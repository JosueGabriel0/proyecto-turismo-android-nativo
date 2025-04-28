package pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import pe.edu.upeu.turismo_kotlin.modelo.UsuarioDto
import pe.edu.upeu.turismo_kotlin.modelo.UsuarioResponse
import pe.edu.upeu.turismo_kotlin.repository.FileRepository
import pe.edu.upeu.turismo_kotlin.repository.UsuarioRepository
import javax.inject.Inject

// Anotación Hilt para ViewModel
@HiltViewModel
class AdminUsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<UsuarioResponse>>(emptyList())
    val usuarios = _usuarios.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Función para obtener usuarios
    fun obtenerUsuarios() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val usuariosResponse = usuarioRepository.getUsuariosCompleto()
                _usuarios.value = usuariosResponse
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para crear usuario
    fun crearUsuario(usuarioDto: UsuarioDto, file: MultipartBody.Part? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val usuarioCreado = usuarioRepository.postUsuarioCompleto(usuarioDto, file)
                // Actualizar lista de usuarios con el nuevo usuario creado
                usuarioCreado?.let {
                    _usuarios.value = _usuarios.value + it
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para actualizar usuario
    fun actualizarUsuario(idUsuario: Long, usuarioDto: UsuarioDto, file: MultipartBody.Part? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val usuarioActualizado = usuarioRepository.putUsuarioCompleto(idUsuario, usuarioDto, file)
                // Actualizar lista de usuarios con el usuario actualizado
                usuarioActualizado?.let {
                    _usuarios.value = _usuarios.value.map { usuario ->
                        if (usuario.idUsuario == idUsuario) it else usuario
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para eliminar usuario
    fun eliminarUsuario(idUsuario: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val eliminado = usuarioRepository.deleteUsuarioCompleto(idUsuario)
                if (eliminado) {
                    _usuarios.value = _usuarios.value.filter { it.idUsuario != idUsuario }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun obtenerImagenPerfil(fileName: String, onSuccess: (ResponseBody) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = fileRepository.getImage(fileName)
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)  // Llamar el callback con la respuesta exitosa
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