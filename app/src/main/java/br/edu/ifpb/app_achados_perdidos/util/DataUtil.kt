package br.edu.ifpb.app_achados_perdidos.util

import java.util.Calendar

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