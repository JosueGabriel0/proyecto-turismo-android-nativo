package pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.FileViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: FileViewModel = hiltViewModel()
) {
    // El nombre de archivo por defecto
    val defaultFileName = "1745723820802_Perfil.jpg" // Ajusta esto al nombre real de la imagen por defecto

    // Obtén los valores de los estados del ViewModel usando collectAsState()
    val imageBitmapState = viewModel.imageBitmap.collectAsState()
    val isLoadingState = viewModel.isLoading.collectAsState()
    val errorMessageState = viewModel.errorMessage.collectAsState()

    // Llamada a obtener la imagen con el nombre de archivo por defecto
    LaunchedEffect(defaultFileName) {
        viewModel.getImage(defaultFileName)
    }

    // Diseño de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isLoadingState.value -> {
                // Mostrar el indicador de carga mientras se descarga la imagen
                CircularProgressIndicator()
            }
            imageBitmapState.value != null -> {
                // Mostrar la imagen descargada
                Image(
                    bitmap = imageBitmapState.value!!.asImageBitmap(),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            }
            errorMessageState.value != null -> {
                // Mostrar mensaje de error si algo salió mal
                Text("Error: ${errorMessageState.value}")
            }
            else -> {
                // Si no hay imagen ni error, mostrar un mensaje
                Text("Sin imagen disponible")
            }
        }
    }
}
