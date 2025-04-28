package pe.edu.upeu.turismo_kotlin.ui.presentation.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.edu.upeu.turismo_kotlin.modelo.LoginDto
import pe.edu.upeu.turismo_kotlin.utils.JwtUtils
import pe.edu.upeu.turismo_kotlin.utils.TokenUtils

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.observeAsState(false)
    val isLogin by viewModel.islogin.observeAsState(false)
    val isError by viewModel.isError.observeAsState(false)
    val loginResult by viewModel.listUser.observeAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo decorativo
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-80).dp, y = (-80).dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF5AC7F5),
                            Color(0xFF87E2FF)
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de Usuario
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Usuario") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xFF5AC7F5),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { /* Acción de recuperación */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (username.value.isNotBlank() && password.value.isNotBlank()) {
                        val user = LoginDto(
                            username = username.value,
                            password = password.value
                        )

                        coroutineScope.launch {
                            viewModel.loginSys(user)
                            delay(3000)
                            Log.i("TOKEN", TokenUtils.TOKEN_CONTENT)
                            Log.i("Role", TokenUtils.ROL)

                            if (isLogin && loginResult != null) {
                                if (TokenUtils.ROL == "ROLE_ADMIN") {
                                    navController.navigate("homeAdmin")
                                } else if (TokenUtils.ROL == "ROLE_USUARIO") {
                                    navController.navigate("homeUsuario")
                                }
                            } else if (isError) {
                                Toast.makeText(
                                    context,
                                    "Error en el inicio de sesión",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5AC7F5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Iniciar Sesión", fontSize = 16.sp, color = Color.White)
            }
        }

        if (isLoading) {
            // Cargando...
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}