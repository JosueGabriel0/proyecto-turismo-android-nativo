package pe.edu.upeu.turismo_kotlin.ui.presentation.components.admin

// Define your destinations here with titles and routes
sealed class Destinations(val route: String, val title: String) {
    object Dashboard : Destinations("dashboard", "Dashboard")
    object Users : Destinations("users", "Users")
    object Reports : Destinations("reports", "Reports")
    object Settings : Destinations("settings", "Settings")
}
