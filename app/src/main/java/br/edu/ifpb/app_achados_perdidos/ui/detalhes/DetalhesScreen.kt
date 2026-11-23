package br.edu.ifpb.app_achados_perdidos.ui.detalhes

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.app_achados_perdidos.R
import br.edu.ifpb.app_achados_perdidos.domain.model.StatusItem
import br.edu.ifpb.app_achados_perdidos.ui.components.BotaoContorno
import br.edu.ifpb.app_achados_perdidos.ui.components.BotaoPrimario
import br.edu.ifpb.app_achados_perdidos.ui.components.ImagemItem
import br.edu.ifpb.app_achados_perdidos.ui.components.MiniMapa
import br.edu.ifpb.app_achados_perdidos.ui.components.StatusChip
import br.edu.ifpb.app_achados_perdidos.ui.components.rotulo
import br.edu.ifpb.app_achados_perdidos.ui.theme.Perigo
import br.edu.ifpb.app_achados_perdidos.ui.theme.PerigoBorda
import br.edu.ifpb.app_achados_perdidos.ui.theme.TextoPrincipal
import br.edu.ifpb.app_achados_perdidos.ui.theme.TextoSecundario
import br.edu.ifpb.app_achados_perdidos.util.formatarData

@Composable
fun DetalhesScreen(
    onVoltar: () -> Unit,
    onEditar: (Long) -> Unit,
    viewModel: DetalhesViewModel = viewModel()
) {
    val item by viewModel.item.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var confirmarExclusao by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val atual = item
        if (atual == null) {
            Text(
                text = stringResource(R.string.detalhes_nao_encontrado),
                color = TextoSecundario,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ImagemItem(
                    url = atual.imagemUrl,
                    contentDescription = stringResource(R.string.cd_imagem_item),
                    tamanhoIcone = 72.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(20.dp)
                ) {
                    StatusChip(tipo = atual.tipo, status = atual.status)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = atual.titulo,
                        color = TextoPrincipal,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(
                            R.string.detalhes_categoria_data,
                            atual.categoria.rotulo(),
                            formatarData(atual.dataMillis)
                        ),
                        color = TextoSecundario,
                        fontSize = 17.sp
                    )
                    if (atual.descricao.isNotBlank()) {
                        Spacer(Modifier.height(12.dp))
                        Text(text = atual.descricao, color = TextoPrincipal, fontSize = 19.sp, lineHeight = 26.sp)
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.detalhes_local, atual.local),
                        color = TextoSecundario,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    MiniMapa(
                        latitude = atual.latitude,
                        longitude = atual.longitude,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                    Spacer(Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        BotaoPrimario(
                            texto = stringResource(R.string.detalhes_contato),
                            onClick = { discar(context, atual.contato) }
                        )
                        if (atual.status == StatusItem.ATIVO) {
                            BotaoContorno(
                                texto = stringResource(R.string.detalhes_devolvido),
                                onClick = viewModel::marcarComoDevolvido,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            BotaoContorno(
                                texto = stringResource(R.string.detalhes_editar),
                                onClick = { onEditar(atual.id) },
                                modifier = Modifier.weight(1f)
                            )
                            BotaoContorno(
                                texto = stringResource(R.string.detalhes_excluir),
                                onClick = { confirmarExclusao = true },
                                corTexto = Perigo,
                                corBorda = PerigoBorda,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // Botão de voltar sobre a imagem
        Surface(
            onClick = onVoltar,
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(16.dp)
                .size(48.dp)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_voltar)
                )
            }
        }
    }

    if (confirmarExclusao) {
        AlertDialog(
            onDismissRequest = { confirmarExclusao = false },
            containerColor = Color.White,
            title = { Text(stringResource(R.string.excluir_titulo)) },
            text = { Text(stringResource(R.string.excluir_mensagem)) },
            confirmButton = {
                TextButton(onClick = {
                    confirmarExclusao = false
                    viewModel.excluir(aoConcluir = onVoltar)
                }) {
                    Text(stringResource(R.string.detalhes_excluir), color = Perigo, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarExclusao = false }) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        )
    }
}

/** Abre o discador com o número já preenchido (não precisa de permissão). */
private fun discar(context: Context, numero: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(numero)}"))
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        // Aparelho sem discador: nada a fazer por enquanto.
    }
}