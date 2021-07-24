package intro.android.cm.api

data class Notas(
    val id: Int?,
    val title: String,
    val description: String,
    val latitude: Float,
    val longitude: Float,
    val id_utilizador: Int,
    // esta var e para receber mensagens do WS de modo a ler erros
    val MSG: String
)
