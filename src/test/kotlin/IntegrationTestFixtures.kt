import com.fiap.stock.domain.valueobjects.ProductCategory
import com.fiap.stock.driver.web.request.ComponentRequest
import com.fiap.stock.driver.web.request.ProductRequest
import java.math.BigDecimal
import java.util.*



fun createProductRequest(
    name: String = "Big Mac",
    category: String = ProductCategory.MAIN.name,
    price: BigDecimal = BigDecimal("10.00"),
    description: String = "Dois hambúrgueres, alface, queijo, molho especial, cebola, picles, num pão com gergelim",
    minSub: Int = 3,
    maxSub: Int = 3,
    components: List<Long> = listOf(1, 2, 3, 4, 5, 6, 7),
): ProductRequest {
    return ProductRequest(
        name = name,
        category = category,
        price = price,
        description = description,
        minSub = minSub,
        maxSub = maxSub,
        components = components,
    )
}

fun createNewInputRequests(): List<ComponentRequest> {
    return listOf(
        ComponentRequest("Hambúrguer", 100),
        ComponentRequest("Alface", 100),
        ComponentRequest("Queijo", 100),
        ComponentRequest("Molho especial", 100),
        ComponentRequest("Cebola", 100),
        ComponentRequest("Picles", 100),
        ComponentRequest("Pão com gergelim", 100),
    )
}
