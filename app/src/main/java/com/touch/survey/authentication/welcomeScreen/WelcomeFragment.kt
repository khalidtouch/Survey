package com.touch.survey.authentication.welcomeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.touch.survey.theme.MySurveyTheme
import com.touch.survey.util.events.WelcomeEvent
import com.touch.survey.util.navigation.Screen
import com.touch.survey.util.navigation.navigate

class WelcomeFragment : Fragment() {

    private val welcomeViewModel: WelcomeViewModel by viewModels<WelcomeViewModel> {
        WelcomeViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        welcomeViewModel.mNavigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.goToIfNotCurrentlyActive()?.let { navigateTo ->
                navigate(Screen.Welcome, navigateTo)
            }

        }

        return ComposeView(requireContext()).apply {
            setContent {
                MySurveyTheme (darkTheme = false){
                    WelcomeScreen(
                        onEvent = { welcomeEvent ->
                            when (welcomeEvent) {
                                is WelcomeEvent.SignInSignUp -> welcomeViewModel.handleAuthentication(
                                    welcomeEvent.email
                                )
                                is WelcomeEvent.SignInAsGuest -> welcomeViewModel.signInAsGuest()
                            }
                        }
                    )
                }
            }
        }
    }
}