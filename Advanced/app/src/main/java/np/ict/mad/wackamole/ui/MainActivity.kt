package np.ict.mad.wackamole.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import np.ict.mad.wackamole.data.AppDatabase
import np.ict.mad.wackamole.data.UserEntity
import np.ict.mad.wackamole.ui.theme.WackAMoleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WackAMoleTheme {

                val navController = rememberNavController()
                val db = AppDatabase.getDatabase(this)

                var currentUser by remember {
                    mutableStateOf<UserEntity?>(null)
                }

                // ✅ refresh trigger
                var leaderboardRefresh by remember { mutableStateOf(0) }

                NavHost(
                    navController = navController,
                    startDestination = if (currentUser == null) "auth" else "game"
                ) {

                    composable("auth") {
                        AuthScreen(
                            db = db,
                            onAuthSuccess = {
                                currentUser = it
                                navController.navigate("game") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("game") {
                        GameScreen(
                            currentUser = currentUser!!,
                            db = db,
                            onViewLeaderboard = {
                                leaderboardRefresh++
                                navController.navigate("leaderboard")
                            },
                            onLogout = {
                                currentUser = null
                                navController.navigate("auth") {
                                    popUpTo("game") { inclusive = true }
                                }
                            }
                        )
                    }


                    composable("leaderboard") {
                        LeaderboardScreen(
                            db = db,
                            currentUser = currentUser!!,
                            refreshKey = leaderboardRefresh,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
