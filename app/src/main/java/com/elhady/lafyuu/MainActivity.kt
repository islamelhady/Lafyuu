package com.elhady.lafyuu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.auth.presentation.navigation.AUTH_GRAPH_ROUTE
import com.elhady.lafyuu.feature.auth.presentation.navigation.authGraph
import com.elhady.lafyuu.feature.cart.navigation.cartScreen
import com.elhady.lafyuu.feature.checkout.presentation.addAddressScreen
import com.elhady.lafyuu.feature.checkout.presentation.checkoutScreen
import com.elhady.lafyuu.feature.checkout.presentation.editAddressScreen
import com.elhady.lafyuu.feature.checkout.presentation.navigateToCheckout
import com.elhady.lafyuu.feature.checkout.presentation.navigateToAddAddress
import com.elhady.lafyuu.feature.checkout.presentation.navigateToEditAddress
import com.elhady.lafyuu.feature.checkout.presentation.navigateToPayment
import com.elhady.lafyuu.feature.checkout.presentation.paymentScreen
import com.elhady.lafyuu.feature.explore.presentation.exploreScreen
import com.elhady.lafyuu.feature.home.navigation.HOME_ROUTE
import com.elhady.lafyuu.feature.home.navigation.categoriesScreen
import com.elhady.lafyuu.feature.home.navigation.categoryProductsScreen
import com.elhady.lafyuu.feature.home.navigation.homeScreen
import com.elhady.lafyuu.feature.home.navigation.navigateToCategories
import com.elhady.lafyuu.feature.home.navigation.navigateToCategoryProducts
import com.elhady.lafyuu.feature.notifications.presentation.navigateToNotifications
import com.elhady.lafyuu.feature.orders.presentation.navigateToOrders
import com.elhady.lafyuu.feature.orders.presentation.navigateToOrderDetails
import com.elhady.lafyuu.feature.orders.presentation.ordersScreen
import com.elhady.lafyuu.feature.orders.presentation.orderDetailsScreen
import com.elhady.lafyuu.feature.profile.presentation.navigateToAccount
import com.elhady.lafyuu.feature.profile.presentation.navigateToProfile
import com.elhady.lafyuu.feature.profile.presentation.navigateToChangePassword
import com.elhady.lafyuu.feature.profile.presentation.accountScreen
import com.elhady.lafyuu.feature.profile.presentation.profileScreen
import com.elhady.lafyuu.feature.profile.presentation.changePasswordScreen
import com.elhady.lafyuu.feature.product.navigation.navigateToProductDetails
import com.elhady.lafyuu.feature.product.navigation.productDetailsScreen
import com.elhady.lafyuu.feature.review.navigation.navigateToReviews
import com.elhady.lafyuu.feature.review.navigation.navigateToWriteReview
import com.elhady.lafyuu.feature.review.navigation.reviewsScreen
import com.elhady.lafyuu.feature.review.navigation.writeReviewScreen
import com.elhady.lafyuu.feature.search.presentation.navigateToSearch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val cartItemCount by viewModel.cartItemCount.collectAsStateWithLifecycle()

            LafyuuTheme {
                when (uiState) {
                    MainUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Theme.color.blue
                            )
                        }
                    }
                    is MainUiState.Authenticated -> {
                        key(true) {
                            MainApp(isAuthenticated = true, cartItemCount = cartItemCount)
                        }
                    }
                    is MainUiState.Unauthenticated -> {
                        key(false) {
                            MainApp(isAuthenticated = false, cartItemCount = 0)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainApp(
    isAuthenticated: Boolean,
    cartItemCount: Int
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) HOME_ROUTE else AUTH_GRAPH_ROUTE
    ) {
        authGraph(
            onNavigateToHome = {
                navController.navigate(HOME_ROUTE) {
                    popUpTo(AUTH_GRAPH_ROUTE) { inclusive = true }
                }
            },
            navController = navController
        )

        homeScreen(
            onNavigateToProductDetails = { productId ->
                navController.navigateToProductDetails(productId)
            },
            onNavigateToFlashSale = {
                // Handle navigation to flash sale
            },
            onNavigateToMegaSale = {
                // Handle navigation to mega sale
            },
            onNavigateToMoreCategories = {
                navController.navigateToCategories()
            },
            onNavigateToCategoryProducts = { categoryId, categoryName ->
                navController.navigateToCategoryProducts(categoryName)
            },
            onNavigateToSearch = {
                navController.navigateToSearch()
            },
            onNavigateToNotifications = {
                navController.navigateToNotifications()
            },
            onNavigateToWishlist = {
                // Handle navigation to wishlist
            },
            onNavigateToTab = { tabId ->
                if (tabId == "account" || tabId == "Account") {
                    navController.navigateToAccount()
                } else if (navController.graph.findNode(tabId) != null) {
                    navController.navigate(tabId) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            cartItemCount = cartItemCount
        )

        categoriesScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToCategoryProducts = { categoryId, categoryName ->
                navController.navigateToCategoryProducts(categoryName)
            }
        )

        categoryProductsScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToProductDetails = { productId ->
                navController.navigateToProductDetails(productId)
            }
        )

        exploreScreen(
            onNavigateToCategoryProducts = { categoryId, categoryName ->
                navController.navigateToCategoryProducts(categoryName)
            },
            onNavigateToSearch = {
                // Handle navigation to search
            },
            onNavigateToNotifications = {},
            onNavigateToWishlist = {},
            onNavigateToTab = { tabId ->
                if (tabId == "account" || tabId == "Account") {
                    navController.navigateToAccount()
                } else if (navController.graph.findNode(tabId) != null) {
                    navController.navigate(tabId) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        productDetailsScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToSearch = {
                // Handle navigation to search
            },
            onNavigateToProductDetails = { productId ->
                navController.navigateToProductDetails(productId)
            },
            onNavigateToReviews = { productId ->
                navController.navigateToReviews(productId)
            },
            onNavigateToWriteReview = { productId ->
                navController.navigateToWriteReview(productId)
            }
        )

        reviewsScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToWriteReview = { productId ->
                navController.navigateToWriteReview(productId)
            }
        )

        writeReviewScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        cartScreen(
            onNavigateToCheckout = {
                navController.navigateToCheckout()
            },
            onNavigateToProductDetails = { productId ->
                navController.navigateToProductDetails(productId)
            },
            onNavigateToTab = { tabId ->
                if (tabId == "account" || tabId == "Account") {
                    navController.navigateToAccount()
                } else if (navController.graph.findNode(tabId) != null) {
                    navController.navigate(tabId) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        checkoutScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToPayment = { addressId, paymentMethod ->
                navController.navigateToPayment(addressId, paymentMethod)
            },
            onNavigateToAddAddress = {
                navController.navigateToAddAddress()
            },
            onNavigateToEditAddress = { addressId ->
                navController.navigateToEditAddress(addressId)
            }
        )

        addAddressScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        editAddressScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        paymentScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToSuccess = {
                navController.navigateToOrders()
            },
            onOpenUrl = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                try {
                    context.startActivity(intent)
                } catch (_: Exception) {}
            }
        )

        ordersScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToOrderDetails = { orderId ->
                navController.navigateToOrderDetails(orderId)
            }
        )

        orderDetailsScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        accountScreen(
            onNavigateToProfile = {
                navController.navigateToProfile()
            },
            onNavigateToOrders = {
                navController.navigateToOrders()
            },
            onNavigateToAddress = {
                navController.navigateToCheckout()
            },
            onNavigateToPayment = {}
        )

        profileScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToChangePassword = {
                navController.navigateToChangePassword()
            },
            onNavigateToLogin = {
                navController.navigate(AUTH_GRAPH_ROUTE) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        )

        changePasswordScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}
