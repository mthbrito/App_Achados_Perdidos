package br.edu.ifpb.app_achados_perdidos.util

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun Context.uriParaPart(uri: Uri): MultipartBody.Part {
    val tipo = contentResolver.getType(uri) ?: "image/jpeg"
    val bytes = contentResolver.openInputStream(uri)!!.use { it.readBytes() }
    return MultipartBody.Part.createFormData("imagem", "foto", bytes.toRequestBody(tipo.toMediaType()))
}