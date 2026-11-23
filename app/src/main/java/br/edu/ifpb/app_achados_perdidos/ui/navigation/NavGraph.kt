package br.edu.ifpb.app_achados_perdidos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.ifpb.app_achados_perdidos.ui.detalhes.DetalhesScreen
import br.edu.ifpb.app_achados_perdidos.ui.formulario.FormularioScreen
import br.edu.ifpb.app_achados_perdidos.ui.home.HomeScreen

object Rotas {
    const val HOME = "home"
    const val DETALHES = "detalhes/{itemId}"
    const val FORMULARIO = "formulario?itemId={itemId}"

    fun detalhes(id: Long) = "detalhes/$id"

    // Sem id = novo item; com id = edição.
    fun formulario(id: Long? = null) = if (id == null) "formulario" else "formulario?itemId=$id"
}

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Rotas.HOME) {

        composable(Rotas.HOME) {
            HomeScreen(
                onItemClick = { id -> navController.navigate(Rotas.detalhes(id)) },
                onNovoItem = { navController.navigate(Rotas.formulario()) }
            )
        }

        composable(
            route = Rotas.DETALHES,
            arguments = listOf(navArgument("itemId") { type = NavType.LongType })
        ) {
            DetalhesScreen(
                onVoltar = { navController.popBackStack() },
                onEditar = { id -> navController.navigate(Rotas.formulario(id)) }
            )
        }

        composable(
            route = Rotas.FORMULARIO,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            FormularioScreen(onVoltar = { navController.popBackStack() })
        }
    }
}