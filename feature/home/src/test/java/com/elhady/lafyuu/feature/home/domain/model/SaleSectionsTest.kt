import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.model.toSaleSections
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SaleSectionsTest {

    private fun product(id: String, discount: Int) = Product(id, id, null, 100.0, discount, null)

    @Test
    fun `only discounted products are included sorted by discount descending`() {
        val sections = listOf(product("a", 10), product("b", 0), product("c", 30)).toSaleSections()

        assertEquals(listOf("c", "a"), sections.flash.map { it.id })
        assertTrue(sections.mega.isEmpty())
    }

    @Test
    fun `flash takes the first 15 and mega takes the rest`() {
        val products = (1..20).map { product("p$it", discount = it) }

        val sections = products.toSaleSections()

        assertEquals(15, sections.flash.size)
        assertEquals("p20", sections.flash.first().id)
        assertEquals(listOf("p5", "p4", "p3", "p2", "p1"), sections.mega.map { it.id })
    }
}