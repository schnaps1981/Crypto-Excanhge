package common.models

data class ExmoOutData(
    var id: ExmoId = ExmoId.NONE,
    var method: ExmoMethod = ExmoMethod.NONE,
    val topics: List<String> = emptyList()
) {
    fun deepCopy() = ExmoOutData(
        id = this@ExmoOutData.id,
        method = this@ExmoOutData.method,
        topics = this@ExmoOutData.topics
    )

    fun isEmpty() = this === EMPTY

    companion object {
        val EMPTY = ExmoOutData()
    }
}
