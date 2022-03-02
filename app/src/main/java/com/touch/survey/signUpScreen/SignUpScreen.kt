package com.touch.survey.signUpScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.touch.survey.R
import com.touch.survey.signInScreen.SignInSignUpScreenSegment
import com.touch.survey.signInScreen.SignInSignUpTopAppBar
import com.touch.survey.theme.MySurveyTheme
import com.touch.survey.util.*
import com.touch.survey.util.events.SignUpEvent
import com.touch.survey.util.states.EmailState


@Composable
fun SignUpScreen(onNavigationEvent: (SignUpEvent) -> Unit) {
    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(topAppBarText = stringResource(R.string.create_account),
                onBackPressed = { onNavigationEvent(SignUpEvent.NavigateBack) })
        },
        content = {
            SignInSignUpScreenSegment(
                onSignedInAsGuest = { onNavigationEvent(SignUpEvent.SignInAsGuest) },
                modifier = Modifier.supportWideScreen()
            ) {
                Column {
                    //todo SignUpContent

                }
            }
        }

    )
}


@Composable
fun SignUpContent(
    onSignUpSubmitted: (email: String, password: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val passwordFocusRequest = remember { FocusRequester() }
        val confirmationPasswordFocusRequest = remember { FocusRequester() }
        val emailState = remember { EmailState() }
        Email(emailState, onImeAction = { passwordFocusRequest.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))
        val passwordState = remember { PasswordState() }
        Password(
            label = stringResource(id = R.string.password),
            passwordState = passwordState,
            imeAction = ImeAction.Next,
            onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
            modifier = Modifier.focusRequester(passwordFocusRequest)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val confirmPasswordState = remember { ConfirmPasswordState(passwordState = passwordState) }
        Password(
            label = stringResource(R.string.confirm_password),
            passwordState = confirmPasswordState,
            onImeAction = { onSignUpSubmitted(emailState.testText, passwordState.testText) },
            modifier = Modifier.focusRequester(confirmationPasswordFocusRequest)
        )

        Spacer(modifier = Modifier.height(16.dp))


        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = stringResource(R.string.terms_and_conditions),
                style = MaterialTheme.typography.caption
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSignUpSubmitted(emailState.testText, passwordState.testText) },
            modifier = Modifier.fillMaxWidth(),
            enabled = emailState.isValid && passwordState.isValid && confirmPasswordState.isValid
        ) {
            Text(text = stringResource(id = R.string.create_account))
        }

    }

}


@Preview(widthDp = 1024)
@Composable
fun SignUpPreview() {
    MySurveyTheme {
        SignUpScreen {}
    }
}