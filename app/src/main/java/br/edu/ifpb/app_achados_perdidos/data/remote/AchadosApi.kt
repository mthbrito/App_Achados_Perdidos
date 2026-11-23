package br.edu.ifpb.app_achados_perdidos.data.remote

import br.edu.ifpb.app_achados_perdidos.data.dto.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface AchadosApi {

    @GET("itens")
    suspend fun listarItens(
        @Query("tipo") tipo: String? = null,
        @Query("categoria") categoria: String? = null,
        @Query("q") busca: String? = null
    ): List<ItemResponse>

    @GET("itens/{id}")
    suspend fun buscarItem(@Path("id") id: Long): ItemResponse

    @POST("itens")
    suspend fun criarItem(@Body item: ItemRequest): ItemResponse

    @PUT("itens/{id}")
    suspend fun atualizarItem(@Path("id") id: Long, @Body item: ItemRequest): ItemResponse

    @PATCH("itens/{id}/devolvido")
    suspend fun marcarDevolvido(@Path("id") id: Long): ItemResponse

    @DELETE("itens/{id}")
    suspend fun excluirItem(@Path("id") id: Long)

    @Multipart
    @POST("upload")
    suspend fun enviarImagem(@Part imagem: MultipartBody.Part): UploadResponse

    @GET("geocode/endereco")
    suspend fun buscarPorEndereco(@Query("q") q: String): List<GeocodeResponse>

    @GET("geocode/coordenadas")
    suspend fun buscarPorCoordenadas(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): GeocodeResponse
}