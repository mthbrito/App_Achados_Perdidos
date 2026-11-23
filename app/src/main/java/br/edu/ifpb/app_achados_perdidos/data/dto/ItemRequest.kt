package br.edu.ifpb.app_achados_perdidos.data.dto

data class ItemRequest(
    val titulo: String,
    val descricao: String? = null,
    val categoria: String? = null,
    val tipo: String,
    val local: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val data: String? = null,
    val imagemUrl: String? = null,
    val contato: String? = null
)
