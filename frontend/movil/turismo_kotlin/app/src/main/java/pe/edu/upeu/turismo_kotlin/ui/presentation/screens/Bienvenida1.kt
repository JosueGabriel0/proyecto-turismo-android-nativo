package pe.edu.upeu.turismo_kotlin.ui.presentation.screens

import pe.edu.upeu.turismo_kotlin.R;
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Bienvenida1Screen(navController: NavController) {
    var isPressed by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        if (isPressed) Color(0xFF448AFF) else Color.White
    )
    val textColor by animateColorAsState(
        if (isPressed) Color.White else Color.Black
    )

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tesoros locales",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Descubra y apoye a las empresas locales cerca de usted",
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Image(
                    painter = painterResource(id = R.drawable.imagen_bienvenida1),
                    contentDescription = "Imagen Bienvenida",
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(0.dp)
                        )
                        .clickable {
                            isPressed = !isPressed
                            navController.navigate("bienvenida2")
                        }
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "EXPLORAR",
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}