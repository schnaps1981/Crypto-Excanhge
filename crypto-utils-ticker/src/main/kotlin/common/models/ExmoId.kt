package common.models

@JvmInline
value class ExmoId(private val id: Int) {
    fun asInt() = id

    companion object {
        val NONE = ExmoId(Int.MIN_VALUE)
    }
}