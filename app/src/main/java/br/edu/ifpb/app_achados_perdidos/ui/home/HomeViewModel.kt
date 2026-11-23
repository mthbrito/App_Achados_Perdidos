package br.edu.ifpb.app_achados_perdidos.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifpb.app_achados_perdidos.data.repository.ItemRepository
import br.edu.ifpb.app_achados_perdidos.domain.model.Categoria
import br.edu.ifpb.app_achados_perdidos.domain.model.Item
import br.edu.ifpb.app_achados_perdidos.domain.model.TipoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val carregando: Boolean = true,
    val erro: Boolean = false,
    val aba: TipoItem = TipoItem.PERDIDO,
    val categoria: Categoria? = null, // null = "Todas"
    val itens: List<Item> = emptyList()
)

private enum class StatusCarga { CARREGANDO, OK, ERRO }

class HomeViewModel : ViewModel() {

    private val _busca = MutableStateFlow("")
    val busca: StateFlow<String> = _busca.asStateFlow()

    private val _aba = MutableStateFlow(TipoItem.PERDIDO)
    private val _categoria = MutableStateFlow<Categoria?>(null)
    private val _status = MutableStateFlow(StatusCarga.CARREGANDO)

    // combine(): sempre que a lista, a busca, a aba, o filtro ou o status da carga mudam,
    // recalcula o estado exibido.
    val uiState: StateFlow<HomeUiState> = combine(
        ItemRepository.itens, _busca, _aba, _categoria, _status
    ) { itens, busca, aba, categoria, status ->
        HomeUiState(
            carregando = status == StatusCarga.CARREGANDO,
            erro = status == StatusCarga.ERRO,
            aba = aba,
            categoria = categoria,
            itens = itens.filter { item ->
                item.tipo == aba &&
                        (categoria == null || item.categoria == categoria) &&
                        (busca.isBlank() ||
                                item.titulo.contains(busca, ignoreCase = true) ||
                                item.descricao.contains(busca, ignoreCase = true))
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    init {
        carregar()
    }

    // GET /itens: também serve para o botão "Tentar de novo".
    fun carregar() {
        viewModelScope.launch {
            _status.value = StatusCarga.CARREGANDO
            _status.value = try {
                ItemRepository.carregar()
                StatusCarga.OK
            } catch (e: Exception) {
                StatusCarga.ERRO
            }
        }
    }

    fun onBuscaChange(texto: String) { _busca.value = texto }
    fun onAbaChange(aba: TipoItem) { _aba.value = aba }
    fun onCategoriaChange(categoria: Categoria?) { _categoria.value = categoria }
}