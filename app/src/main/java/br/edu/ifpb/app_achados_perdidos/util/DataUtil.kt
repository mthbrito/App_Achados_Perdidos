package br.edu.ifpb.app_achados_perdidos.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val MESES = listOf(
    "jan", "fev", "mar", "abr", "mai", "jun",
    "jul", "ago", "set", "out", "nov", "dez"
)

/** Converte milissegundos em texto no formato "20 nov 2026". */
fun formatarData(millis: Long): String {
    val c = Calendar.getInstance().apply { timeInMillis = millis }
    return "${c.get(Calendar.DAY_OF_MONTH)} ${MESES[c.get(Calendar.MONTH)]} ${c.get(Calendar.YEAR)}"
}

fun criarData(dia: Int, mes: Int, ano: Int): Long =
    Calendar.getInstance().apply {
        clear()
        set(ano, mes - 1, dia, 12, 0, 0)
    }.timeInMillis

private val ISO = SimpleDateFormat("yyyy-MM-dd", Locale.US)

fun paraIso(millis: Long): String = ISO.format(Date(millis))

fun deIso(texto: String?): Long =
    texto?.let { runCatching { ISO.parse(it)!!.time + 12 * 3_600_000L }.getOrNull() }
        ?: System.currentTimeMillis()