package com.example.mygamelist.ui.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.example.mygamelist.*
import com.example.mygamelist.data.local.User

@Composable
fun ProfileScreen(
    user: User?,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GameTopAppBar("PERFIL DO JOGADOR")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(75.dp))
                    .background(blackGradient)
                    .shadow(20.dp, RoundedCornerShape(75.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Yellow,
                    modifier = Modifier.size(90.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "BEM-VINDO",
                style = MaterialTheme.typography.headlineMedium,
                color = DarkGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = user?.username?.uppercase() ?: "JOGADOR",
                style = MaterialTheme.typography.headlineLarge,
                color = Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, LightYellow)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(yellowGradient)
                    )

                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "INFORMAÇÕES DA CONTA",
                            style = MaterialTheme.typography.titleLarge,
                            color = Black,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "USUÁRIO:",
                                style = MaterialTheme.typography.bodyLarge,
                                color = DarkGray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = user?.username ?: "N/A",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Black
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "STATUS:",
                                style = MaterialTheme.typography.bodyLarge,
                                color = DarkGray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ONLINE",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    border = BorderStroke(2.dp, Black),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("VOLTAR", color = Black, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(6.dp, RoundedCornerShape(24.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("LOGOUT", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}