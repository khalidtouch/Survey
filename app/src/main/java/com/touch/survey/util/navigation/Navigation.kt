package com.touch.survey.util.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.touch.survey.R
import java.security.InvalidParameterException

enum class Screen { Welcome, SignUp, SignIn, Survey }

fun Fragment.navigate(from: Screen, to: Screen) {
    if(from == to) {
        throw InvalidParameterException("Can't navigate to $to")
    }

    when(to) {
        Screen.Welcome -> {
            findNavController().navigate(R.id.welcomeFragment)
        }

        Screen.SignIn -> {
            findNavController().navigate(R.id.signInFragment)
        }

        Screen.SignUp -> {
            findNavController().navigate(R.id.signUpFragment)
        }

        Screen.Survey -> {
            findNavController().navigate(R.id.surveyFragment)
        }

    }
}