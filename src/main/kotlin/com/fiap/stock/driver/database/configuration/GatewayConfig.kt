package com.fiap.stock.driver.database.configuration

import com.fiap.stock.StockApiApp
import com.fiap.stock.adapter.gateway.ComponentGateway
import com.fiap.stock.adapter.gateway.ProductGateway
import com.fiap.stock.adapter.gateway.StockGateway
import com.fiap.stock.adapter.gateway.TransactionalGateway
import com.fiap.stock.adapter.gateway.impl.ComponentGatewayImpl
import com.fiap.stock.adapter.gateway.impl.ProductGatewayImpl
import com.fiap.stock.adapter.gateway.impl.StockGatewayImpl
import com.fiap.stock.adapter.gateway.impl.TransactionalGatewayImpl
import com.fiap.stock.driver.database.persistence.jpa.ComponentJpaRepository
import com.fiap.stock.driver.database.persistence.jpa.ProductJpaRepository
import com.fiap.stock.driver.database.persistence.jpa.StockJpaRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [StockApiApp::class])
class GatewayConfig {

    @Bean("ComponentGateway")
    fun createComponentGateway(componentJpaRepository: ComponentJpaRepository): ComponentGateway {
        return ComponentGatewayImpl(componentJpaRepository)
    }

    @Bean("StockGateway")
    fun createStockGateway(stockJpaRepository: StockJpaRepository): StockGateway {
        return StockGatewayImpl(stockJpaRepository)
    }

    @Bean("ProductGateway")
    fun createProductGateway(productJpaRepository: ProductJpaRepository): ProductGateway {
        return ProductGatewayImpl(productJpaRepository)
    }

    @Bean("TransactionalGateway")
    fun createTransactionalGateway(): TransactionalGateway {
        return TransactionalGatewayImpl()
    }
}
