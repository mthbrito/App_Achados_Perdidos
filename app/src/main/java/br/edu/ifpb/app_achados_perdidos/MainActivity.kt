package br.edu.ifpb.app_achados_perdidos

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.edu.ifpb.app_achados_perdidos.ui.navigation.NavGraph
import br.edu.ifpb.app_achados_perdidos.ui.theme.App_Achados_PerdidosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // App só no tema claro: ícones escuros nas barras do sistema.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            App_Achados_PerdidosTheme {
                NavGraph()
            }
        }
    }
}