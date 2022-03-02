package com.touch.survey.signInScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.touch.survey.R
import com.touch.survey.theme.MySurveyTheme

import com.touch.survey.util.events.SignInEvent

import com.touch.survey.util.navigation.Screen
import com.touch.survey.util.navigation.navigate

class SignInFragment : Fragment() {
    private val signInViewModel: SignInViewModel by viewModels<SignInViewModel> { SignInViewModelFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signInViewModel.navigateTo.observe(viewLifecycleOwner) {navigateToEvent ->
            navigateToEvent.goToIfNotCurrentlyActive()?.let { destinationScreen ->
                navigate(Screen.SignIn, destinationScreen)
            }
        }

        return ComposeView(requireContext()).apply {
            id = R.id.signInFragment
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContent {
                MySurveyTheme {
                   SignInScreen(onNavigationEvent = { event ->
                       when(event) {
                           is SignInEvent.SignIn -> {
                               signInViewModel.signIn(event.email, event.password)
                           }

                           is SignInEvent.SignUp -> {
                               signInViewModel.signUp()
                           }

                           is SignInEvent.SignInAsGuest -> {
                               signInViewModel.signInAsGuest()
                           }

                           else -> {
                               activity?.onBackPressedDispatcher?.onBackPressed()
                           }
                       }
                   }
                   )

                }
            }
        }
    }
}