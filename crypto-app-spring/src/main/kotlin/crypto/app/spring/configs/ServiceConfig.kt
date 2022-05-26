package crypto.app.spring.configs

import OrderService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfig {
    @Bean
    fun orderService(): OrderService = OrderService()
}