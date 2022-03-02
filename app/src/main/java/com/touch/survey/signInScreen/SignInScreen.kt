package com.touch.survey.signInScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.touch.survey.R
import com.touch.survey.theme.snackbarAction
import com.touch.survey.util.*
import com.touch.survey.util.events.SignInEvent
import com.touch.survey.util.states.EmailState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignInScreen(onNavigationEvent: (SignInEvent) -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarErrorText = stringResource(id = R.string.snackbar_error)
    val snackbarActionLabel = stringResource(id = R.string.snackbar_label)


    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = stringResource(id = R.string.sign_in),
                onBackPressed = { onNavigationEvent(SignInEvent.NavigateBack) }
            )
        },

        content = {
            SignInSignUpScreenSegment(
                onSignedInAsGuest = { onNavigationEvent(SignInEvent.SignInAsGuest) },
                modifier = Modifier.supportWideScreen()

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    SignInContent(onSignInSubmitted = { email, password ->
                        onNavigationEvent(SignInEvent.SignIn(email, password))
                    })

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = snackbarErrorText,
                                    actionLabel = snackbarActionLabel
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.forgot_password))

                    }
                }
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ErrorSnackbar(
            snackbarHostState = snackbarHostState,
            onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun SignInSignUpTopAppBar(
    topAppBarText: String,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = topAppBarText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },

        navigationIcon = {
            IconButton(onClick = { onBackPressed }) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },

        actions = {
            Spacer(modifier = Modifier.width(68.dp))
        },

        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    )

}


@Composable
fun SignInSignUpScreenSegment(
    onSignedInAsGuest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable() () -> Unit
) {
    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(44.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                content()
            }


            Spacer(modifier = Modifier.height(16.dp))

            OrSignInAsGuest(
                onSignedInAsGuest = onSignedInAsGuest,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        }
    }
}


@Composable
fun SignInContent(
    onSignInSubmitted: (email: String, password: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val focusRequester = remember { FocusRequester() }
        val emailState = remember {
            EmailState()
        }
        Spacer(modifier = Modifier.height(16.dp))
        val passwordState = remember {
            PasswordState()
        }

        Password(label = stringResource(R.string.password),
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = { onSignInSubmitted(emailState.testText, passwordState.testText) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSignInSubmitted(emailState.testText, passwordState.testText) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid && passwordState.isValid
        ) {
            Text(
                text = stringResource(id = R.string.sign_in)
            )
        }

    }
}

@Composable
fun ErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2
                    )
                },
                action = {
                    data.actionLabel?.let {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(id = R.string.dismiss),
                                color = MaterialTheme.colors.snackbarAction
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}