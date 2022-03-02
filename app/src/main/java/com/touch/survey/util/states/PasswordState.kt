package com.touch.survey.util

import com.touch.survey.util.states.TextFieldState

class PasswordState :
    TextFieldState(validator = ::isPasswordValid, errorFor = ::passwordValidationError) {
}

class ConfirmPasswordState(private val passwordState: PasswordState) :
    TextFieldState() {
    override val isValid: Boolean
        get() = passwordAndConfirmationValid(passwordState.testText, testText)

    override fun getError(): String? {
        return if (showErrors()) {
            passwordConfirmationError()
        } else {
            null
        }
    }
}

private fun passwordAndConfirmationValid(password: String, confirmedPassword: String): Boolean {
    return isPasswordValid(password = password) && password == confirmedPassword
}

private fun isPasswordValid(password: String): Boolean {
    return password.length > 3
}

@Suppress("UNUSED_PARAMETER")
private fun passwordValidationError(password: String): String {
    return "Invalid password"
}

private fun passwordConfirmationError(): String {
    return "Passwords don't match"
}
