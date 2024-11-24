import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.HomeSection
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import com.elhady.lafyuu.feature.home.domain.usecase.GetHomeContentUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetHomeContentUseCaseTest {

    private val repository: HomeRepository = mockk()
    private val useCase = GetHomeContentUseCase(repository)

    private val categories = listOf(Category("1", "Rings", null, null))
    private val offers = listOf(Offer("1", "Sale", "50% Off", null))
    private val products = listOf(
        Product("p1", "A", null, 100.0, 30, null),
        Product("p2", "B", null, 100.0, 0, null)
    )

    private fun <T> failure(): AppResult<T> = AppResult.Failure(AppError.Validation("Network error"))

    @Test
    fun `all sources succeed`() = runTest {
        coEvery { repository.getCategories() } returns AppResult.Success(categories)
        coEvery { repository.getOffers() } returns AppResult.Success(offers)
        coEvery { repository.getProducts(any()) } returns AppResult.Success(products)

        val content = (useCase() as AppResult.Success).data

        assertEquals(categories, content.categories)
        assertEquals(offers, content.offers)
        assertEquals(listOf("p1"), content.flashSaleProducts.map { it.id })
        assertEquals(products, content.recommendedProducts)
        assertTrue(content.failedSections.isEmpty())
    }

    @Test
    fun `products failure is reported per section and does not hide other data`() = runTest {
        coEvery { repository.getCategories() } returns AppResult.Success(categories)
        coEvery { repository.getOffers() } returns AppResult.Success(offers)
        coEvery { repository.getProducts(any()) } returns failure()

        val content = (useCase() as AppResult.Success).data

        assertEquals(categories, content.categories)
        assertTrue(content.flashSaleProducts.isEmpty())
        assertEquals(
            setOf(HomeSection.FlashSale, HomeSection.MegaSale, HomeSection.Recommended),
            content.failedSections
        )
    }

    @Test
    fun `offers failure is reported`() = runTest {
        coEvery { repository.getCategories() } returns AppResult.Success(categories)
        coEvery { repository.getOffers() } returns failure()
        coEvery { repository.getProducts(any()) } returns AppResult.Success(products)

        val content = (useCase() as AppResult.Success).data

        assertEquals(setOf(HomeSection.Offers), content.failedSections)
    }

    @Test
    fun `fails when every source fails`() = runTest {
        coEvery { repository.getCategories() } returns failure()
        coEvery { repository.getOffers() } returns failure()
        coEvery { repository.getProducts(any()) } returns failure()

        assertTrue(useCase() is AppResult.Failure)
    }
}