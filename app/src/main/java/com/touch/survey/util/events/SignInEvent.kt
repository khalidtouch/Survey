package com.touch.survey.util.events

sealed class SignInEvent {
    data class SignIn(val email: String, val password: String): SignInEvent()
    object SignUp: SignInEvent()
    object SignInAsGuest : SignInEvent()
    object NavigateBack : SignInEvent()
}