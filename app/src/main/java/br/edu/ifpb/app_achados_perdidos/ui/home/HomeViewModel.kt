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

data class HomeUiState(
    val carregando: Boolean = true,
    val aba: TipoItem = TipoItem.PERDIDO,
    val categoria: Categoria? = null, // null = "Todas"
    val itens: List<Item> = emptyList()
)

class HomeViewModel : ViewModel() {

    private val _busca = MutableStateFlow("")
    val busca: StateFlow<String> = _busca.asStateFlow()

    private val _aba = MutableStateFlow(TipoItem.PERDIDO)
    private val _categoria = MutableStateFlow<Categoria?>(null)

    // combine(): sempre que a lista, a busca, a aba ou o filtro mudam, recalcula a lista exibida.
    val uiState: StateFlow<HomeUiState> = combine(
        ItemRepository.itens, _busca, _aba, _categoria
    ) { itens, busca, aba, categoria ->
        HomeUiState(
            carregando = false,
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

    fun onBuscaChange(texto: String) { _busca.value = texto }
    fun onAbaChange(aba: TipoItem) { _aba.value = aba }
    fun onCategoriaChange(categoria: Categoria?) { _categoria.value = categoria }
}