package com.example.mygamelist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygamelist.data.local.Game
import com.example.mygamelist.data.local.GameStatus


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopAppBar(title: String, showProfileIcon: Boolean = false, onProfileClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(blackGradient)
            .shadow(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (showProfileIcon) Arrangement.SpaceBetween else Arrangement.Center
        ) {
            if (showProfileIcon) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Yellow,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Yellow,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Yellow,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Yellow,
                    textAlign = TextAlign.Center
                )
            }
        }

        if (showProfileIcon) {
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .padding(top = 40.dp)
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Yellow.copy(alpha = 0.2f))
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = Yellow,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun GameCard(
    game: Game,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    val cardBackgroundColor = when (game.status) {
        GameStatus.IN_PROGRESS -> InProgressBlue
        GameStatus.COMPLETED -> CompletedGreen
    }

    val cardBorderColor = when (game.status) {
        GameStatus.IN_PROGRESS -> InProgressBlueBorder
        GameStatus.COMPLETED -> CompletedGreenBorder
    }

    val statusText = when (game.status) {
        GameStatus.IN_PROGRESS -> "EM ANDAMENTO"
        GameStatus.COMPLETED -> "FINALIZADO"
    }

    val statusColor = when (game.status) {
        GameStatus.IN_PROGRESS -> InProgressBlueBorder
        GameStatus.COMPLETED -> CompletedGreenBorder
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, cardBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = game.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Black,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(statusColor.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = statusColor
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = game.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkGray,
                        maxLines = 2
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(LightYellow)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = GoldenYellow,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFFFFEBEE))
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Deletar",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(
                        text = "CONQUISTAS",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = DarkGray
                    )
                    Text(
                        text = "${game.achievementsCompleted}/${game.totalAchievements}",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
                        color = Black
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "PROGRESSO",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = DarkGray
                    )
                    Text(
                        text = "${game.completionPercentage}%",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}