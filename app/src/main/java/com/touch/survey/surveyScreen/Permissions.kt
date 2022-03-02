package com.touch.survey.surveyScreen

import QuestionHeader
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.touch.survey.R
import com.touch.survey.model.Question
import com.touch.survey.model.QuestionFormat


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsRationale(
    question: Question,
    multiplePermissionsState: MultiplePermissionsState,
    onDoNotAskForPermissions: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Spacer(modifier = Modifier.height(32.dp))
        QuestionHeader(title = question.questionText)
        Spacer(modifier = Modifier.height(32.dp))
        val rationaleId = question.permissionsRationaleText ?: R.string.permissions_rationale
        Text(text = stringResource(id = rationaleId)    )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }) {
            Text(text = stringResource(id = R.string.request_permissions))
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onDoNotAskForPermissions) {
            Text(text = stringResource(id = R.string.do_not_ask_permissions))
        }
        
    }
}


@Composable
fun PermissionsDenied(
    @StringRes title: Int,
    openSettings: () -> Unit,
    modifier: Modifier = Modifier 
    ) {
    Column(modifier) {
        Spacer(modifier = Modifier.height(32.dp))
        QuestionHeader(title = title)
        Spacer(modifier = Modifier.height(32.dp))
        Text(stringResource(id = R.string.permissions_denied))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = openSettings) {
            Text(text = stringResource(id = R.string.open_settings))
        }
    }
}