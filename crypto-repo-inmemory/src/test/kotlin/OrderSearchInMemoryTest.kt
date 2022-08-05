import crypto.app.inmemory.OrderRepositoryInMemory
import crypto.app.repo.test.RepoOrderSearchTest
import repository.IOrderRepository

class OrderSearchInMemoryTest : RepoOrderSearchTest() {
    override val repo: IOrderRepository = OrderRepositoryInMemory(
        initObjects = initObjects
    )
}
