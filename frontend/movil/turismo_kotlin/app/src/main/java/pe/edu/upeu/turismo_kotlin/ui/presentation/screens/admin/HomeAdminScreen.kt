package pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home.AdminCategoriaScreen
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home.AdminDashboardScreen
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home.AdminLugarTuristicoScreen
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home.AdminRolScreen
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.admin_home.AdminUsuarioScreen
import pe.edu.upeu.turismo_kotlin.utils.TokenUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAdminScreen(
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Simular la carga de usuario (deberías reemplazar esto con tu lógica real)
    val userName = remember { mutableStateOf(TokenUtils ?: "Nombre no disponible") }
    val userEmail = remember { mutableStateOf(TokenUtils ?: "Correo no disponible") }

    val selectedView = remember { mutableStateOf("dashboard") } // Estado para saber qué vista mostrar

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxHeight(),
                drawerContainerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        // Header
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Picture",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(8.dp)
                            )
                            Text("Nombre")
                            Text("")
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Opciones de menú
                        NavigationDrawerItem(
                            label = { Text("Dashboard") },
                            selected = selectedView.value == "dashboard",
                            onClick = {
                                selectedView.value = "dashboard"
                                scope.launch { drawerState.close() }
                            },
                            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Dashboard") },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )

                        NavigationDrawerItem(
                            label = { Text("Usuarios") },
                            selected = selectedView.value == "usuarios",
                            onClick = {
                                selectedView.value = "usuarios"
                                scope.launch { drawerState.close() }
                            },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Usuarios") },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )

                        NavigationDrawerItem(
                            label = { Text("Roles") },
                            selected = selectedView.value == "roles",
                            onClick = {
                                selectedView.value = "roles"
                                scope.launch { drawerState.close() }
                            },
                            icon = { Icon(Icons.Default.DateRange, contentDescription = "Roles") },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )

                        NavigationDrawerItem(
                            label = { Text("LugarTuristico") },
                            selected = selectedView.value == "lugarTuristico",
                            onClick = {
                                selectedView.value = "lugarTuristico"
                                scope.launch { drawerState.close() }
                            },
                            icon = { Icon(Icons.Default.Place, contentDescription = "LugarTuristico") },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )

                        NavigationDrawerItem(
                            label = { Text("Categoria") },
                            selected = selectedView.value == "categoria",
                            onClick = {
                                selectedView.value = "categoria"
                                scope.launch { drawerState.close() }
                            },
                            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Categoria") },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }

                    // Botón de Cerrar sesión
                    NavigationDrawerItem(
                        label = { Text("Cerrar sesión", color = Color.Red) },
                        selected = false,
                        onClick = {
                            TokenUtils.TOKEN_CONTENT = ""
                            navController.navigate("login") {
                                popUpTo(0)
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Cerrar sesión", tint = Color.Red) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(getTitle(selectedView.value)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Contenido que cambia
                when (selectedView.value) {
                    "dashboard" -> AdminDashboardScreen()
                    "usuarios" -> AdminUsuarioScreen()
                    "roles" -> AdminRolScreen()
                    "lugarTuristico" -> AdminLugarTuristicoScreen()
                    "categoria" -> AdminCategoriaScreen()
                    else -> Text("Vista no encontrada")
                }
            }
        }
    }
}

fun getTitle(view: String): String {
    return when (view) {
        "dashboard" -> "Dashboard Admin"
        "usuarios" -> "Gestión de Usuarios"
        "roles" -> "Gestión de Roles"
        "lugarTuristico" -> "Gestion de Lugares"
        else -> "Admin Panel"
    }
}