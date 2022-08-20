import crypto.app.inmemory.OrderRepositoryInMemory
import crypto.app.repo.test.RepoOrderUpdateTest
import repository.IOrderRepository

class OrderUpdateInMemoryTest : RepoOrderUpdateTest() {
    override val repo: IOrderRepository = OrderRepositoryInMemory(
        initObjects = initObjects
    )
}
