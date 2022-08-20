data class SQLDbConfig(
    val url: String = DEFAULT_URL,
    val user: String = DEFAULT_USER,
    val password: String = DEFAULT_PASS,
    val schema: String = DEFAULT_SCHEMA,
) {
    companion object {
        const val DEFAULT_URL = "jdbc:postgresql://localhost:5432/cryptodevdb"
        const val DEFAULT_USER = "postgres"
        const val DEFAULT_PASS = "crypto-pass"
        const val DEFAULT_SCHEMA = "crypto"
    }
}
