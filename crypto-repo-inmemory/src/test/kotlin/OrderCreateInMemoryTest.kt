import crypto.app.inmemory.OrderRepositoryInMemory
import crypto.app.repo.test.RepoOrderCreateTest
import repository.IOrderRepository

class OrderCreateInMemoryTest : RepoOrderCreateTest() {
    override val repo: IOrderRepository = OrderRepositoryInMemory(
        initObjects = initObjects
    )
}
