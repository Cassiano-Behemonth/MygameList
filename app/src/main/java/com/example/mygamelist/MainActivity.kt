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
import kotlinx.coroutines.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygamelist.toGame
import com.example.mygamelist.toUser

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
    val scope = rememberCoroutineScope()


    val database = remember { MyGameListDatabase.getDatabase(context) }
    val userDao = database.userDao()
    val gameDao = database.gameDao()


    var currentUserId by remember { mutableIntStateOf(0) }
    var registeredUser by remember { mutableStateOf<User?>(null) }


    val gamesFromDb by gameDao.getAllGamesByUser(currentUserId).collectAsState(initial = emptyList())
    val games = remember(gamesFromDb) {
        mutableStateListOf<Game>().apply {
            addAll(gamesFromDb.map { it.toGame() })
        }
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLogin = { username, password ->
                    scope.launch {
                        val user = userDao.login(username, password)
                        if (user != null) {
                            currentUserId = user.id
                            registeredUser = user.toUser()
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                            Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onGoToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegister = { username, password ->
                    scope.launch {
                        val exists = userDao.checkUsernameExists(username)
                        if (exists > 0) {
                            Toast.makeText(context, "Usuário já existe!", Toast.LENGTH_SHORT).show()
                        } else {
                            val newUser = UserEntity(username = username, password = password)
                            userDao.insertUser(newUser)
                            Toast.makeText(context, "Usuário cadastrado!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                games = games,
                registeredUser = registeredUser,
                onAddClick = { navController.navigate("form/-1") },
                onProfileClick = { navController.navigate("profile") },
                onEditClick = { index ->
                    if (index >= 0 && index < gamesFromDb.size) {
                        navController.navigate("form/${gamesFromDb[index].id}")
                    }
                },
                onDeleteClick = { index ->
                    if (index >= 0 && index < gamesFromDb.size) {
                        scope.launch {
                            gameDao.deleteGameById(gamesFromDb[index].id)
                            Toast.makeText(context, "Jogo removido!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                user = registeredUser,
                onLogout = {
                    registeredUser = null
                    currentUserId = 0
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                    Toast.makeText(context, "Logout realizado!", Toast.LENGTH_SHORT).show()
                },
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


            val editGame = gamesFromDb.find { it.id == gameId }

            FormScreenWithDB(
                currentUserId = currentUserId,
                editGame = editGame,
                gameDao = gameDao,
                onSave = { navController.popBackStack() },
                onCancel = {
                    Toast.makeText(context, "Operação cancelada", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun FormScreenWithDB(
    currentUserId: Int,
    editGame: GameEntity?,
    gameDao: GameDao,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf(editGame?.name ?: "") }
    var description by remember { mutableStateOf(editGame?.description ?: "") }
    var achievementsCompleted by remember { mutableStateOf(editGame?.achievementsCompleted?.toString() ?: "0") }
    var totalAchievements by remember { mutableStateOf(editGame?.totalAchievements?.toString() ?: "0") }
    var completionPercentage by remember { mutableStateOf(editGame?.completionPercentage?.toString() ?: "0") }
    var selectedStatus by remember { mutableStateOf(
        editGame?.status?.let { GameStatus.valueOf(it) } ?: GameStatus.IN_PROGRESS
    ) }


    val tempGames = remember { mutableStateListOf<Game>() }


    LaunchedEffect(editGame) {
        editGame?.let {
            name = it.name
            description = it.description
            achievementsCompleted = it.achievementsCompleted.toString()
            totalAchievements = it.totalAchievements.toString()
            completionPercentage = it.completionPercentage.toString()
            selectedStatus = GameStatus.valueOf(it.status)
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        GameTopAppBar(if (editGame != null) "EDITAR JOGO" else "NOVO JOGO")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("NOME DO JOGO") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Yellow,
                        focusedLabelColor = GoldenYellow,
                        cursorColor = Yellow
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("DESCRIÇÃO") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Yellow,
                        focusedLabelColor = GoldenYellow,
                        cursorColor = Yellow
                    ),
                    shape = RoundedCornerShape(16.dp),
                    maxLines = 3
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = achievementsCompleted,
                        onValueChange = { achievementsCompleted = it.filter { char -> char.isDigit() } },
                        label = { Text("CONQUISTAS FEITAS") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Yellow,
                            focusedLabelColor = GoldenYellow,
                            cursorColor = Yellow
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    OutlinedTextField(
                        value = totalAchievements,
                        onValueChange = { totalAchievements = it.filter { char -> char.isDigit() } },
                        label = { Text("TOTAL CONQUISTAS") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Yellow,
                            focusedLabelColor = GoldenYellow,
                            cursorColor = Yellow
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                OutlinedTextField(
                    value = completionPercentage,
                    onValueChange = {
                        val filtered = it.filter { char -> char.isDigit() }
                        if (filtered.isEmpty() || filtered.toInt() <= 100) {
                            completionPercentage = filtered
                        }
                    },
                    label = { Text("PORCENTAGEM DE CONCLUSÃO (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Yellow,
                        focusedLabelColor = GoldenYellow,
                        cursorColor = Yellow
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "STATUS DO JOGO",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkGray,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { selectedStatus = GameStatus.IN_PROGRESS },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedStatus == GameStatus.IN_PROGRESS) InProgressBlueBorder else Color.Gray.copy(alpha = 0.3f),
                                contentColor = if (selectedStatus == GameStatus.IN_PROGRESS) Color.White else Black
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("EM ANDAMENTO", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { selectedStatus = GameStatus.COMPLETED },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedStatus == GameStatus.COMPLETED) CompletedGreenBorder else Color.Gray.copy(alpha = 0.3f),
                                contentColor = if (selectedStatus == GameStatus.COMPLETED) Color.White else Black
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("FINALIZADO", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        border = BorderStroke(2.dp, Black),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("CANCELAR", color = Black, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank() && description.isNotBlank()) {
                                scope.launch {
                                    val gameEntity = GameEntity(
                                        id = editGame?.id ?: 0,
                                        name = name.trim(),
                                        description = description.trim(),
                                        achievementsCompleted = achievementsCompleted.toIntOrNull() ?: 0,
                                        totalAchievements = totalAchievements.toIntOrNull() ?: 0,
                                        completionPercentage = completionPercentage.toIntOrNull() ?: 0,
                                        status = selectedStatus.name,
                                        userId = currentUserId
                                    )

                                    if (editGame != null) {
                                        gameDao.updateGame(gameEntity)
                                    } else {
                                        gameDao.insertGame(gameEntity)
                                    }

                                    Toast.makeText(context, "Jogo salvo com sucesso!", Toast.LENGTH_SHORT).show()
                                    onSave()
                                }
                            } else {
                                Toast.makeText(context, "Preencha pelo menos nome e descrição!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .shadow(6.dp, RoundedCornerShape(24.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Yellow,
                            contentColor = Black
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("SALVAR", fontWeight = FontWeight.Bold)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}