package pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home

import pe.edu.upeu.turismo_kotlin.R;
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import pe.edu.upeu.turismo_kotlin.modelo.RolResponse
import pe.edu.upeu.turismo_kotlin.modelo.UsuarioDto
import pe.edu.upeu.turismo_kotlin.modelo.UsuarioResponse
import java.io.File
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsuarioScreen(
    viewModel: AdminUsuarioViewModel = hiltViewModel(),
    adminRolViewModel: AdminRolViewModel = hiltViewModel() // Crear instancia de AdminRolViewModel
) {

    val roles by adminRolViewModel.roles.collectAsState() // Observa la lista de roles
    val isLoadingRoles by adminRolViewModel.isLoading.collectAsState()

    // Aquí observamos los estados directamente desde el ViewModel
    val usuarios by viewModel.usuarios.collectAsState(initial = emptyList())  // usuarios es una lista
    val isLoading by viewModel.isLoading.collectAsState(initial = false)  // isLoading es un booleano
    val errorMessage by viewModel.errorMessage.collectAsState(initial = null)  // errorMessage es un String?

    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<UsuarioResponse?>(null) }
    var userName by remember { mutableStateOf("") }

    // Fetch usuarios cuando la pantalla se inicializa
    LaunchedEffect(Unit) {
        viewModel.obtenerUsuarios()
        adminRolViewModel.fetchRoles()
    }

    // Mostrar loading mientras se cargan los roles
    if (isLoadingRoles) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
    }

    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogOpen = true },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Usuario")
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
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        usuarios.forEach { user ->
                            UsuarioItem(
                                user = user,
                                viewModel = viewModel, // Pasar el viewModel aquí
                                onEdit = {
                                    selectedUser = user
                                    userName = user.persona.nombres ?: ""
                                    isDialogOpen = true
                                },
                                onDelete = {
                                    viewModel.eliminarUsuario(user.idUsuario)
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
        AddOrUpdateUserDialog(
            initialUser = selectedUser?.let {
                UsuarioDto(
                    username = it.username,
                    password = it.password,
                    estadoCuenta = it.estado,
                    nombreRol = it.rol.nombre,
                    nombres = it.persona.nombres,
                    apellidos = it.persona.apellidos,
                    tipoDocumento = it.persona.tipoDocumento,
                    numeroDocumento = it.persona.numeroDocumento,
                    telefono = it.persona.telefono,
                    direccion = it.persona.direccion,
                    correoElectronico = it.persona.correoElectronico,
                    fotoPerfil = it.persona.fotoPerfil,
                    fechaNacimiento = it.persona.fechaNacimiento
                )
            } ?: UsuarioDto(),
            roles = roles,
            onDismiss = { isDialogOpen = false },
            onSave = { usuarioDto, fotoPerfilPart ->
                if (selectedUser == null) {
                    // Si no hay un usuario seleccionado, creamos uno nuevo
                    viewModel.crearUsuario(usuarioDto, fotoPerfilPart)
                } else {
                    // Si hay un usuario seleccionado, lo actualizamos
                    selectedUser?.let { user ->
                        viewModel.actualizarUsuario(user.idUsuario, usuarioDto, fotoPerfilPart)
                    }
                }
                isDialogOpen = false
            }
        )
    }
}

@Composable
fun UsuarioItem(user: UsuarioResponse, onEdit: () -> Unit, onDelete: () -> Unit, viewModel: AdminUsuarioViewModel) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageLoading by remember { mutableStateOf(true) }

    // Cargar la imagen del perfil cuando el usuario esté disponible
    LaunchedEffect(user.persona.fotoPerfil) {
        viewModel.obtenerImagenPerfil(
            user.persona.fotoPerfil ?: "",
            onSuccess = { imageResponse ->
                try {
                    val bytes = imageResponse.bytes() // Descargamos todo como byteArray
                    if (bytes.isNotEmpty()) {
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        if (bitmap != null) {
                            imageBitmap = bitmap
                        } else {
                            Log.e("UsuarioItem", "Bitmap decoding failed (bitmap is null)")
                        }
                    } else {
                        Log.e("UsuarioItem", "Downloaded image bytes are empty")
                    }
                } catch (e: Exception) {
                    Log.e("UsuarioItem", "Error processing image: ${e.localizedMessage}")
                } finally {
                    imageLoading = false
                }
            },
            onError = {
                Log.e("UsuarioItem", "Error loading image: $it")
                imageLoading = false
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (imageLoading) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
        } else {
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                        .clip(CircleShape)
                )
            } ?: run {
                Icon(
                    painter = painterResource(id = R.drawable.imagen_bienvenida1),
                    contentDescription = "Usuario sin foto",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                        .clip(CircleShape)
                )
            }
        }

        Text(text = user.persona.nombres ?: "Sin nombre")

        Row {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Usuario")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Usuario")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddOrUpdateUserDialog(
    initialUser: UsuarioDto,
    roles: List<RolResponse>,
    onDismiss: () -> Unit,
    onSave: (UsuarioDto, MultipartBody.Part?) -> Unit
) {

    val context = LocalContext.current

    var username by remember { mutableStateOf(initialUser.username ?: "") }
    var password by remember { mutableStateOf(initialUser.password ?: "") }
    var estadoCuenta by remember { mutableStateOf(initialUser.estadoCuenta ?: "") }
    var nombreRol by remember { mutableStateOf(initialUser.nombreRol ?: "") }
    var nombres by remember { mutableStateOf(initialUser.nombres ?: "") }
    var apellidos by remember { mutableStateOf(initialUser.apellidos ?: "") }
    var tipoDocumento by remember { mutableStateOf(initialUser.tipoDocumento ?: "") }
    var numeroDocumento by remember { mutableStateOf(initialUser.numeroDocumento ?: "") }
    var telefono by remember { mutableStateOf(initialUser.telefono ?: "") }
    var direccion by remember { mutableStateOf(initialUser.direccion ?: "") }
    var correoElectronico by remember { mutableStateOf(initialUser.correoElectronico ?: "") }
    var fotoPerfilUri: Uri? by remember { mutableStateOf(null) }
    var fechaNacimiento by remember { mutableStateOf(initialUser.fechaNacimiento ?: "") }

    val estadoCuentaOptions = listOf("ACTIVO", "INACTIVO", "BLOQUEADO")
    var expandedRol by remember { mutableStateOf(false) }

    // Permissions for gallery and camera
    val permissionState = rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    // Select Image from Gallery
    val openGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { fotoPerfilUri = it }
    }

    // Take Picture from Camera
    val openCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Handle the image taken
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialUser.username.isNullOrEmpty()) "Agregar Nuevo Usuario" else "Actualizar Usuario") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nombre de Usuario") }
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") }
                )
                Text("Estado de Cuenta", style = MaterialTheme.typography.bodyLarge)
                estadoCuentaOptions.forEach { estado ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = estadoCuenta == estado,
                            onClick = { estadoCuenta = estado }
                        )
                        Text(estado, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Text("Nombre de Rol", style = MaterialTheme.typography.bodyLarge)
                roles.forEach { rol ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = nombreRol == rol.nombre,
                            onClick = { nombreRol = rol.nombre }
                        )
                        Text(rol.nombre, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                TextField(
                    value = nombres,
                    onValueChange = { nombres = it },
                    label = { Text("Nombres") }
                )
                TextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text("Apellidos") }
                )
                TextField(
                    value = tipoDocumento,
                    onValueChange = { tipoDocumento = it },
                    label = { Text("Tipo de Documento") }
                )
                TextField(
                    value = numeroDocumento,
                    onValueChange = { numeroDocumento = it },
                    label = { Text("Número de Documento") }
                )
                TextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") }
                )
                TextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") }
                )
                TextField(
                    value = correoElectronico,
                    onValueChange = { correoElectronico = it },
                    label = { Text("Correo Electrónico") }
                )

                // Button to select photo from gallery
                Button(onClick = {
                    openGalleryLauncher.launch("image/*")
                }) {
                    Text("Seleccionar Foto de Perfil")
                }

                // Display selected photo URI
                fotoPerfilUri?.let {
                    Text("Foto seleccionada: $it")
                }

                // Date of birth input
                TextField(
                    value = fechaNacimiento,
                    onValueChange = { fechaNacimiento = it },
                    label = { Text("Fecha de Nacimiento (yyyy-MM-dd)") }
                )
            }
        },
        confirmButton = {
            // Save the user with the selected photo
            Button(
                onClick = {
                    val fotoPerfilPart = fotoPerfilUri?.let {
                        val file = File(context.getRealPathFromURI(it))
                        val filePart = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody())
                        filePart
                    }
                    onSave(
                        UsuarioDto(
                            username = username,
                            password = password,
                            estadoCuenta = estadoCuenta,
                            nombreRol = nombreRol,
                            nombres = nombres,
                            apellidos = apellidos,
                            tipoDocumento = tipoDocumento,
                            numeroDocumento = numeroDocumento,
                            telefono = telefono,
                            direccion = direccion,
                            correoElectronico = correoElectronico,
                            fotoPerfil = fotoPerfilUri.toString(),
                            fechaNacimiento = fechaNacimiento
                        ),
                        fotoPerfilPart
                    )
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

fun Context.getRealPathFromURI(uri: Uri): String? {
    val cursor: Cursor? = this.contentResolver.query(uri, null, null, null, null)
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
    val filePath = columnIndex?.let { cursor.getString(it) }
    cursor?.close()
    return filePath
}
