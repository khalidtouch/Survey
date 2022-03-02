package com.touch.survey.util.states

import java.util.regex.Pattern

private const val EMAIL_VALIDATION_REGEX = "^(.+)@(.+)\$"

class EmailState :
    TextFieldState(validator = ::isEmailValid, errorFor = ::emailValidatorError)

private fun isEmailValid(email: String): Boolean {
    return Pattern.matches(EMAIL_VALIDATION_REGEX, email)
}

/**
 * returns an error to be displayed or null if none thrown
 */

private fun emailValidatorError(email: String): String {
    return "Invalid Email: $email"
}