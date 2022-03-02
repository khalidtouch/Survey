package com.touch.survey.signUpScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.touch.survey.R
import com.touch.survey.theme.MySurveyTheme

import com.touch.survey.util.events.SignUpEvent
import com.touch.survey.util.navigation.Screen
import com.touch.survey.util.navigation.navigate


class SignUpFragment : Fragment() {

    private val signUpViewModel: SignUpViewModel by viewModels<SignUpViewModel> { SignUpViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signUpViewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.goToIfNotCurrentlyActive()?.let { navigateTo ->
                navigate(Screen.SignUp, navigateTo)
            }
        }

        return ComposeView(requireContext()).apply {
            id = R.id.signUpFragment
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                MySurveyTheme {

                    SignUpScreen(onNavigationEvent = { event ->
                        when (event) {
                            is SignUpEvent.SignUp -> {
                                signUpViewModel.signUp(event.email, event.password)
                            }

                            is SignUpEvent.SignIn -> {
                                signUpViewModel.signIn()
                            }

                            is SignUpEvent.SignInAsGuest -> {
                                signUpViewModel.signInAsGuest()
                            }

                            else -> {
                                activity?.onBackPressedDispatcher?.onBackPressed()
                            }
                        }
                    })
                }
            }
        }
    }
}