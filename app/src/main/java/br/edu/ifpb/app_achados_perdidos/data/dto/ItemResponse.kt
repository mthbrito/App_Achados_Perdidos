package br.edu.ifpb.app_achados_perdidos.data.dto

data class ItemResponse(
    val id: Long,
    val titulo: String,
    val descricao: String?,
    val categoria: String?,
    val tipo: String,
    val status: String,
    val local: String?,
    val latitude: Double?,
    val longitude: Double?,
    val data: String?,
    val imagemUrl: String?,
    val contato: String?
)