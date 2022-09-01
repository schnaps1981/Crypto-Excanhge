import crypto.app.inmemory.OrderRepositoryInMemory
import crypto.app.repo.test.*
import repository.IOrderRepository

class OrderInMemoryTest : RepoOrderCreateTest() {
    override val repo: IOrderRepository = OrderRepositoryInMemory(initObjects = initObjects)
}

class OrderDeleteInMemoryTest : RepoOrderDeleteTest() {
    override val repo: IOrderRepository = OrderRepositoryInMemory(initObjects = initObjects)
}

class OrderReadInMemoryTest : RepoOrderReadTest() {
    override val repo: IOrderRepository = OrderRepositoryInMemory(initObjects = initObjects)
}

class OrderSearchInMemoryTest : RepoOrderSearchTest() {
    override val repo: IOrderRepository = OrderRepositoryInMemory(initObjects = initObjects)
}

class OrderUpdateInMemoryTest : RepoOrderUpdateTest() {
    override val repo: IOrderRepository = OrderRepositoryInMemory(initObjects = initObjects)
}
