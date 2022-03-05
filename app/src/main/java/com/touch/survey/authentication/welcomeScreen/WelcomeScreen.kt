package com.touch.survey.authentication.welcomeScreen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.touch.survey.R
import com.touch.survey.theme.MySurveyTheme
import com.touch.survey.util.*
import com.touch.survey.util.events.WelcomeEvent
import com.touch.survey.util.states.EmailState

@Composable
fun WelcomeScreen(onEvent: (WelcomeEvent) -> Unit) {
    var brandingBottom by remember { mutableStateOf(0f) }
    var showBranding by remember { mutableStateOf(true) }
    var heightWithBranding by remember { mutableStateOf(0) }
    val currentOffsetHolder = remember { mutableStateOf(0f) }

    currentOffsetHolder.value = if (showBranding) 0f else -brandingBottom

    val currentOffsetHolderDp =
        with(LocalDensity.current) { currentOffsetHolder.value.toDp() }

    val heightDp =
        with(LocalDensity.current) { heightWithBranding.toDp() }

    Surface(
        modifier = Modifier.supportWideScreen()
    ) {
        val offset by animateDpAsState(targetValue = currentOffsetHolderDp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .brandingPreferredHeight(showBranding = showBranding, heightDp)
                .offset(y = offset)
                .onSizeChanged {
                    if (showBranding) heightWithBranding = it.height
                }
        ) {

            Branding(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .onGloballyPositioned {
                        if (brandingBottom == 0f) {
                            brandingBottom = it.boundsInParent().bottom
                        }
                    }
            )

            SignInAndCreateAccountSection(
                onEvent = onEvent,
                onFocusChange = { focused -> showBranding = !focused },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)

            )
        }

    }

}//


@Composable
private fun Branding(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)
    ) {

        Text(
            text = stringResource(id = R.string.branding_name),
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )
    }
}//


@Composable
private fun Logo(
    modifier: Modifier = Modifier,
    lightTheme: Boolean = MaterialTheme.colors.isLight
) {
    val assetId = if (lightTheme) {
        R.drawable.ic_logo_light
    } else {
        R.drawable.ic_logo_dark
    }
    Image(
        painter = painterResource(id = assetId),
        contentDescription = null,
        modifier = modifier
    )
}


@Preview(name = "Welcome Light Theme")
@Composable
fun WelcomeScreenPreviewLight() {
    MySurveyTheme {
        WelcomeScreen {}
    }
}

@Preview(name = "Welcome dark theme")
@Composable
fun WelcomeScreenPreviewDark() {
    MySurveyTheme(darkTheme = true) {
        WelcomeScreen {}
    }
}


@Composable
private fun SignInAndCreateAccountSection(
    onEvent: (WelcomeEvent) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val emailState = remember { EmailState() }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.medium
        ) {
            Text(
                text = stringResource(id = R.string.sign_in_create_account),
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 64.dp, bottom = 12.dp)
            )
        }

        val onSubmit = {
            if (emailState.isValid) {
                onEvent(WelcomeEvent.SignInSignUp(emailState.testText))
            } else {
                emailState.enableShowErrors()
            }
        }
        onFocusChange(emailState.isFocused)

        Email(
            emailState = emailState, imeAction = ImeAction.Done, onImeAction = onSubmit
        )

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 3.dp)
        ) {
            Text(
                text = stringResource(id = R.string.continue_sign_in),
                style = MaterialTheme.typography.subtitle2
            )
        }

        OrSignInAsGuest(
            onSignedInAsGuest = { onEvent(WelcomeEvent.SignInAsGuest) },
            modifier = Modifier.fillMaxWidth()

        )

    }


}