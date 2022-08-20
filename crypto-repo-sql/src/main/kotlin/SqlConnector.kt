import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.DEFAULT_ISOLATION_LEVEL
import org.jetbrains.exposed.sql.transactions.transaction

class SqlConnector(
    private val config: SQLDbConfig,
    private val databaseConfig: DatabaseConfig = DatabaseConfig { defaultIsolationLevel = DEFAULT_ISOLATION_LEVEL }
) {
    // Sample of describing different db drivers in case of multiple DB connections with different data bases
    private enum class DbType(val driver: String) {
        MYSQL("com.mysql.cj.jdbc.Driver"),
        POSTGRESQL("org.postgresql.Driver")
    }

    // Sample of different db types
    private val dbType: DbType = config.url.let { url ->
        when {
            url.startsWith("jdbc:mysql://") -> DbType.MYSQL
            url.startsWith("jdbc:postgresql://") -> DbType.POSTGRESQL
            else -> error("Cannot parse database type from url: $url. `jdbc:mysql://...` and `jdbc:postgresql://` are supported only.")
        }
    }

    // Global connection to PSQL
    private val globalConnection = Database.connect(config.url, dbType.driver, config.user, config.password, databaseConfig = databaseConfig)

    // Ensure creation of new connection with options to migrate/pre-drop database
    fun connect(vararg tables: Table): Database {
        // Create schema if such not exists
        transaction(globalConnection) {
            connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS ${config.schema}", false).executeUpdate()
        }

        // Create connection for all supported db types
        val connect = Database.connect(
            config.url, dbType.driver, config.user, config.password,
            databaseConfig = databaseConfig,
            setupConnection = { connection ->
                when (dbType) {
                    DbType.MYSQL -> {
                        connection.transactionIsolation = DEFAULT_ISOLATION_LEVEL
                        connection.schema = config.schema
                        connection.catalog = config.schema
                    }
                    DbType.POSTGRESQL -> {
                        connection.transactionIsolation = DEFAULT_ISOLATION_LEVEL
                        connection.schema = config.schema
                    }
                }
            }
        )

        // Update schema:
        //   - drop if needed (for ex, in tests);
        //   - exec migrations if needed;
        //   - otherwise unsure to create tables
        transaction(connect) {
            if (System.getenv("crypto.sql_drop_db")?.toBoolean() == true) {
                SchemaUtils.drop(*tables, inBatch = true)
                SchemaUtils.create(*tables, inBatch = true)
            } else if (System.getenv("crypto.sql_fast_migration").toBoolean()) {
                // TODO: Place to exec migration: create and ensure tables
            } else {
                SchemaUtils.createMissingTablesAndColumns(*tables, inBatch = true)
            }
        }

        return connect
    }
}
