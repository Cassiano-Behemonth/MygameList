package com.example.mygamelist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

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
    val games = remember {
        mutableStateListOf(
            Game("The Legend of Zelda: Breath of the Wild", "Aventura em mundo aberto épica", 76, 120, 85, GameStatus.IN_PROGRESS),
            Game("Minecraft", "Sandbox infinito de construção", 95, 95, 100, GameStatus.COMPLETED),
            Game("Among Us", "Jogo social de dedução espacial", 12, 25, 60, GameStatus.IN_PROGRESS),
            Game("Cyberpunk 2077", "RPG de ação futurista cyberpunk", 46, 46, 100, GameStatus.COMPLETED)
        )
    }

    var registeredUser by remember { mutableStateOf<User?>(null) }
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLogin = { username, password ->
                    if (registeredUser?.username == username && registeredUser?.password == password) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                        Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show()
                    }
                },
                onGoToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegister = { username, password ->
                    registeredUser = User(username, password)
                    Toast.makeText(context, "Usuário cadastrado!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                games = games,
                registeredUser = registeredUser,
                onAddClick = { navController.navigate("form") },
                onProfileClick = { navController.navigate("profile") },
                onEditClick = { index ->
                    if (index >= 0) {
                        navController.navigate("form/$index")
                    }
                },
                onDeleteClick = { index ->
                    if (index in games.indices) {
                        Toast.makeText(context, "Jogo removido!", Toast.LENGTH_SHORT).show()
                        games.removeAt(index)
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                user = registeredUser,
                onLogout = {
                    registeredUser = null
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                    Toast.makeText(context, "Logout realizado!", Toast.LENGTH_SHORT).show()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "form/{index}",
            arguments = listOf(navArgument("index") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: -1
            FormScreen(
                games = games,
                editIndex = if (index >= 0) index else null,
                onSave = { navController.popBackStack() },
                onCancel = {
                    Toast.makeText(context, "Operação cancelada", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            )
        }

        composable("form") {
            FormScreen(
                games = games,
                editIndex = null,
                onSave = { navController.popBackStack() },
                onCancel = {
                    Toast.makeText(context, "Operação cancelada", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            )
        }
    }
}