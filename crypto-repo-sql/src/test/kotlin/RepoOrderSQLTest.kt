import crypto.app.repo.test.*
import repository.IOrderRepository

class RepoOrderSQLCreateTest : RepoOrderCreateTest() {
    override val repo: IOrderRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoOrderSQLDeleteTest : RepoOrderDeleteTest() {
    override val repo: IOrderRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoOrderSQLReadTest : RepoOrderReadTest() {
    override val repo: IOrderRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoOrderSQLSearchTest : RepoOrderSearchTest() {
    override val repo: IOrderRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoOrderSQLUpdateTest : RepoOrderUpdateTest() {
    override val repo: IOrderRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}
