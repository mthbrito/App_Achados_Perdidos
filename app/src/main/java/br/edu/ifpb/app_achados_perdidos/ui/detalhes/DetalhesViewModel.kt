package br.edu.ifpb.app_achados_perdidos.ui.detalhes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifpb.app_achados_perdidos.data.repository.ItemRepository
import br.edu.ifpb.app_achados_perdidos.domain.model.Item
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DetalhesViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    // O Navigation coloca os argumentos da rota dentro do SavedStateHandle.
    private val itemId: Long = checkNotNull(savedStateHandle.get<Long>("itemId"))

    val item: StateFlow<Item?> = ItemRepository.itens
        .map { lista -> lista.firstOrNull { it.id == itemId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ItemRepository.buscarPorId(itemId))

    fun marcarComoDevolvido() = ItemRepository.marcarComoDevolvido(itemId)

    fun excluir(aoConcluir: () -> Unit) {
        ItemRepository.excluir(itemId)
        aoConcluir()
    }
}