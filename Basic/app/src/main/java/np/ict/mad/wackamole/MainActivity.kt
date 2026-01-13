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
                Text("Score: 0")
                Text("Time: 30")
            }

            Spacer(Modifier.height(8.dp))
            Text("High score: 0")

            Spacer(Modifier.height(20.dp))

            // 3x3 grid (UI only for now)
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                repeat(3) {
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        repeat(3) {
                            Button(
                                onClick = { },
                                modifier = Modifier.size(88.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp)
                            ) { Text("") }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(onClick = { }) {
                Text("Start")
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
