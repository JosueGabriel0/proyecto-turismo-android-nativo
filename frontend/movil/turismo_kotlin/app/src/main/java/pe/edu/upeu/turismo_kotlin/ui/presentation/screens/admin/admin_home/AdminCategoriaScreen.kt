package pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home

import android.graphics.BitmapFactory
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import pe.edu.upeu.turismo_kotlin.modelo.CategoriaDto
import pe.edu.upeu.turismo_kotlin.modelo.CategoriaResponse
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCategoriaScreen(
    viewModel: AdminCategoriaViewModel = hiltViewModel()
) {
    val categorias by viewModel.categorias.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState(initial = false)
    val errorMessage by viewModel.errorMessage.collectAsState(initial = null)

    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedCategoria by remember { mutableStateOf<CategoriaResponse?>(null) }

    LaunchedEffect(Unit) {
        viewModel.obtenerCategorias()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedCategoria = null
                isDialogOpen = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Categoría")
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
                    categorias.forEach { categoria ->
                        CategoriaItem(
                            categoria = categoria,
                            onEdit = {
                                selectedCategoria = categoria
                                isDialogOpen = true
                            },
                            onDelete = {
                                viewModel.eliminarCategoria(categoria.idCategoria)
                            },
                            viewModel = viewModel
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
        AddOrUpdateCategoriaDialog(
            initialCategoria = selectedCategoria,
            onDismiss = { isDialogOpen = false },
            onSave = { categoriaDto, imagenPart ->
                if (selectedCategoria == null) {
                    viewModel.crearCategoria(categoriaDto, imagenPart)
                } else {
                    viewModel.actualizarCategoria(selectedCategoria!!.idCategoria, categoriaDto, imagenPart)
                }
                isDialogOpen = false
            }
        )
    }
}

@Composable
fun CategoriaItem(
    categoria: CategoriaResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    viewModel: AdminCategoriaViewModel
) {
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var imageLoading by remember { mutableStateOf(true) }

    LaunchedEffect(categoria.imagenUrl) {
        viewModel.obtenerImagenCategoria(
            categoria.imagenUrl,
            onSuccess = { imageResponse ->
                try {
                    val bytes = imageResponse.bytes()
                    imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                } catch (e: Exception) {
                    Log.e("CategoriaItem", "Error decoding image: ${e.localizedMessage}")
                } finally {
                    imageLoading = false
                }
            },
            onError = {
                Log.e("CategoriaItem", "Error loading image: $it")
                imageLoading = false
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (imageLoading) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
        } else {
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Imagen Categoría",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }
        }

        Text(
            text = categoria.nombre,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f).padding(start = 16.dp)
        )

        Row {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrUpdateCategoriaDialog(
    initialCategoria: CategoriaResponse?,
    onDismiss: () -> Unit,
    onSave: (CategoriaDto, MultipartBody.Part?) -> Unit
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf(initialCategoria?.nombre ?: "") }
    var nombreLugar by remember { mutableStateOf(initialCategoria?.lugarTuristico?.nombre ?: "") }
    var imagenUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val openGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { imagenUri = it }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialCategoria == null) "Agregar Categoría" else "Editar Categoría") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de Categoría") }
                )
                TextField(
                    value = nombreLugar,
                    onValueChange = { nombreLugar = it },
                    label = { Text("Nombre del Lugar Turístico") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    openGalleryLauncher.launch("image/*")
                }) {
                    Text("Seleccionar Imagen")
                }

                imagenUri?.let {
                    Text(text = "Imagen seleccionada: $it", style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val imagenPart = imagenUri?.let {
                    val file = File(context.getRealPathFromURICategoria(it))
                    MultipartBody.Part.createFormData(
                        "file",
                        file.name,
                        file.asRequestBody()
                    )
                }
                onSave(
                    CategoriaDto(
                        nombre = nombre,
                        nombreLugar = nombreLugar
                    ),
                    imagenPart
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

// Helper para obtener el path real del archivo
fun android.content.Context.getRealPathFromURICategoria(uri: android.net.Uri): String {
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.moveToFirst()
    val index = cursor?.getColumnIndex(android.provider.MediaStore.Images.Media.DATA)
    val result = index?.let { cursor.getString(it) }
    cursor?.close()
    return result ?: throw IllegalArgumentException("No se pudo obtener el path")
}