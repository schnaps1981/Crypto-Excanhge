import models.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

object OrderTable : IntIdTable("Orders") {
    val orderId = varchar("orderId", 50)
    val ownerId = varchar("ownerId", 50)
    val created = timestamp("created")
    val orderState = enumeration("orderState", CryptoOrderState::class)
    val amount = varchar("amount", 50)
    val quantity = varchar("quantity", 50)
    val price = varchar("price", 50)
    val orderType = enumeration("orderType", CryptoOrderType::class)
    val pair = reference("tradePair", PairTable, onDelete = ReferenceOption.CASCADE)
    val lock = varchar("lock", 50)

    fun from(res: InsertStatement<Number>): CryptoOrder {

        val cryptoPair = transaction {
            PairTable.select {
                PairTable.id eq res[pair]
            }.single()
        }

        return CryptoOrder(
            orderId = CryptoOrderId(res[orderId].toString()),
            ownerId = CryptoUserId(res[ownerId].toString()),
            created = res[created],
            orderState = res[orderState],
            amount = BigDecimal(res[amount]),
            quantity = BigDecimal(res[quantity]),
            price = BigDecimal(res[price]),
            orderType = res[orderType],
            pair = PairTable.from(cryptoPair),
            lock = CryptoLock(res[lock].toString())
        )
    }

    fun from(res: ResultRow) = CryptoOrder(
        orderId = CryptoOrderId(res[orderId].toString()),
        ownerId = CryptoUserId(res[ownerId].toString()),
        created = res[created],
        orderState = res[orderState],
        amount = BigDecimal(res[amount]),
        quantity = BigDecimal(res[quantity]),
        price = BigDecimal(res[price]),
        orderType = res[orderType],

        pair = PairTable.from(transaction {
            PairTable.select {
                PairTable.id eq res[pair]
            }.single()
        }),

        lock = CryptoLock(res[lock].toString())
    )
}

object PairTable : IntIdTable("pairs") {
    val first = varchar("first", 10)
    val second = varchar("second", 10)

    fun from(res: ResultRow) = CryptoPair(
        first = res[first],
        second = res[second]
    )

    fun from(res: InsertStatement<Number>) = CryptoPair(
        first = res[first],
        second = res[second]
    )
}
