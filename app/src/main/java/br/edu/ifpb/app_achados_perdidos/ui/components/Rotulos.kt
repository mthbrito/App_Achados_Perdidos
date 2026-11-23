package br.edu.ifpb.app_achados_perdidos.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.edu.ifpb.app_achados_perdidos.R
import br.edu.ifpb.app_achados_perdidos.domain.model.Categoria

/** Texto exibido para cada categoria (vem do strings.xml, então o domain fica "limpo"). */
@Composable
fun Categoria.rotulo(): String = stringResource(
    when (this) {
        Categoria.DOCUMENTOS -> R.string.categoria_documentos
        Categoria.CHAVES -> R.string.categoria_chaves
        Categoria.ELETRONICOS -> R.string.categoria_eletronicos
        Categoria.OUTROS -> R.string.categoria_outros
    }
)