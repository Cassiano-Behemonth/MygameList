package com.example.mygamelist.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.mygamelist.Black
import com.example.mygamelist.GameCard
import com.example.mygamelist.GameTopAppBar
import com.example.mygamelist.Yellow
import com.example.mygamelist.data.local.GameEntity
import com.example.mygamelist.data.local.User
import com.example.mygamelist.data.local.toGame

@Composable
fun HomeScreen(
    games: List<GameEntity>,
    registeredUser: User?,
    onAddClick: () -> Unit,
    onProfileClick: () -> Unit,
    onEditClick: (GameEntity) -> Unit,
    onDeleteClick: (GameEntity) -> Unit
) {
    Scaffold(
        topBar = {
            GameTopAppBar(
                title = "MEUS JOGOS",
                showProfileIcon = true,
                onProfileClick = onProfileClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier
                    .size(64.dp)
                    .shadow(12.dp, RoundedCornerShape(32.dp)),
                containerColor = Yellow,
                contentColor = Black,
                shape = RoundedCornerShape(32.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Adicionar Jogo",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(games) { gameEntity ->
                GameCard(
                    game = gameEntity.toGame(),
                    onEditClick = { onEditClick(gameEntity) },
                    onDeleteClick = { onDeleteClick(gameEntity) }
                )
            }
        }
    }
}