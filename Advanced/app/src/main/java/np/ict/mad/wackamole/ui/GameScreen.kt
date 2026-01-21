package np.ict.mad.wackamole.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import np.ict.mad.wackamole.data.AppDatabase
import np.ict.mad.wackamole.data.ScoreEntity
import np.ict.mad.wackamole.data.UserEntity
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    currentUser: UserEntity,
    db: AppDatabase,
    onViewLeaderboard: () -> Unit,
    onLogout: () -> Unit
)
 {
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var moleIndex by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var gameOver by remember { mutableStateOf(false) }

    // TIMER + SAVE SCORE
     LaunchedEffect(isRunning) {
         if (isRunning) {
             while (timeLeft > 0) {
                 delay(1000)
                 timeLeft--
             }

             isRunning = false
             gameOver = true

             // Run DB write off the main thread
             withContext(Dispatchers.IO) {
                 db.scoreDao().insertScore(
                     ScoreEntity(
                         userId = currentUser.userId,
                         score = score,
                         timestamp = System.currentTimeMillis()
                     )
                 )
             }
         }
     }


     // MOLE MOVEMENT
    LaunchedEffect(isRunning, gameOver) {
        if (isRunning && !gameOver) {
            while (isRunning && !gameOver) {
                delay((700..1000).random().toLong())
                moleIndex = (0..8).random()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Wack-a-Mole", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Score: $score")
            Text("Time: $timeLeft")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            for (r in 0..2) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    for (c in 0..2) {
                        val index = r * 3 + c
                        Button(
                            onClick = {
                                if (isRunning && index == moleIndex) {
                                    score++
                                    moleIndex = (0..8).random()
                                }
                            },
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(if (isRunning && index == moleIndex) "M" else "")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            score = 0
            timeLeft = 30
            moleIndex = (0..8).random()
            gameOver = false
            isRunning = true
        }) {
            Text(if (isRunning) "Restart" else "Start")
        }

        if (gameOver) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Game Over! Final Score: $score")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onViewLeaderboard) {
            Text("View Leaderboard")
        }



        Button(onClick = onLogout) {
            Text("Log Out")
        }

    }
}
