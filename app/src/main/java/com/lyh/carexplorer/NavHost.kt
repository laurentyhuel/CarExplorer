package com.lyh.carexplorer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.lyh.carexplorer.feature.car.nav.CarDestination
import com.lyh.carexplorer.feature.car.nav.CarListDestination
import com.lyh.carexplorer.feature.car.nav.carGraph
import com.lyh.carexplorer.feature.core.NavigationDestination
import com.lyh.carexplorer.feature.user.nav.userGraph

@Composable
fun NavHost(
    navController: NavHostController,
    onNavigateToDestination: (NavigationDestination, String) -> Unit,
    startDestination: String = CarListDestination.route,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        carGraph(
            navigateToCar = {
                onNavigateToDestination(
                    CarDestination, CarDestination.createNavigationRoute(it)
                )
            },
            onBackClick = onBackClick
        )
        userGraph(onBackClick)
    }
}