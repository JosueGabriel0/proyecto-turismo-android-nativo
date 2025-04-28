package pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home

import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.database.Cursor
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import pe.edu.upeu.turismo_kotlin.R
import pe.edu.upeu.turismo_kotlin.modelo.LugarTuristicoDto
import pe.edu.upeu.turismo_kotlin.modelo.LugarTuristicoResponse
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLugarTuristicoScreen(
    viewModel: AdminLugarTuristicoViewModel = hiltViewModel()
) {
    val lugares by viewModel.lugaresTuristicos.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState(initial = false)
    val errorMessage by viewModel.errorMessage.collectAsState(initial = null)

    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedLugar by remember { mutableStateOf<LugarTuristicoResponse?>(null) }

    LaunchedEffect(Unit) {
        viewModel.obtenerLugaresTuristicos()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { isDialogOpen = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Lugar")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())) {
                    lugares.forEach { lugar ->
                        LugarItem(
                            lugar = lugar,
                            viewModel = viewModel,
                            onEdit = {
                                selectedLugar = lugar
                                isDialogOpen = true
                            },
                            onDelete = {
                                viewModel.eliminarLugarTuristico(lugar.idLugar)
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

    if (isDialogOpen) {
        AddOrUpdateLugarDialog(
            initialLugar = selectedLugar,
            onDismiss = { isDialogOpen = false },
            onSave = { lugarDto, imagePart ->
                if (selectedLugar == null) {
                    viewModel.crearLugarTuristico(lugarDto, imagePart)
                } else {
                    selectedLugar?.let { lugar ->
                        Log.d("ACTUALIZAR", "ID del lugar: ${lugar.idLugar}, ${lugar}")
                        viewModel.actualizarLugarTuristico(lugar.idLugar, lugarDto, imagePart)
                    }
                }
                isDialogOpen = false
            }
        )
    }
}

@Composable
fun LugarItem(
    lugar: LugarTuristicoResponse,
    viewModel: AdminLugarTuristicoViewModel,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var imageLoading by remember { mutableStateOf(true) }

    LaunchedEffect(lugar.imagenUrl) {
        viewModel.obtenerImagenLugar(
            lugar.imagenUrl ?: "",
            onSuccess = { responseBody ->
                val bytes = responseBody.bytes()
                if (bytes.isNotEmpty()) {
                    imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                }
                imageLoading = false
            },
            onError = {
                imageLoading = false
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageLoading) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
        } else {
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Imagen Lugar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            } ?: Icon(
                painter = painterResource(id = R.drawable.imagen_bienvenida1),
                contentDescription = "Imagen no disponible",
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Text(text = lugar.nombre, modifier = Modifier.weight(1f))

        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "Editar Lugar")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar Lugar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrUpdateLugarDialog(
    initialLugar: LugarTuristicoResponse?,
    onDismiss: () -> Unit,
    onSave: (LugarTuristicoDto, MultipartBody.Part?) -> Unit
) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf(initialLugar?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(initialLugar?.descripcion ?: "") }
    var ubicacion by remember { mutableStateOf(initialLugar?.ubicacion ?: "") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val openGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> fotoUri = uri }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (initialLugar == null) "Agregar Lugar" else "Actualizar Lugar") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                TextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") }
                )
                TextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    label = { Text("Ubicación") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { openGalleryLauncher.launch("image/*") }) {
                    Text(text = "Seleccionar Imagen")
                }

                fotoUri?.let {
                    Text(text = "Imagen seleccionada: $it", style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val fotoPart = fotoUri?.let { uri ->
                    val file = File(context.getRealPathFromURILugarTuristico(uri))
                    MultipartBody.Part.createFormData(
                        "file",
                        file.name,
                        file.asRequestBody()
                    )
                }
                onSave(
                    LugarTuristicoDto(
                        nombre = nombre,
                        descripcion = descripcion,
                        ubicacion = ubicacion,
                        imagenUrl = fotoUri?.toString() ?: "" // <-- AQUI AÑADES ESTO
                    ),
                    fotoPart
                )
            }) {
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

fun android.content.Context.getRealPathFromURILugarTuristico(uri: Uri): String? {
    val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
    cursor?.moveToFirst()
    val idx = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
    val path = idx?.let { cursor.getString(it) }
    cursor?.close()
    return path
}