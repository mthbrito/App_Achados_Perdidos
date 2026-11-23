package br.edu.ifpb.app_achados_perdidos.data.repository

import br.edu.ifpb.app_achados_perdidos.domain.model.Categoria
import br.edu.ifpb.app_achados_perdidos.domain.model.Item
import br.edu.ifpb.app_achados_perdidos.domain.model.StatusItem
import br.edu.ifpb.app_achados_perdidos.domain.model.TipoItem
import br.edu.ifpb.app_achados_perdidos.util.criarData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * VERSÃO PROVISÓRIA (em memória) só para as telas funcionarem.
 * Quando a API estiver pronta, troque o corpo destas funções por chamadas
 * ao Retrofit (AchadosApi) + ItemMapper. Os ViewModels não precisam mudar.
 */
object ItemRepository {

    private val _itens = MutableStateFlow(itensDeExemplo())
    val itens: StateFlow<List<Item>> = _itens.asStateFlow()

    private var proximoId = 100L

    fun buscarPorId(id: Long): Item? = _itens.value.firstOrNull { it.id == id }

    fun salvar(item: Item) {
        if (item.id == 0L) {
            val novo = item.copy(id = proximoId++)
            _itens.update { listOf(novo) + it }
        } else {
            _itens.update { lista -> lista.map { if (it.id == item.id) item else it } }
        }
    }

    fun excluir(id: Long) {
        _itens.update { lista -> lista.filterNot { it.id == id } }
    }

    fun marcarComoDevolvido(id: Long) {
        _itens.update { lista ->
            lista.map { if (it.id == id) it.copy(status = StatusItem.DEVOLVIDO) else it }
        }
    }

    private fun itensDeExemplo() = listOf(
        Item(
            id = 1, titulo = "Carteira preta", descricao = "Perdida perto da praça, com documentos dentro.",
            categoria = Categoria.DOCUMENTOS, tipo = TipoItem.PERDIDO,
            local = "Praça Central, São José do Egito", latitude = -7.4733, longitude = -37.2800,
            contato = "81 99999-0000", dataMillis = criarData(20, 11, 2026)
        ),
        Item(
            id = 2, titulo = "Chave com chaveiro azul", descricao = "Chaveiro azul com duas chaves.",
            categoria = Categoria.CHAVES, tipo = TipoItem.PERDIDO,
            local = "Escola Municipal", latitude = -7.4745, longitude = -37.2815,
            contato = "81 98888-1111", dataMillis = criarData(19, 11, 2026)
        ),
        Item(
            id = 3, titulo = "Fone de ouvido branco", descricao = "Fone sem fio, estojo branco.",
            categoria = Categoria.ELETRONICOS, tipo = TipoItem.PERDIDO, status = StatusItem.DEVOLVIDO,
            local = "Terminal Rodoviário", latitude = -7.4710, longitude = -37.2780,
            contato = "81 97777-2222", dataMillis = criarData(18, 11, 2026)
        ),
        Item(
            id = 4, titulo = "Celular com capa verde", descricao = "Encontrado na feira, tela trincada.",
            categoria = Categoria.ELETRONICOS, tipo = TipoItem.ENCONTRADO,
            local = "Feira Livre", latitude = -7.4725, longitude = -37.2795,
            contato = "81 96666-3333", dataMillis = criarData(20, 11, 2026)
        )
    )
}