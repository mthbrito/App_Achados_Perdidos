package br.edu.ifpb.app_achados_perdidos.data.repository

import android.net.Uri
import android.content.Context
import br.edu.ifpb.app_achados_perdidos.data.mapper.ItemMapper
import br.edu.ifpb.app_achados_perdidos.data.remote.RetrofitClient
import br.edu.ifpb.app_achados_perdidos.domain.model.Item
import br.edu.ifpb.app_achados_perdidos.util.uriParaPart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object ItemRepository {

    private val api = RetrofitClient.api

    private val _itens = MutableStateFlow<List<Item>>(emptyList())
    val itens: StateFlow<List<Item>> = _itens.asStateFlow()

    fun buscarPorId(id: Long): Item? = _itens.value.firstOrNull { it.id == id }

    // GET /itens
    suspend fun carregar() {
        _itens.value = api.listarItens().map(ItemMapper::toDomain)
    }

    // POST /itens (id == 0) ou PUT /itens/{id}
    suspend fun salvar(item: Item) {
        val request = ItemMapper.toRequest(item)
        if (item.id == 0L) {
            val criado = ItemMapper.toDomain(api.criarItem(request))
            _itens.update { listOf(criado) + it }
        } else {
            val atualizado = ItemMapper.toDomain(api.atualizarItem(item.id, request))
            _itens.update { lista -> lista.map { if (it.id == atualizado.id) atualizado else it } }
        }
    }

    // DELETE /itens/{id}
    suspend fun excluir(id: Long) {
        api.excluirItem(id)
        _itens.update { lista -> lista.filterNot { it.id == id } }
    }

    // PATCH /itens/{id}/devolvido
    suspend fun marcarComoDevolvido(id: Long) {
        val atualizado = ItemMapper.toDomain(api.marcarDevolvido(id))
        _itens.update { lista -> lista.map { if (it.id == id) atualizado else it } }
    }

    // POST /upload (multipart): devolve a URL pública da imagem
    suspend fun enviarImagem(contexto: Context, uri: Uri): String =
        api.enviarImagem(contexto.applicationContext.uriParaPart(uri)).url

    // GET /geocode/coordenadas (Nominatim via API)
    suspend fun enderecoDe(latitude: Double, longitude: Double): String =
        api.buscarPorCoordenadas(latitude, longitude).endereco
}