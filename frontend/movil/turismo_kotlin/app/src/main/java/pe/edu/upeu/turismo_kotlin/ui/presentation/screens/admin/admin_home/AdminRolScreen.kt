package pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upeu.turismo_kotlin.modelo.RolDto
import pe.edu.upeu.turismo_kotlin.modelo.RolResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRolScreen(
    viewModel: AdminRolViewModel = hiltViewModel()
) {

    // Aquí observamos los estados directamente desde el ViewModel
    val roles by viewModel.roles.collectAsState(initial = emptyList())  // roles es una lista
    val isLoading by viewModel.isLoading.collectAsState(initial = false)  // isLoading es un booleano
    val errorMessage by viewModel.errorMessage.collectAsState(initial = null)  // errorMessage es un String?

    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf<RolResponse?>(null) }
    var roleName by remember { mutableStateOf("") }

    // Fetch roles cuando la pantalla se inicializa
    LaunchedEffect(Unit) {
        viewModel.fetchRoles()
    }

    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogOpen = true },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Rol")
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp) // Aplicamos el padding que viene del Scaffold
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        roles.forEach { role ->
                            RoleItem(
                                role = role,
                                onEdit = {
                                    selectedRole = role
                                    roleName = role.nombre
                                    isDialogOpen = true
                                },
                                onDelete = {
                                    viewModel.deleteRole(role.idRol)
                                }
                            )
                        }
                    }
                }

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                    )
                }
            }
        }
    )

    if (isDialogOpen) {
        AddOrUpdateRoleDialog(
            initialName = roleName,
            onDismiss = { isDialogOpen = false },
            onSave = {
                if (selectedRole == null) {
                    viewModel.createRole(RolDto(it))
                } else {
                    selectedRole?.let { role ->
                        viewModel.updateRole(role.idRol, RolDto(it))
                    }
                }
                isDialogOpen = false
            }
        )
    }
}

@Composable
fun RoleItem(role: RolResponse, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = role.nombre)
        Row {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Rol")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Rol")
            }
        }
    }
}

@Composable
fun AddOrUpdateRoleDialog(
    initialName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var roleName by remember { mutableStateOf(initialName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialName.isEmpty()) "Agregar Nuevo Rol" else "Actualizar Rol") },
        text = {
            TextField(
                value = roleName,
                onValueChange = { roleName = it },
                label = { Text("Nombre del Rol") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(roleName)
                    roleName = ""
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}