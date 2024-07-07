package com.example.chatroomapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatroomapp.screen.ChatRoomListScreen
import com.example.chatroomapp.screen.ChatRoomListScreen
import com.example.chatroomapp.screen.ChatScreen
import com.example.chatroomapp.screen.SignInScreen
import com.example.chatroomapp.screen.SignUpScreen
import com.example.chatroomapp.screen.WelcomeScreen
import com.example.chatroomapp.ui.theme.ChatRoomAppTheme
import com.example.chatroomapp.viewModels.AuthViewModel
import com.example.chatroomapp.viewModels.RoomViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val authViewModel :AuthViewModel = viewModel()
            ChatRoomAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    innerPadding -> Modifier.padding(innerPadding)
                    NavigationGraph(navController = navController, authViewModel)


                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(navController: NavHostController,
                    authViewModel: AuthViewModel
){
    NavHost(
        navController = navController,
        startDestination = Screen.WelcomeScreen.route
    ){
        composable(route=Screen.WelcomeScreen.route){
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate(Screen.LogInScreen.route) },
                onNavigateToSignUp = { navController.navigate(Screen.SignUpScreen.route) })
        }
        composable(route=Screen.SignUpScreen.route){
           SignUpScreen(
               authViewModel = authViewModel,onNavigateToLogin = { navController.navigate(Screen.LogInScreen.route) },
               onNaviGateBack = { navController.popBackStack() }

           )

        }

        composable(route = Screen.LogInScreen.route) {
            SignInScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignUpScreen.route) },
                onSignInSuccess = { navController.navigate(Screen.ChatRoomsScreen.route) },
                onNaviGateBack = { navController.popBackStack() } // Pop back to previous screen
            )
        }
        composable(Screen.ChatRoomsScreen.route) {
           val roomViewModel: RoomViewModel = viewModel()
            ChatRoomListScreen(
                roomViewModel = roomViewModel,
                onJoinClicked = { room ->
                    navController.navigate("${Screen.ChatScreen.route}/${room.id}")
                },
                onLogout = {
                    navController.navigate(Screen.WelcomeScreen.route)


                }
            )
        }
        composable("${Screen.ChatScreen.route}/{roomId}") {
            val roomId: String = it
                .arguments?.getString("roomId") ?: ""
            ChatScreen(roomId = roomId)
        }


    }
}




