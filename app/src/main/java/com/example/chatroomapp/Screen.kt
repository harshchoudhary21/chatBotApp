package com.example.chatroomapp

sealed class Screen(val route: String) {
    object LogInScreen : Screen("loginscreen")
    object SignUpScreen : Screen("signupscreen")
    object ChatScreen : Screen("chatscreen")
    object ChatRoomsScreen:Screen("chatroomscreen")
    object WelcomeScreen : Screen("welcomescreen")
}
