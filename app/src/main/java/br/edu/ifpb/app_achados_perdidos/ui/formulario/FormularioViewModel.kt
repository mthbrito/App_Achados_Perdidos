package br.edu.ifpb.app_achados_perdidos.ui.formulario

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifpb.app_achados_perdidos.data.repository.ItemRepository
import br.edu.ifpb.app_achados_perdidos.domain.model.Categoria
import br.edu.ifpb.app_achados_perdidos.domain.model.Item
import br.edu.ifpb.app_achados_perdidos.domain.model.StatusItem
import br.edu.ifpb.app_achados_perdidos.domain.model.TipoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormularioUiState(
    val editando: Boolean = false,
    val tipo: TipoItem = TipoItem.PERDIDO,
    val titulo: String = "",
    val descricao: String = "",
    val categoria: Categoria = Categoria.DOCUMENTOS,
    val local: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val contato: String = "",
    val imagemUri: String? = null,
    val tituloInvalido: Boolean = false,
    val localInvalido: Boolean = false,
    val contatoInvalido: Boolean = false,
    val salvando: Boolean = false,
    val erro: String? = null
)

class FormularioViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    // -1 = criando um item novo; qualquer outro valor = editando aquele item.
    private val itemId: Long = savedStateHandle.get<Long>("itemId") ?: -1L
    private val itemOriginal: Item? = ItemRepository.buscarPorId(itemId)

    private val _uiState = MutableStateFlow(
        itemOriginal?.let { item ->
            FormularioUiState(
                editando = true,
                tipo = item.tipo,
                titulo = item.titulo,
                descricao = item.descricao,
                categoria = item.categoria,
                local = item.local,
                latitude = item.latitude,
                longitude = item.longitude,
                contato = item.contato,
                imagemUri = item.imagemUrl
            )
        } ?: FormularioUiState()
    )
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    fun onTipoChange(tipo: TipoItem) = _uiState.update { it.copy(tipo = tipo) }
    fun onTituloChange(v: String) = _uiState.update { it.copy(titulo = v, tituloInvalido = false) }
    fun onDescricaoChange(v: String) = _uiState.update { it.copy(descricao = v) }
    fun onCategoriaChange(v: Categoria) = _uiState.update { it.copy(categoria = v) }
    fun onLocalChange(v: String) = _uiState.update { it.copy(local = v, localInvalido = false) }
    fun onContatoChange(v: String) = _uiState.update { it.copy(contato = v, contatoInvalido = false) }
    fun onImagemChange(uri: String) = _uiState.update { it.copy(imagemUri = uri) }

    fun onMapaClick(latitude: Double, longitude: Double) {
        _uiState.update { it.copy(latitude = latitude, longitude = longitude) }
        viewModelScope.launch {
            runCatching { ItemRepository.enderecoDe(latitude, longitude) }
                .onSuccess { endereco ->
                    _uiState.update { it.copy(local = endereco, localInvalido = false) }
                }
        }
    }

    fun salvar(contexto: Context, aoSalvar: () -> Unit) {
        val s = _uiState.value
        // ... validação (tituloInvalido etc.) continua igual ...

        _uiState.update { it.copy(salvando = true, erro = null) }
        viewModelScope.launch {
            try {
                val uri = s.imagemUri
                // Foto nova (content://...) precisa de upload; foto já existente (http...) é reaproveitada.
                val imagemUrl = if (uri != null && !uri.startsWith("http"))
                    ItemRepository.enviarImagem(contexto, Uri.parse(uri))
                else uri

                ItemRepository.salvar(
                    Item(
                        id = itemOriginal?.id ?: 0L,
                        titulo = s.titulo.trim(),
                        descricao = s.descricao.trim(),
                        categoria = s.categoria,
                        tipo = s.tipo,
                        status = itemOriginal?.status ?: StatusItem.ATIVO,
                        local = s.local.trim(),
                        latitude = s.latitude,
                        longitude = s.longitude,
                        contato = s.contato.trim(),
                        imagemUrl = imagemUrl,
                        dataMillis = itemOriginal?.dataMillis ?: System.currentTimeMillis()
                    )
                )
                aoSalvar()
            } catch (e: Exception) {
                _uiState.update { it.copy(salvando = false, erro = "Não foi possível salvar. Verifique a conexão.") }
            }
        }
    }
}