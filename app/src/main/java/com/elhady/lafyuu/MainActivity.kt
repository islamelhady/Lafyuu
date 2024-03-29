package com.elhady.lafyuu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.feature.auth.presentation.navigation.AUTH_GRAPH_ROUTE
import com.elhady.lafyuu.feature.auth.presentation.navigation.authGraph
import com.elhady.lafyuu.feature.home.navigation.HOME_ROUTE
import com.elhady.lafyuu.feature.home.navigation.homeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isAuthenticated by viewModel.isAuthenticated.collectAsStateWithLifecycle()

            LafyuuTheme {
                MainApp(
                    isAuthenticated = isAuthenticated
                )
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

        homeScreen()
    }
}