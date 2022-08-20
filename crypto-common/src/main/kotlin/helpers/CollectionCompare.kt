package helpers


infix fun <T> Collection<T>.isEqualIgnoreOrder(other: Collection<T>): Boolean {
    if (this !== other) {

        if (this.size != other.size) return false

        val areNotEqual = this.asSequence()
            .map { it in other }
            .contains(false)

        if (areNotEqual) return false
    }

    return true
}
