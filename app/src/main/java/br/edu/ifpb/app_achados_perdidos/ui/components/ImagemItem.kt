package br.edu.ifpb.app_achados_perdidos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.edu.ifpb.app_achados_perdidos.R
import br.edu.ifpb.app_achados_perdidos.ui.theme.CinzaPlaceholder
import br.edu.ifpb.app_achados_perdidos.ui.theme.IconeCinza
import coil.compose.SubcomposeAsyncImage

/**
 * Mostra a foto do item com Coil. Enquanto carrega, se der erro ou se não
 * houver foto, aparece o ícone de imagem sobre fundo cinza (como no protótipo).
 */
@Composable
fun ImagemItem(
    url: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tamanhoIcone: Dp = 40.dp
) {
    Box(
        modifier = modifier.background(CinzaPlaceholder),
        contentAlignment = Alignment.Center
    ) {
        if (url.isNullOrBlank()) {
            IconePlaceholder(tamanhoIcone)
        } else {
            SubcomposeAsyncImage(
                model = url,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = { IconePlaceholder(tamanhoIcone) },
                error = { IconePlaceholder(tamanhoIcone) }
            )
        }
    }
}

@Composable
private fun IconePlaceholder(tamanho: Dp) {
    Icon(
        painter = painterResource(R.drawable.ic_imagem),
        contentDescription = null,
        tint = IconeCinza,
        modifier = Modifier.size(tamanho)
    )
}