package br.edu.ifpb.app_achados_perdidos.data.mapper

import br.edu.ifpb.app_achados_perdidos.data.dto.ItemRequest
import br.edu.ifpb.app_achados_perdidos.data.dto.ItemResponse
import br.edu.ifpb.app_achados_perdidos.domain.model.*
import br.edu.ifpb.app_achados_perdidos.util.deIso
import br.edu.ifpb.app_achados_perdidos.util.paraIso

object ItemMapper {

    fun toDomain(r: ItemResponse) = Item(
        id = r.id,
        titulo = r.titulo,
        descricao = r.descricao.orEmpty(),
        categoria = runCatching { Categoria.valueOf(r.categoria.orEmpty().uppercase()) }
            .getOrDefault(Categoria.OUTROS),
        tipo = TipoItem.valueOf(r.tipo),
        status = StatusItem.valueOf(r.status),
        local = r.local.orEmpty(),
        latitude = r.latitude,
        longitude = r.longitude,
        contato = r.contato.orEmpty(),
        imagemUrl = r.imagemUrl,
        dataMillis = deIso(r.data)
    )

    fun toRequest(i: Item) = ItemRequest(
        titulo = i.titulo,
        descricao = i.descricao,
        categoria = i.categoria.name,
        tipo = i.tipo.name,
        local = i.local,
        latitude = i.latitude,
        longitude = i.longitude,
        data = paraIso(i.dataMillis),
        imagemUrl = i.imagemUrl,
        contato = i.contato
    )
}