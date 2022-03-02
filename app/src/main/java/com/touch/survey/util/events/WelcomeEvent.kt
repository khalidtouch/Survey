package com.touch.survey.util.events

sealed class WelcomeEvent {
    data class SignInSignUp(val email: String): WelcomeEvent()
    object SignInAsGuest : WelcomeEvent()
}