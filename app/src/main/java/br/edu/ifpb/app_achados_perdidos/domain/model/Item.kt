package br.edu.ifpb.app_achados_perdidos.domain.model

data class Item(
    val id: Long = 0L,
    val titulo: String,
    val descricao: String,
    val categoria: Categoria,
    val tipo: TipoItem,
    val status: StatusItem = StatusItem.ATIVO,
    val local: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val contato: String,
    val imagemUrl: String? = null,
    val dataMillis: Long = System.currentTimeMillis()
)
