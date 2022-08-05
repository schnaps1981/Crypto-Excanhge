import crypto.app.inmemory.OrderRepositoryInMemory
import crypto.app.repo.test.RepoOrderReadTest
import repository.IOrderRepository

class OrderReadInMemoryTest : RepoOrderReadTest() {
    override val repo: IOrderRepository = OrderRepositoryInMemory(
        initObjects = initObjects
    )
}
