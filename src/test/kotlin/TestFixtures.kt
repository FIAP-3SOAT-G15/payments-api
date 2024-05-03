import com.fiap.stock.domain.entities.Component
import com.fiap.stock.domain.entities.Product
import com.fiap.stock.domain.entities.Stock
import com.fiap.stock.domain.valueobjects.ProductCategory
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


fun createProduct(
    number: Long = 123,
    name: String = "Big Mac",
    category: ProductCategory = ProductCategory.MAIN,
    price: BigDecimal = BigDecimal("10.00"),
    description: String = "Dois hambúrgueres, alface, queijo, molho especial, cebola, picles, num pão com gergelim",
    minSub: Int = 3,
    maxSub: Int = 3,
    subitems: List<Product> = listOf(),
    components: List<Component> = listOf(),
) = Product(
    number = number,
    name = name,
    category = category,
    price = price,
    description = description,
    minSub = minSub,
    maxSub = maxSub,
    subItems = subitems,
    components = components,
)

fun createStock(
    productNumber: Long = 123,
    quantity: Long = 100,
) = Stock(
    componentNumber = productNumber,
    quantity = quantity,
)

fun createComponent(
    componentNumber: Long = 9870001,
    name: String = "Lata refrigerante coca-cola 355ml",
) = Component(
    number = componentNumber,
    name = name,
)



