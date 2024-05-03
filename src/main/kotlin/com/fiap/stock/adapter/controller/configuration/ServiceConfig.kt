package com.fiap.stock.adapter.controller.configuration

import com.fiap.stock.StockApiApp
import com.fiap.stock.adapter.gateway.ComponentGateway
import com.fiap.stock.adapter.gateway.ProductGateway
import com.fiap.stock.adapter.gateway.StockGateway
import com.fiap.stock.usecases.LoadComponentUseCase
import com.fiap.stock.usecases.services.ComponentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import services.ProductService
import services.StockService

@Configuration
@ComponentScan(basePackageClasses = [StockApiApp::class])
class ServiceConfig {


    @Bean
    fun createProductService(
        productRepository: ProductGateway,
        loadComponentUseCase: LoadComponentUseCase,
    ): ProductService {
        return ProductService(
            productRepository,
            loadComponentUseCase,
        )
    }

    @Bean
    fun createComponentService(
        componentRepository: ComponentGateway,
        stockRepository: StockGateway,
        productRepository: ProductGateway,
    ): ComponentService {
        return ComponentService(
            componentRepository,
            stockRepository,
            productRepository,
        )
    }

    @Bean
    fun createStockService(stockRepository: StockGateway): StockService {
        return StockService(stockRepository)
    }


}
