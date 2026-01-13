package np.ict.mad.wackamole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import np.ict.mad.wackamole.ui.theme.WackAMoleTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment

import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WackAMoleTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "game"
                ) {
                    composable("game") {
                        GameScreen(onOpenSettings = { navController.navigate("settings") })
                    }
                    composable("settings") {
                        SettingsScreen(onBack = { navController.popBackStack() })
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(onOpenSettings: () -> Unit) {
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var moleIndex by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var highScore by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Wack-a-Mole") },
            actions = {
                IconButton(onClick = onOpenSettings) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Score: $score")
                Text("Time: $timeLeft")
            }

            Spacer(Modifier.height(8.dp))
            Text("High score: $highScore")

            Spacer(Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                for (r in 0..2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        for (c in 0..2) {
                            val index = r * 3 + c
                            Button(
                                onClick = {
                                    if (isRunning && index == moleIndex) {
                                        score += 1
                                        moleIndex = (0..8).random() // move mole immediately
                                    }
                                },
                                modifier = Modifier.size(88.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(if (isRunning && index == moleIndex) "M" else "")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(onClick = {
                score = 0
                timeLeft = 30
                moleIndex = (0..8).random()
                isRunning = true
            }) {
                Text(if (isRunning) "Restart" else "Start")
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Settings screen")
        }
    }
}
