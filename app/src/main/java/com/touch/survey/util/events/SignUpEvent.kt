package com.touch.survey.util.events

sealed class SignUpEvent {
    data class SignUp(val email: String, val password: String): SignUpEvent()
    object SignIn: SignUpEvent()
    object SignInAsGuest : SignUpEvent()
    object NavigateBack : SignUpEvent()
}