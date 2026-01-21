package np.ict.mad.wackamole.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import np.ict.mad.wackamole.data.AppDatabase
import np.ict.mad.wackamole.data.LeaderboardResult
import np.ict.mad.wackamole.data.UserEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    db: AppDatabase,
    currentUser: UserEntity,
    refreshKey: Int,
    onBack: () -> Unit
) {
    var personalBest by remember { mutableStateOf<Int?>(null) }
    var leaderboard by remember { mutableStateOf(emptyList<LeaderboardResult>()) }

    LaunchedEffect(refreshKey) {
        personalBest = db.scoreDao().getPersonalBest(currentUser.userId)
        leaderboard = db.scoreDao().getLeaderboard()
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Your Personal Best",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = personalBest?.toString() ?: "No scores yet",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Leaderboard",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(leaderboard) { entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(entry.username)
                        Text(entry.bestScore.toString())
                    }
                    Divider()
                }
            }
        }
    }
}
