package common.models

@JvmInline
value class ExmoId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ExmoId("")
    }
}