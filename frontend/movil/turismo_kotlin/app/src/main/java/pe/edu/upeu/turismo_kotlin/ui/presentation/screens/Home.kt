package pe.edu.upeu.turismo_kotlin.ui.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var isClickedLogin by remember { mutableStateOf(false) }
    var isClickedSignup by remember { mutableStateOf(false) }

    val colorCelestePersonalizado = Color(0xFF5AC7F5)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Screen 3")
                },
                actions = {
                    Button(
                        onClick = {
                            isClickedLogin = !isClickedLogin
                            navController.navigate("login")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorCelestePersonalizado,
                            contentColor = if (isClickedLogin) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(0.dp),
                        border = BorderStroke(2.dp, Color.Black),
                        modifier = Modifier
                            .width(90.dp)
                            .height(35.dp)
                    ) {
                        Text("Log in", fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
                            isClickedSignup = !isClickedSignup
                            navController.navigate("home")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorCelestePersonalizado,
                            contentColor = if (isClickedSignup) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(0.dp),
                        border = BorderStroke(2.dp, Color.Black),
                        modifier = Modifier
                            .width(100.dp)
                            .height(35.dp)
                    ) {
                        Text("Sign up", fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Home", fontSize = 24.sp)
            }
        }
    )
}