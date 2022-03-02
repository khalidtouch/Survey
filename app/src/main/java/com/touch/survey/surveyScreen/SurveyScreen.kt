package com.touch.survey.surveyScreen

import QuestionSection
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import com.touch.survey.R
import com.touch.survey.model.AnswerFormat
import com.touch.survey.model.SurveyActionType
import com.touch.survey.theme.progressIndicatorBackground
import com.touch.survey.util.states.QuestionState
import com.touch.survey.util.states.SurveyState
import com.touch.survey.util.supportWideScreen
import java.time.temporal.TemporalAdjusters.previous

@Composable
fun SurveyBottomBar(
    questionState: QuestionState,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit
) {
    Surface(
        elevation = 12.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            if (questionState.showPrevious) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onPreviousPressed
                ) {
                    Text(text = stringResource(id = R.string.previous))
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            if (questionState.showDone) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onDonePressed,
                    enabled = questionState.enableNext
                ) {
                    Text(text = stringResource(id = R.string.done))
                }
            } else {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = onNextPressed,
                    enabled = questionState.enableNext
                ) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    }
}


@OptIn(ExperimentalUnitApi::class)
@Composable
fun TopAppBarTitle(
    questionIndex: Int,
    totalQuestionsCount: Int,
    modifier: Modifier = Modifier
) {
    val indexStyle = MaterialTheme.typography.caption.toSpanStyle().copy(
        fontWeight = FontWeight.Bold
    )
    val totalStyle = MaterialTheme.typography.caption.toSpanStyle()
    val text = buildAnnotatedString {
        withStyle(style = indexStyle) {
            append("${questionIndex + 1}")
        }
        withStyle(style = totalStyle) {
            append(stringResource(R.string.question_count, totalQuestionsCount))
        }
    }
    Text(
        text = text,
        fontSize = TextUnit(value = 12f, type = TextUnitType.Sp),
        style = MaterialTheme.typography.caption,
        modifier = modifier
    )
}


@Composable
fun SurveyTopAppBar(
    questionIndex: Int,
    totalQuestionsCount: Int,
    onBackPressed: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {

            TopAppBarTitle(
                questionIndex = questionIndex,
                totalQuestionsCount = totalQuestionsCount,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .align(Alignment.Center)
            )

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                        .fillMaxWidth()
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
        val animatedProgress by animateFloatAsState(
            targetValue = (questionIndex + 1) / totalQuestionsCount.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )

        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(3.dp)
                .zIndex(12f),
            backgroundColor = MaterialTheme.colors.progressIndicatorBackground
        )

    }
}

@Composable
fun SurveyResult(
    result: SurveyState.Result,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Spacer(modifier = modifier.height(44.dp))
            Text(
                text = result.surveyResult.library,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Text(
                text = stringResource(
                    id = result.surveyResult.result,
                    result.surveyResult.library
                ),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(20.dp)
            )

            Text(
                text = stringResource(id = result.surveyResult.description),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}


@Composable
fun SurveyResultScreen(
    result: SurveyState.Result,
    onDonePressed: () -> Unit
) {
    Surface(modifier = Modifier.supportWideScreen()) {
        Scaffold(
            content = { innerPadding ->
                val modifier = Modifier.padding(innerPadding)
                SurveyResult(result = result, modifier = modifier)
            },
            bottomBar = {
                OutlinedButton(
                    onClick = { onDonePressed() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Text(text = stringResource(id = R.string.done))

                }
            }
        )
    }
}


@Composable
fun SurveyQuestionsScreen(
    questions: SurveyState.Questions,
    shouldAskPermissions: Boolean,
    onDoNotAskPermissions: () -> Unit,
    onAction: (Int, SurveyActionType) -> Unit,
    onDonePressed: () -> Unit,
    onBackPressed: () -> Unit,
    openSettings: () -> Unit
) {

    val questionState = remember(questions.currentQuestionIndex) {
        questions.questionsState[questions.currentQuestionIndex]
    }

    Surface(modifier = Modifier.supportWideScreen()) {
        Scaffold(
            topBar = {
                SurveyTopAppBar(
                    questionIndex = questionState.questionIndex,
                    totalQuestionsCount = questionState.totalQuestionsCount,
                    onBackPressed = onBackPressed
                )
            },
            content = { innerPadding ->
                QuestionSection(
                    question = questionState.question,
                    answerFormat = questionState.answer,
                    shouldAskPermissions = shouldAskPermissions,
                    onAnswer = {
                        if (it !is AnswerFormat.PermissionsDenied) {
                            questionState.answer = it
                        }
                        questionState.enableNext = true
                    },
                    onAction = onAction,
                    onDoNotAskForPermissions = onDoNotAskPermissions,
                    openSettings = openSettings,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            },
            bottomBar = {
                SurveyBottomBar(
                    questionState = questionState,
                    onPreviousPressed = { questions.currentQuestionIndex-- },
                    onNextPressed = { questions.currentQuestionIndex++ },
                    onDonePressed = onDonePressed
                )
            }
        )
    }

}
