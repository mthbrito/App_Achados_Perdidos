package br.edu.ifpb.app_achados_perdidos.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.app_achados_perdidos.ui.theme.Borda
import br.edu.ifpb.app_achados_perdidos.ui.theme.TextoPrincipal

private val FormaBotao = RoundedCornerShape(14.dp)

@Composable
fun BotaoPrimario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = FormaBotao,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        )
    ) {
        Text(texto, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BotaoContorno(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    corTexto: Color = TextoPrincipal,
    corBorda: Color = Borda
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = FormaBotao,
        border = BorderStroke(1.dp, corBorda),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = corTexto
        )
    ) {
        Text(texto, fontSize = 17.sp, fontWeight = FontWeight.Bold)
    }
}