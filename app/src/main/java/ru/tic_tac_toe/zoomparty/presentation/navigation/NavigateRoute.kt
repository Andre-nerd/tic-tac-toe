package ru.tic_tac_toe.zoomparty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.tic_tac_toe.zoomparty.presentation.GameScreen
import ru.tic_tac_toe.zoomparty.presentation.ServiceViewModel
import ru.tic_tac_toe.zoomparty.presentation.SettingScreen

@Composable
fun NavigateRoute(
    navController: NavHostController,
    serviceViewModel: ServiceViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Route.Setting.name
    ) {
        composable(route = Route.Setting.name) {
            SettingScreen(serviceViewModel = serviceViewModel, navController = navController)
        }
        composable(route = Route.Game.name) {
            GameScreen(serviceViewModel = serviceViewModel, navController = navController )
        }
    }

}