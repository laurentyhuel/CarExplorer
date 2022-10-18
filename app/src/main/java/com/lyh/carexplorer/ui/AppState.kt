package com.lyh.carexplorer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lyh.carexplorer.feature.car.nav.CarListDestination
import com.lyh.carexplorer.feature.core.NavigationDestination
import com.lyh.carexplorer.feature.core.R
import com.lyh.carexplorer.feature.core.TopLevelDestination
import com.lyh.carexplorer.feature.user.nav.UserDestination

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
): AppState {
    return remember(navController) {
        AppState(navController)
    }
}

@Stable
class AppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    fun navigate(destination: NavigationDestination, route: String? = null) {
        navController.navigate(route ?: destination.route)
    }

    fun onBackClick() {
        navController.popBackStack()
    }

    /**
     * Top level destinations to be used in the BottomBar
     */
    val topLevelDestinations: List<TopLevelDestination> = listOf(
        TopLevelDestination(
            route = CarListDestination.route,
            destination = CarListDestination.destination,
            icon = R.drawable.cars,
            iconTextId = R.string.search
        ),
        TopLevelDestination(
            route = UserDestination.route,
            destination = UserDestination.destination,
            icon = R.drawable.user,
            iconTextId = R.string.my_account
        )
    )
}


