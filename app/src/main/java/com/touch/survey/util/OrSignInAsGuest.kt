package com.touch.survey.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.touch.survey.R


@Composable
fun OrSignInAsGuest(
    onSignedInAsGuest: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material.Surface {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = stringResource(id = R.string.or_string),
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.paddingFromBaseline(top = 25.dp)
                )

            }

            OutlinedButton(
                onClick = onSignedInAsGuest,
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 20.dp, bottom = 24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in_as_guest)
                )
            }

        }

    }
}