package pe.edu.upeu.turismo_kotlin.ui.presentation.components.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    items: List<Destinations>,
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background) // Fondo blanco (o el fondo del theme)
    ) {
        items.forEach { destination ->
            TextButton(
                onClick = {
                    navController.navigate(destination.route)
                    scope.launch { drawerState.close() }
                }
            ) {
                Text(
                    text = destination.title,
                    color = MaterialTheme.colorScheme.onBackground // Color de texto correcto
                )
            }
        }
    }
}
