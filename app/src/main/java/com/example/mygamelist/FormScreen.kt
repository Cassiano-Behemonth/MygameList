package com.example.mygamelist

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun FormScreen(
    games: SnapshotStateList<com.example.mygamelist.Game>,
    editIndex: Int?,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(editIndex?.let { games[it].name } ?: "") }
    var description by remember { mutableStateOf(editIndex?.let { games[it].description } ?: "") }
    var achievementsCompleted by remember { mutableStateOf(editIndex?.let { games[it].achievementsCompleted.toString() } ?: "0") }
    var totalAchievements by remember { mutableStateOf(editIndex?.let { games[it].totalAchievements.toString() } ?: "0") }
    var completionPercentage by remember { mutableStateOf(editIndex?.let { games[it].completionPercentage.toString() } ?: "0") }
    var selectedStatus by remember { mutableStateOf(editIndex?.let { games[it].status } ?: GameStatus.IN_PROGRESS) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GameTopAppBar(if (editIndex != null) "EDITAR JOGO" else "NOVO JOGO")

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
                                val achievementsCompletedInt = achievementsCompleted.toIntOrNull() ?: 0
                                val totalAchievementsInt = totalAchievements.toIntOrNull() ?: 0
                                val completionPercentageInt = completionPercentage.toIntOrNull() ?: 0

                                val newGame = Game(
                                    name = name.trim(),
                                    description = description.trim(),
                                    achievementsCompleted = achievementsCompletedInt,
                                    totalAchievements = totalAchievementsInt,
                                    completionPercentage = completionPercentageInt,
                                    status = selectedStatus
                                )

                                if (editIndex != null) {
                                    games[editIndex] = newGame
                                } else {
                                    games.add(newGame)
                                }
                                Toast.makeText(context, "Jogo salvo com sucesso!", Toast.LENGTH_SHORT).show()
                                onSave()
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