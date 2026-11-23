package br.edu.ifpb.app_achados_perdidos.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.app_achados_perdidos.R
import br.edu.ifpb.app_achados_perdidos.domain.model.Categoria
import br.edu.ifpb.app_achados_perdidos.domain.model.TipoItem
import br.edu.ifpb.app_achados_perdidos.ui.components.ItemCard
import br.edu.ifpb.app_achados_perdidos.ui.components.rotulo
import br.edu.ifpb.app_achados_perdidos.ui.theme.Borda
import br.edu.ifpb.app_achados_perdidos.ui.theme.BordaSuave
import br.edu.ifpb.app_achados_perdidos.ui.theme.TextoPrincipal
import br.edu.ifpb.app_achados_perdidos.ui.theme.TextoSecundario

@Composable
fun HomeScreen(
    onItemClick: (Long) -> Unit,
    onNovoItem: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val busca by viewModel.busca.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNovoItem,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier
                    .navigationBarsPadding()
                    .size(64.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.home_adicionar))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Cabeçalho branco: título, busca e abas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.home_titulo),
                    color = TextoPrincipal,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = busca,
                    onValueChange = viewModel::onBuscaChange,
                    placeholder = { Text(stringResource(R.string.home_busca), color = TextoSecundario) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Borda
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.padding(horizontal = 20.dp)) {
                    Aba(
                        texto = stringResource(R.string.aba_perdidos),
                        selecionada = estado.aba == TipoItem.PERDIDO,
                        onClick = { viewModel.onAbaChange(TipoItem.PERDIDO) },
                        modifier = Modifier.weight(1f)
                    )
                    Aba(
                        texto = stringResource(R.string.aba_encontrados),
                        selecionada = estado.aba == TipoItem.ENCONTRADO,
                        onClick = { viewModel.onAbaChange(TipoItem.ENCONTRADO) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            HorizontalDivider(color = BordaSuave)

            // Filtros por categoria
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                item {
                    ChipFiltro(
                        texto = stringResource(R.string.categoria_todas),
                        selecionado = estado.categoria == null,
                        onClick = { viewModel.onCategoriaChange(null) }
                    )
                }
                items(Categoria.entries) { categoria ->
                    ChipFiltro(
                        texto = categoria.rotulo(),
                        selecionado = estado.categoria == categoria,
                        onClick = { viewModel.onCategoriaChange(categoria) }
                    )
                }
            }

            // Lista
            Box(modifier = Modifier.weight(1f)) {
                when {
                    estado.carregando -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                    estado.itens.isEmpty() -> Text(
                        text = stringResource(R.string.home_vazio),
                        color = TextoSecundario,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    else -> LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 120.dp),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
                    ) {
                        items(estado.itens, key = { it.id }) { item ->
                            ItemCard(item = item, onClick = { onItemClick(item.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Aba(
    texto: String,
    selecionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cor = if (selecionada) MaterialTheme.colorScheme.primary else TextoSecundario
    Column(
        modifier = modifier.clickable(role = Role.Tab, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = texto,
            color = cor,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 14.dp)
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(if (selecionada) MaterialTheme.colorScheme.primary else BordaSuave)
        )
    }
}

@Composable
private fun ChipFiltro(texto: String, selecionado: Boolean, onClick: () -> Unit) {
    val azul = MaterialTheme.colorScheme.primary
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (selecionado) azul else Color.White,
        border = BorderStroke(1.dp, if (selecionado) azul else Borda)
    ) {
        Text(
            text = texto,
            color = if (selecionado) Color.White else TextoPrincipal,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
        )
    }
}