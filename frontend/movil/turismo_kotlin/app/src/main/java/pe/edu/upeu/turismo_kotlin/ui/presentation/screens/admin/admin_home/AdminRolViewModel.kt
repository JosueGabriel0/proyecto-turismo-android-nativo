package pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.turismo_kotlin.modelo.RolDto
import pe.edu.upeu.turismo_kotlin.modelo.RolResponse
import pe.edu.upeu.turismo_kotlin.repository.RolRepository
import javax.inject.Inject

@HiltViewModel
class AdminRolViewModel @Inject constructor(private val rolRepository: RolRepository) : ViewModel() {

    // Usamos StateFlow para que las composables puedan observar los cambios.
    private val _roles = MutableStateFlow<List<RolResponse>>(emptyList())
    val roles: StateFlow<List<RolResponse>> = _roles

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Obtener todos los roles
    fun fetchRoles() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val rolesResponse = rolRepository.getRoles()
                _roles.value = rolesResponse // Lista de roles obtenida
            } catch (e: Exception) {
                _errorMessage.value = e.message // Manejo de error si la llamada falla
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Obtener un rol por su ID
    fun fetchRoleById(idRol: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val rol = rolRepository.getRolById(idRol)
                rol?.let {
                    // Maneja el rol obtenido (puedes usarlo para mostrar en UI)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message // Manejo de error si la llamada falla
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Crear un nuevo rol
    fun createRole(rolDto: RolDto) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val createdRole = rolRepository.postRol(rolDto)
                createdRole?.let {
                    fetchRoles() // Recarga la lista de roles después de agregar uno nuevo
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message // Manejo de error si la llamada falla
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Actualizar un rol existente
    fun updateRole(idRol: Int, rolDto: RolDto) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val updatedRole = rolRepository.putRol(idRol, rolDto)
                updatedRole?.let {
                    fetchRoles() // Recarga la lista de roles después de actualizar uno
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message // Manejo de error si la llamada falla
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Eliminar un rol
    fun deleteRole(idRol: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val isDeleted = rolRepository.deleteRol(idRol)
                if (isDeleted) {
                    fetchRoles() // Recarga la lista de roles después de eliminar uno
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message // Manejo de error si la llamada falla
            } finally {
                _isLoading.value = false
            }
        }
    }
}