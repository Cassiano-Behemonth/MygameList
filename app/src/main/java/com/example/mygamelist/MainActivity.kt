package com.example.mygamelist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mygamelist.data.local.MyGameListDatabase
import com.example.mygamelist.data.repository.GameRepository
import com.example.mygamelist.data.repository.UserRepository
import com.example.mygamelist.ui.games.FormScreen
import com.example.mygamelist.ui.games.HomeScreen
import com.example.mygamelist.ui.games.LoginScreen
import com.example.mygamelist.ui.games.ProfileScreen
import com.example.mygamelist.ui.games.RegisterScreen
import com.example.mygamelist.ui.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGameListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current


    val database = remember { MyGameListDatabase.getDatabase(context) }
    val gameRepository = remember { GameRepository(database.gameDao()) }
    val userRepository = remember { UserRepository(database.userDao()) }


    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userRepository)
    )
    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(gameRepository)
    )


    val currentUser by userViewModel.currentUser.collectAsState()
    val currentUserId by userViewModel.currentUserId.collectAsState()
    val authState by userViewModel.authState.collectAsState()
    val games by gameViewModel.games.collectAsState()
    val gameUiState by gameViewModel.uiState.collectAsState()


    LaunchedEffect(currentUserId) {
        if (currentUserId > 0) {
            gameViewModel.loadGames(currentUserId)
        }
    }


    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                Toast.makeText(context, (authState as AuthState.Success).message, Toast.LENGTH_SHORT).show()
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
                userViewModel.resetAuthState()
            }
            is AuthState.RegisterSuccess -> {
                Toast.makeText(context, (authState as AuthState.RegisterSuccess).message, Toast.LENGTH_SHORT).show()
                navController.popBackStack()
                userViewModel.resetAuthState()
            }
            is AuthState.LogoutSuccess -> {
                Toast.makeText(context, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show()
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
                userViewModel.resetAuthState()
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
                userViewModel.resetAuthState()
            }
            else -> {}
        }
    }


    LaunchedEffect(gameUiState) {
        when (gameUiState) {
            is GameUiState.Success -> {
                Toast.makeText(context, (gameUiState as GameUiState.Success).message, Toast.LENGTH_SHORT).show()
                gameViewModel.resetUiState()
            }
            is GameUiState.Error -> {
                Toast.makeText(context, (gameUiState as GameUiState.Error).message, Toast.LENGTH_SHORT).show()
                gameViewModel.resetUiState()
            }
            else -> {}
        }
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLogin = { username, password ->
                    userViewModel.login(username, password)
                },
                onGoToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegister = { username, password ->
                    userViewModel.register(username, password)
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                games = games,
                registeredUser = currentUser,
                onAddClick = { navController.navigate("form/-1") },
                onProfileClick = { navController.navigate("profile") },
                onEditClick = { gameEntity ->
                    navController.navigate("form/${gameEntity.id}")
                },
                onDeleteClick = { gameEntity ->
                    gameViewModel.deleteGame(gameEntity.id)
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                user = currentUser,
                onLogout = { userViewModel.logout() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "form/{gameId}",
            arguments = listOf(navArgument("gameId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getInt("gameId") ?: -1
            val editGame = games.find { it.id == gameId }

            FormScreen(
                currentUserId = currentUserId,
                editGame = editGame,
                onSave = { name, description, achievementsCompleted, totalAchievements, completionPercentage, status ->
                    gameViewModel.saveGame(
                        userId = currentUserId,
                        gameId = if (gameId == -1) null else gameId,
                        name = name,
                        description = description,
                        achievementsCompleted = achievementsCompleted,
                        totalAchievements = totalAchievements,
                        completionPercentage = completionPercentage,
                        status = status
                    )
                    navController.popBackStack()
                },
                onCancel = {
                    Toast.makeText(context, "Operação cancelada", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            )
        }
    }
}