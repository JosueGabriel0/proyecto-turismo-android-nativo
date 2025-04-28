package pe.edu.upeu.turismo_kotlin.ui.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bienvenida2Screen(navController: NavController) {
    var isClicked by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val colorCelestePersonalizado = Color(0xFF0AA3EF)

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 24.dp, top = 80.dp, end = 24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(BorderStroke(2.dp, Color.Gray), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("android.resource://${context.packageName}/raw/tienda")
                        .build(),
                    contentDescription = "Tienda",
                    modifier = Modifier.size(50.dp),
                    imageLoader = imageLoader
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Descubre lo nuestro",
                fontSize = 30.sp,
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Descubra lugares únicos y emprendimientos como juegos en el lago, artesanía, comida, y mucho más",
                fontSize = 20.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(140.dp))

            Button(
                onClick = {
                    isClicked = !isClicked
                    navController.navigate("home")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorCelestePersonalizado,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(0.dp),
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
            ) {
                Text("EXPLORAR", fontSize = 20.sp)
            }
        }
    }
}