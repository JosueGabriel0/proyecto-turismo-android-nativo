package pe.edu.upeu.turismo_kotlin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.Bienvenida1Screen
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.Bienvenida2Screen
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.HomeScreen
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.admin.HomeAdminScreen
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.login.LoginScreen
import pe.edu.upeu.turismo_kotlin.ui.presentation.screens.usuario.HomeUsuarioScreen

@Composable
fun AppNavigation(showFirstTwoScreens: Boolean) {
    val navController = rememberNavController()
    val startDestination = if (showFirstTwoScreens) "bienvenida1" else "home"

    NavHost(navController = navController, startDestination = startDestination) {
        composable ("bienvenida1") { Bienvenida1Screen(navController) }
        composable("bienvenida2") { Bienvenida2Screen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("homeAdmin") { HomeAdminScreen(navController) }
        composable("homeUsuario") { HomeUsuarioScreen(navController) }
    }
}