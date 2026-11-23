package br.edu.ifpb.app_achados_perdidos.ui.formulario

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.app_achados_perdidos.R
import br.edu.ifpb.app_achados_perdidos.domain.model.Categoria
import br.edu.ifpb.app_achados_perdidos.domain.model.TipoItem
import br.edu.ifpb.app_achados_perdidos.ui.components.BotaoPrimario
import br.edu.ifpb.app_achados_perdidos.ui.components.ImagemItem
import br.edu.ifpb.app_achados_perdidos.ui.components.MiniMapa
import br.edu.ifpb.app_achados_perdidos.ui.components.rotulo
import br.edu.ifpb.app_achados_perdidos.ui.theme.Borda
import br.edu.ifpb.app_achados_perdidos.ui.theme.BordaSuave
import br.edu.ifpb.app_achados_perdidos.ui.theme.CinzaPlaceholder
import br.edu.ifpb.app_achados_perdidos.ui.theme.IconeCinza
import br.edu.ifpb.app_achados_perdidos.ui.theme.TextoPrincipal
import br.edu.ifpb.app_achados_perdidos.ui.theme.TextoSecundario

@Composable
fun FormularioScreen(
    onVoltar: () -> Unit,
    viewModel: FormularioViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    // Seletor de fotos do sistema (não exige permissão de armazenamento).
    val seletorFoto = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) viewModel.onImagemChange(uri.toString())
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Surface(color = Color.White) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .height(64.dp)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onVoltar) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.cd_voltar)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(if (estado.editando) R.string.form_editar else R.string.form_novo),
                            color = TextoPrincipal,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider(color = BordaSuave)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Foto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .bordaTracejada(Borda, 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(CinzaPlaceholder)
                    .clickable {
                        seletorFoto.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (estado.imagemUri != null) {
                    ImagemItem(
                        url = estado.imagemUri,
                        contentDescription = stringResource(R.string.cd_imagem_item),
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.ic_imagem),
                            contentDescription = null,
                            tint = IconeCinza,
                            modifier = Modifier.width(36.dp).height(36.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.form_foto),
                            color = IconeCinza,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            SeletorTipo(selecionado = estado.tipo, onSelecionar = viewModel::onTipoChange)

            CampoTexto(
                rotulo = stringResource(R.string.form_titulo),
                valor = estado.titulo,
                onValorChange = viewModel::onTituloChange,
                invalido = estado.tituloInvalido
            )

            CampoTexto(
                rotulo = stringResource(R.string.form_descricao),
                valor = estado.descricao,
                onValorChange = viewModel::onDescricaoChange,
                minLinhas = 3
            )

            CampoCategoria(selecionada = estado.categoria, onSelecionar = viewModel::onCategoriaChange)

            CampoTexto(
                rotulo = stringResource(R.string.form_local),
                valor = estado.local,
                onValorChange = viewModel::onLocalChange,
                invalido = estado.localInvalido
            )

            Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                MiniMapa(
                    latitude = estado.latitude,
                    longitude = estado.longitude,
                    modifier = Modifier.fillMaxSize(),
                    onMapaClick = viewModel::onMapaClick
                )
                Text(
                    text = stringResource(R.string.mapa_dica),
                    color = TextoPrincipal,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }

            CampoTexto(
                rotulo = stringResource(R.string.form_contato),
                valor = estado.contato,
                onValorChange = viewModel::onContatoChange,
                invalido = estado.contatoInvalido,
                teclado = KeyboardType.Phone,
                capitalizacao = KeyboardCapitalization.None
            )

            BotaoPrimario(
                texto = stringResource(R.string.form_salvar),
                onClick = { viewModel.salvar(aoSalvar = onVoltar) }
            )
        }
    }
}

@Composable
private fun SeletorTipo(selecionado: TipoItem, onSelecionar: (TipoItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CinzaPlaceholder)
            .padding(6.dp)
    ) {
        OpcaoTipo(
            texto = stringResource(R.string.form_perdi),
            selecionada = selecionado == TipoItem.PERDIDO,
            onClick = { onSelecionar(TipoItem.PERDIDO) },
            modifier = Modifier.weight(1f)
        )
        OpcaoTipo(
            texto = stringResource(R.string.form_encontrei),
            selecionada = selecionado == TipoItem.ENCONTRADO,
            onClick = { onSelecionar(TipoItem.ENCONTRADO) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun OpcaoTipo(
    texto: String,
    selecionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selecionada) Color.White else Color.Transparent)
            .clickable(role = Role.RadioButton, onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = if (selecionada) TextoPrincipal else TextoSecundario,
            fontSize = 18.sp,
            fontWeight = if (selecionada) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}

@Composable
private fun CampoTexto(
    rotulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    minLinhas: Int = 1,
    invalido: Boolean = false,
    teclado: KeyboardType = KeyboardType.Text,
    capitalizacao: KeyboardCapitalization = KeyboardCapitalization.Sentences
) {
    Column(modifier) {
        Text(rotulo, color = TextoPrincipal, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            modifier = Modifier.fillMaxWidth(),
            isError = invalido,
            singleLine = minLinhas == 1,
            minLines = minLinhas,
            shape = RoundedCornerShape(14.dp),
            colors = coresDoCampo(),
            keyboardOptions = KeyboardOptions(keyboardType = teclado, capitalization = capitalizacao),
            supportingText = if (invalido) {
                { Text(stringResource(R.string.form_obrigatorio)) }
            } else null
        )
    }
}

/** Menu suspenso de categoria (campo somente leitura + DropdownMenu). */
@Composable
private fun CampoCategoria(selecionada: Categoria, onSelecionar: (Categoria) -> Unit) {
    var expandido by remember { mutableStateOf(false) }
    val forma = RoundedCornerShape(14.dp)

    Column {
        Text(
            stringResource(R.string.form_categoria),
            color = TextoPrincipal,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(6.dp))
        Box {
            OutlinedTextField(
                value = selecionada.rotulo(),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                shape = forma,
                colors = coresDoCampo(),
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            // Camada transparente que captura o toque e abre o menu.
            Box(
                Modifier
                    .matchParentSize()
                    .clip(forma)
                    .clickable { expandido = true }
            )
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false },
                containerColor = Color.White
            ) {
                Categoria.entries.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria.rotulo()) },
                        onClick = {
                            onSelecionar(categoria)
                            expandido = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun coresDoCampo() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    errorContainerColor = Color.White,
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = Borda
)

/** Borda pontilhada (o Compose não tem pronta): desenha um retângulo arredondado tracejado. */
private fun Modifier.bordaTracejada(cor: Color, raio: Dp, espessura: Dp = 2.dp): Modifier =
    drawBehind {
        val metade = espessura.toPx() / 2
        drawRoundRect(
            color = cor,
            topLeft = Offset(metade, metade),
            size = Size(size.width - 2 * metade, size.height - 2 * metade),
            cornerRadius = CornerRadius(raio.toPx()),
            style = Stroke(
                width = espessura.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f))
            )
        )
    }