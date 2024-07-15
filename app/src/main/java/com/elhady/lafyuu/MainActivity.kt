package com.elhady.lafyuu

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.auth.presentation.navigation.AUTH_GRAPH_ROUTE
import com.elhady.lafyuu.feature.auth.presentation.navigation.authGraph
import com.elhady.lafyuu.feature.home.navigation.HOME_ROUTE
import com.elhady.lafyuu.feature.home.navigation.homeScreen
import com.elhady.lafyuu.feature.cart.navigation.cartScreen
import com.elhady.lafyuu.feature.product.navigation.navigateToProductDetails
import com.elhady.lafyuu.feature.product.navigation.productDetailsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                            MainApp(isAuthenticated = true)
                        }
                    }
                    is MainUiState.Unauthenticated -> {
                        key(false) {
                            MainApp(isAuthenticated = false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainApp(
    isAuthenticated: Boolean
) {
    val navController = rememberNavController()

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
            onNavigateToCategory = { categoryId, categoryName ->
                // Handle navigation to category products
            },
            onNavigateToFlashSale = {
                // Handle navigation to flash sale
            },
            onNavigateToMegaSale = {
                // Handle navigation to mega sale
            },
            onNavigateToCategories = {
                // Handle navigation to categories
            },
            onNavigateToNotifications = {
                // Handle navigation to notifications
            },
            onNavigateToWishlist = {
                // Handle navigation to wishlist
            },
            onNavigateToTab = { tabId ->
                if (navController.graph.findNode(tabId) != null) {
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
            }
        )

        cartScreen(
            onNavigateToCheckout = {},
            onNavigateToProductDetails = { productId ->
                navController.navigateToProductDetails(productId)
            }
        )
    }
}
