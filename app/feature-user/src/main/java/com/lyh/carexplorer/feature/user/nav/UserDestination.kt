package com.lyh.carexplorer.feature.user.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lyh.carexplorer.feature.core.NavigationDestination
import com.lyh.carexplorer.feature.user.detail.UserRoute

object UserDestination : NavigationDestination {
    override val route = "user_route"
    override val destination = "user_destination"
}

fun NavGraphBuilder.userGraph(
    onBackClick: () -> Unit,
) {

    composable(route = UserDestination.route) {
        UserRoute(onBackClick = onBackClick)
    }
}
