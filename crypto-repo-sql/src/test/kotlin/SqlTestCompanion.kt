import models.CryptoOrder
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "crypto-pass"
    private const val SCHEMA = "crypto"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(initObjects: Collection<CryptoOrder> = emptyList()): RepoOrderSql {
        return RepoOrderSql(url, USER, PASS, SCHEMA, initObjects)
    }
}
