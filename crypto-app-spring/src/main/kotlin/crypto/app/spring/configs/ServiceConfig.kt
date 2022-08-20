package crypto.app.spring.configs

import OrderService
import crypto.app.inmemory.OrderRepositoryInMemory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import repository.IOrderRepository

@Configuration
class ServiceConfig {
    @Bean
    fun orderRepository(): IOrderRepository = OrderRepositoryInMemory()

    @Bean
    fun orderService(repo: IOrderRepository): OrderService = OrderService(repo)
}