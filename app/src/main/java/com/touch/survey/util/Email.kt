package com.touch.survey.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.touch.survey.R
import com.touch.survey.util.states.EmailState
import com.touch.survey.util.states.TextFieldError
import com.touch.survey.util.states.TextFieldState


@Composable
fun Email(
    emailState: TextFieldState = remember { EmailState() },
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(value = emailState.testText,
        onValueChange = {
            emailState.testText = it
        },
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {

                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.body2
                )

            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                emailState.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    emailState.enableShowErrors()
                }
            },

        textStyle = MaterialTheme.typography.body2,
        isError = emailState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = { onImeAction() }
        )
    )
    emailState.getError()?.let { error -> TextFieldError(textError = error) }

}