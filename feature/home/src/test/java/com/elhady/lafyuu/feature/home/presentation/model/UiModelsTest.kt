import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.presentation.mapper.toUi
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class UiModelsTest {

    private fun product(base: Double = 100.0, discount: Int = 0, rating: Float? = null) =
        Product("1", "Ring", null, base, discount, rating)

    @Test
    fun `discounted product shows final price original price and discount`() {
        val ui = product(discount = 20).toUi()

        assertEquals("$80.00", ui.price)
        assertEquals("$100.00", ui.originalPrice)
        assertEquals(20, ui.discountPercentage)
    }

    @Test
    fun `product without discount has no original price`() {
        val ui = product().toUi()

        assertEquals("$100.00", ui.price)
        assertNull(ui.originalPrice)
        assertNull(ui.discountPercentage)
    }

    @Test
    fun `rating is rounded and clamped to five`() {
        assertEquals(4, product(rating = 4.4f).toUi().rating)
        assertEquals(5, product(rating = 4.6f).toUi().rating)
        assertEquals(5, product(rating = 7f).toUi().rating)
        assertNull(product(rating = null).toUi().rating)
    }
}