package services

import com.fiap.stock.adapter.gateway.ProductGateway
import com.fiap.stock.domain.entities.Product
import com.fiap.stock.domain.errors.ErrorType
import com.fiap.stock.domain.errors.SelfOrderManagementException
import com.fiap.stock.domain.valueobjects.ProductCategory
import com.fiap.stock.usecases.*

class ProductService(
    private val productRepository: ProductGateway,
    private val loadComponentUseCase: LoadComponentUseCase,
) :
    LoadProductUseCase,
        SearchProductUseCase,
        AssembleProductsUseCase,
        RemoveProductUseCase {
    override fun getByProductNumber(productNumber: Long): Product {
        return productRepository.findByProductNumber(productNumber)
            ?: throw SelfOrderManagementException(
                errorType = ErrorType.PRODUCT_NOT_FOUND,
                message = "Product [$productNumber] not found",
            )
    }

    override fun findAll(): List<Product> {
        return productRepository.findAll()
    }

    override fun findByCategory(category: ProductCategory): List<Product> {
        return productRepository.findByCategory(category)
    }

    override fun searchByName(productName: String): List<Product> {
        return productRepository.searchByName(productName.trim())
    }

    override fun create(
        product: Product,
        components: List<Long>,
    ): Product {
        val newProduct = product.copy(components = components.map(loadComponentUseCase::getByComponentNumber))
        return productRepository.create(newProduct)
    }

    override fun update(
        product: Product,
        components: List<Long>,
    ): Product {
        val newProduct = product.copy(components = components.map(loadComponentUseCase::getByComponentNumber))
        return productRepository.update(newProduct)
    }

    override fun delete(productNumber: Long): Product {
        return productRepository.delete(productNumber)
    }

    override fun compose(
        productNumber: Long,
        subItemsNumbers: List<Long>,
    ): Product {
        val product = getByProductNumber(productNumber)
        val subItems = subItemsNumbers.map(::getByProductNumber)
        val newProduct = product.copy(subItems = subItems)
        return productRepository.update(newProduct)
    }
}
