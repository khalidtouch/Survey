package com.touch.survey.model

import androidx.compose.runtime.Immutable
/**
 * Repository that holds the logged in user.
 *
 * In a production app, this class would also handle the communication with the backend for
 * sign in and sign up.
 */


object UserRepository {

    private var _user: User = User.NoUserLoggedIn
    val user: User get() = _user

    @Suppress("UNUSED_PARAMETER")
    fun signIn(email: String, password: String) {
        _user = User.LoggedInUser(email)
    }

    @Suppress("UNUSED_PARAMETER")
    fun signUp(email: String, password: String) {
        _user = User.LoggedInUser(email)
    }

    fun signInAsGuest() {
        _user = User.GuestUser
    }

    fun isUserEmailKnown(email: String): Boolean {
        return !email.contains("signup")
    }
}

sealed class User {
    @Immutable
    data class LoggedInUser(val email: String): User()
    object GuestUser: User()
    object NoUserLoggedIn : User()
}