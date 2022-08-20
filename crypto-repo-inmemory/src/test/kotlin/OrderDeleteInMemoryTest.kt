import crypto.app.inmemory.OrderRepositoryInMemory
import crypto.app.repo.test.RepoOrderDeleteTest
import repository.IOrderRepository

class OrderDeleteInMemoryTest : RepoOrderDeleteTest() {
    override val repo: IOrderRepository = OrderRepositoryInMemory(
        initObjects = initObjects
    )
}
