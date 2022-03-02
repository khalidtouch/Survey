import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.touch.survey.R
import com.touch.survey.model.*
import com.touch.survey.surveyScreen.*
import com.touch.survey.theme.MySurveyTheme


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QuestionSection(
    question: Question,
    answerFormat: AnswerFormat<*>?,
    shouldAskPermissions: Boolean,
    onAnswer: (AnswerFormat<*>) -> Unit,
    onAction: (Int, SurveyActionType) -> Unit,
    onDoNotAskForPermissions: () -> Unit,
    openSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (question.permissionsRequired.isEmpty()) {
        QuestionBody(
            question = question,
            answerFormat = answerFormat,
            onAnswer = onAnswer,
            onAction = onAction,
            modifier = modifier
        )
    } else {
        val permissionsContentModifier = modifier.padding(horizontal = 20.dp)
        val multiplePermissionsState =
            rememberMultiplePermissionsState(permissions = question.permissionsRequired)

        when {
            // If all permissions are granted, then show the question
            multiplePermissionsState.allPermissionsGranted -> {
                QuestionBody(
                    question = question,
                    answerFormat = answerFormat,
                    onAnswer = onAnswer,
                    onAction = onAction,
                    modifier = modifier
                )
            }

            multiplePermissionsState.shouldShowRationale ||
                    !multiplePermissionsState.permissionRequested -> {
                if (!shouldAskPermissions) {
                    PermissionsDenied(
                        title = question.questionText,
                        openSettings = openSettings,
                        permissionsContentModifier
                    )
                } else {
                    PermissionsRationale(
                        question = question,
                        multiplePermissionsState = multiplePermissionsState,
                        onDoNotAskForPermissions = onDoNotAskForPermissions,
                        permissionsContentModifier
                    )
                }
            }

            else -> {
                PermissionsDenied(
                    title = question.questionText,
                    openSettings = openSettings,
                    permissionsContentModifier
                )

                LaunchedEffect(true) {
                    onDoNotAskForPermissions()
                }
            }
        }

        // If permissions are denied, inform the caller that can move to the next question
        if (!shouldAskPermissions) {
            LaunchedEffect(true) {
                onAnswer(AnswerFormat.PermissionsDenied)
            }
        }
    }

}


@Composable
fun QuestionBody(
    question: Question,
    answerFormat: AnswerFormat<*>?,
    onAnswer: (AnswerFormat<*>) -> Unit,
    onAction: (Int, SurveyActionType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(32.dp))
            QuestionHeader(title = question.questionText)
            Spacer(modifier = Modifier.height(24.dp))
            if (question.description != null) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = stringResource(id = question.description),
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(bottom = 18.dp, start = 8.dp, end = 8.dp)
                    )
                }
            }

            when (question.format) {
                is QuestionFormat.SingleChoice -> SingleChoiceQuestionFormatStyle(
                    questionFormat = question.format,
                    answerFormat = answerFormat as AnswerFormat.SingleChoice?,
                    onAnswerSelected = { answerFormat ->
                        onAnswer(
                            AnswerFormat.SingleChoice(
                                answerFormat
                            )
                        )
                    },
                    modifier = Modifier.fillParentMaxWidth()
                )

                is QuestionFormat.SingleChoiceIcon -> SingleChoiceIconQuestionStyle(
                    questionFormat = question.format,
                    answerFormat = answerFormat as AnswerFormat.SingleChoice?,
                    onAnswerSelected = { answerFormat ->
                        onAnswer(
                            AnswerFormat.SingleChoice(
                                answerFormat
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                is QuestionFormat.MultipleChoice -> MultipleChoiceQuestionStyle(
                    questionFormat = question.format,
                    answerFormat = answerFormat as AnswerFormat.MultipleChoice?,
                    onAnswerSelected = { newAnswer, selected ->
                        if (answerFormat == null) {
                            onAnswer(AnswerFormat.MultipleChoice(setOf(newAnswer)))
                        } else {
                            onAnswer(answerFormat.withAnswerSelected(newAnswer, selected))
                        }
                    },
                    modifier = Modifier.fillParentMaxWidth()
                )

                is QuestionFormat.MultipleChoiceIcon -> MultipleChoiceIconQuestionStyle(
                    questionFormat = question.format,
                    answerFormat = answerFormat as AnswerFormat.MultipleChoice?,
                    onAnswerSelected = { newAnswer, selected ->
                        // create the answer if it doesn't exist or
                        // update it based on the user's selection
                        if (answerFormat == null) {
                            onAnswer(AnswerFormat.MultipleChoice(setOf(newAnswer)))
                        } else {
                            onAnswer(answerFormat.withAnswerSelected(newAnswer, selected))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                is QuestionFormat.Action -> ActionQuestionStyle(
                    questionId = question.id,
                    questionFormat = question.format,
                    answerFormat = answerFormat as AnswerFormat.Action?,
                    onAction = onAction,
                    modifier = Modifier.fillParentMaxWidth()
                )

                is QuestionFormat.Slider -> SliderQuestionStyle(
                    questionFormat = question.format,
                    answerFormat = answerFormat as AnswerFormat.Slider?,
                    onAnswerSelected = { onAnswer(AnswerFormat.Slider(it)) },
                    modifier = Modifier.fillParentMaxWidth()
                )
            }
        }
    }
}

@Composable
fun QuestionHeader(@StringRes title: Int) {
    val backgroundColor = if (MaterialTheme.colors.isLight) {
        MaterialTheme.colors.onSurface.copy(alpha = 0.04f)
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.06f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        )
    }

}

@Preview
@Composable
fun QuestionPreview() {
    val question = Question(
        id = 2,
        questionText = R.string.your_favorite_fruit,
        format = QuestionFormat.SingleChoice(
            optionsResource = listOf(
                R.string.mango,
                R.string.banana,
                R.string.pineapple,
                R.string.cashew
            )
        ),
        description = R.string.select_one
    )

    MySurveyTheme {
        QuestionSection(
            question = question,
            answerFormat = null,
            shouldAskPermissions = true,
            onAnswer = {},
            onAction = { _, _ -> },
            onDoNotAskForPermissions = { },
            openSettings = { })
    }
}



