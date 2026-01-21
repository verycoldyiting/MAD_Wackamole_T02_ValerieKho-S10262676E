package np.ict.mad.wackamole.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import np.ict.mad.wackamole.data.AppDatabase
import np.ict.mad.wackamole.data.UserEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    db: AppDatabase,
    onAuthSuccess: (UserEntity) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Sign In / Sign Up") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password / PIN") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        val user = db.userDao().login(username, password)
                        if (user != null) {
                            message = ""
                            onAuthSuccess(user)
                        } else {
                            message = "Invalid username or password"
                        }
                    }
                }
            ) {
                Text("Sign In")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        if (username.isBlank() || password.isBlank()) {
                            message = "Fields cannot be empty"
                            return@launch
                        }

                        val existing = db.userDao().findByUsername(username)
                        if (existing == null) {
                            db.userDao().insertUser(
                                UserEntity(username = username, password = password)
                            )
                            message = "Sign up successful. Please sign in."
                            password = ""
                        } else {
                            message = "Username already exists"
                        }
                    }
                }
            ) {
                Text("Sign Up")
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
