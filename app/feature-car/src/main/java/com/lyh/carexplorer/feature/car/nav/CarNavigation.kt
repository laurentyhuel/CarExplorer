package com.lyh.carexplorer.feature.car.nav

import android.net.Uri
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lyh.carexplorer.feature.car.detail.CarRoute
import com.lyh.carexplorer.feature.car.list.CarListRoute
import com.lyh.carexplorer.feature.core.NavigationDestination

object CarListDestination : NavigationDestination {
    override val route = "cars_route"
    override val destination = "cars_destination"
}

object CarDestination : NavigationDestination {
    const val carIdArg = "carId"
    override val route = "car_route/{$carIdArg}"
    override val destination = "car_destination"

    /**
     * Creates destination route for an carId
     */
    fun createNavigationRoute(carIdArg: String): String {
        val encodedId = Uri.encode(carIdArg)
        return "car_route/$encodedId"
    }

    /**
     * Returns the carId from a [NavBackStackEntry] after an car destination navigation call
     */
    fun fromNavArgs(entry: NavBackStackEntry): String {
        val encodedId = entry.arguments!!.getInt(carIdArg)
        return Uri.decode(encodedId.toString())
    }
}

fun NavGraphBuilder.carGraph(
    navigateToCar: (String) -> Unit,
    onBackClick: () -> Unit,
) {

    composable(route = CarListDestination.route) {
        CarListRoute(onNavigateToCar = navigateToCar)
    }
    composable(
        route = CarDestination.route,
        arguments = listOf(
            navArgument(CarDestination.carIdArg) { type = NavType.IntType }
        )
    ) {
        CarRoute(
            onBackClick = onBackClick
        )
    }
}
