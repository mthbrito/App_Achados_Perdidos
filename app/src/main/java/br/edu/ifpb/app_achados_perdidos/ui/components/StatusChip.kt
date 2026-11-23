package br.edu.ifpb.app_achados_perdidos.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.app_achados_perdidos.R
import br.edu.ifpb.app_achados_perdidos.domain.model.StatusItem
import br.edu.ifpb.app_achados_perdidos.domain.model.TipoItem
import br.edu.ifpb.app_achados_perdidos.ui.theme.DevolvidoFundo
import br.edu.ifpb.app_achados_perdidos.ui.theme.DevolvidoTexto
import br.edu.ifpb.app_achados_perdidos.ui.theme.EncontradoFundo
import br.edu.ifpb.app_achados_perdidos.ui.theme.EncontradoTexto
import br.edu.ifpb.app_achados_perdidos.ui.theme.PerdidoFundo
import br.edu.ifpb.app_achados_perdidos.ui.theme.PerdidoTexto

/** Selo "Perdido" / "Encontrado" / "Devolvido". Devolvido tem prioridade. */
@Composable
fun StatusChip(tipo: TipoItem, status: StatusItem, modifier: Modifier = Modifier) {
    val (fundo, texto, rotulo) = when {
        status == StatusItem.DEVOLVIDO ->
            Triple(DevolvidoFundo, DevolvidoTexto, R.string.status_devolvido)
        tipo == TipoItem.PERDIDO ->
            Triple(PerdidoFundo, PerdidoTexto, R.string.status_perdido)
        else ->
            Triple(EncontradoFundo, EncontradoTexto, R.string.status_encontrado)
    }

    Surface(shape = RoundedCornerShape(50), color = fundo, modifier = modifier) {
        Text(
            text = stringResource(rotulo),
            color = texto,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}