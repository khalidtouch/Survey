package com.touch.survey.surveyScreen


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.touch.survey.R

import com.touch.survey.model.AnswerFormat
import com.touch.survey.model.QuestionFormat
import com.touch.survey.model.SurveyActionResult
import com.touch.survey.model.SurveyActionType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.seconds


@Composable
fun SliderQuestionStyle(
    questionFormat: QuestionFormat.Slider,
    answerFormat: AnswerFormat.Slider?,
    onAnswerSelected: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderPosition by remember {
        mutableStateOf(answerFormat?.answerValue ?: questionFormat.defaultValue)
    }

    Row(modifier = modifier) {
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                onAnswerSelected(it)
            },
            valueRange = questionFormat.range,
            steps = questionFormat.steps,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
    }

    Row {
        Text(
            text = stringResource(id = questionFormat.startText),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )

        Text(
            text = stringResource(id = questionFormat.neutralText),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )

        Text(
            text = stringResource(id = questionFormat.endText),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
    }
}


@Composable
fun SingleChoiceQuestionFormatStyle(
    questionFormat: QuestionFormat.SingleChoice,
    answerFormat: AnswerFormat.SingleChoice?,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = questionFormat.optionsResource.associateBy {
        stringResource(id = it)
    }

    val radioOptions = options.keys.toList()

    val selected = if (answerFormat != null) {
        stringResource(id = answerFormat.answer)
    } else {
        null
    }

    val (selectedOption, onOptionsSelected) = remember(answerFormat) {
        mutableStateOf(selected)
    }

    Column(modifier = modifier) {
        radioOptions.forEach { text ->
            val onClickHandler = {
                onOptionsSelected(text)
                options[text]?.let { onAnswerSelected(it) }
                Unit
            }

            val optionsSelected = text == selectedOption

            val answerBorderColor = if (optionsSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            }

            val answerBackgroundColor = if (optionsSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colors.background
            }

            androidx.compose.material.Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = answerBorderColor
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = optionsSelected,
                            onClick = onClickHandler
                        )
                        .background(answerBackgroundColor)
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = text)

                    RadioButton(
                        selected = optionsSelected, onClick = onClickHandler,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary
                        )
                    )

                }
            }
        }
    }
}


@Composable
fun SingleChoiceIconQuestionStyle(
    questionFormat: QuestionFormat.SingleChoiceIcon,
    answerFormat: AnswerFormat.SingleChoice?,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = questionFormat.optionsIconResource.associateBy { stringResource(id = it.second) }

    val radioOptions = options.keys.toList()

    val selected = if (answerFormat != null) {
        stringResource(id = answerFormat.answer)
    } else {
        null
    }

    val (selectedOption, onOptionSelected) = remember(answerFormat) {
        mutableStateOf(selected)
    }

    Column(modifier = modifier) {
        radioOptions.forEach { text ->
            val onClickHandler = {
                onOptionSelected(text)
                options[text]?.let { onAnswerSelected(it.second) }
                Unit
            }

            val optionsSelected = text == selectedOption

            val answerBorderColor = if (optionsSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            }

            val answerBackgroundColor = if (optionsSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colors.background
            }


            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = answerBackgroundColor
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = optionsSelected,
                            onClick = onClickHandler
                        )
                        .background(answerBackgroundColor)
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    options[text]?.let {
                        Image(
                            painter = painterResource(id = it.first), contentDescription = null,
                            modifier = Modifier
                                .width(56.dp)
                                .height(56.dp)
                                .clip(MaterialTheme.shapes.medium)
                        )
                    }

                    Text(
                        text = text
                    )

                    RadioButton(
                        selected = optionsSelected,
                        onClick = onClickHandler,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary
                        )
                    )
                }

            }
        }
    }

}


@Composable
fun MultipleChoiceQuestionStyle(
    questionFormat: QuestionFormat.MultipleChoice,
    answerFormat: AnswerFormat.MultipleChoice?,
    onAnswerSelected: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = questionFormat.optionsResource.associateBy { stringResource(id = it) }

    Column(modifier = modifier) {
        for (option in options) {
            var checkedState by remember(answerFormat) {
                val selectedOption = answerFormat?.answersResource?.contains(option.value)
                mutableStateOf(selectedOption ?: false)
            }

            val answerBorderColor = if (checkedState) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.primary.copy(alpha = 0.12f)
            }

            val answerBackgroundColor = if (checkedState) {
                MaterialTheme.colors.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colors.background
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = answerBorderColor
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(answerBackgroundColor)
                        .clickable(
                            onClick = {
                                checkedState = !checkedState
                                onAnswerSelected(option.value, checkedState)
                            }
                        )
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = option.key
                    )

                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = { selected ->
                            checkedState = selected
                            onAnswerSelected(option.value, selected)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primary
                        )
                    )
                }
            }

        }

    }
}


@Composable
fun MultipleChoiceIconQuestionStyle(
    questionFormat: QuestionFormat.MultipleChoiceIcon,
    answerFormat: AnswerFormat.MultipleChoice?,
    onAnswerSelected: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = questionFormat.optionsIconResource.associateBy { stringResource(id = it.second) }

    Column(modifier = modifier) {
        for (option in options) {
            var checkedState by remember(answerFormat) {
                val selectedOption = answerFormat?.answersResource?.contains(option.value.second)
                mutableStateOf(selectedOption ?: false)
            }

            val answerBorderColor = if (checkedState) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            }

            val answerBackgroundColor = if (checkedState) {
                MaterialTheme.colors.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colors.background
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = answerBorderColor
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            checkedState = !checkedState
                            onAnswerSelected(option.value.second, checkedState)
                        }
                    )
                    .background(answerBackgroundColor)
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {

                    Image(
                        painter = painterResource(id = option.value.first),
                        contentDescription = null,
                        modifier = Modifier
                            .width(56.dp)
                            .height(56.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )

                    Text(
                        text = option.key
                    )

                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = { selected ->
                            checkedState = selected
                            onAnswerSelected(option.value.second, selected)
                        },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary)
                    )
                }

            }
        }
    }
}


@Composable
fun ActionQuestionStyle(
    questionId: Int,
    questionFormat: QuestionFormat.Action,
    answerFormat: AnswerFormat.Action?,
    onAction: (Int, SurveyActionType) -> Unit,
    modifier: Modifier = Modifier
) {
    when (questionFormat.actionType) {
        SurveyActionType.PICK_DATE -> {

            DateQuestionStyle(
                questionId = questionId,
                answerFormat = answerFormat,
                onAction = onAction,
                modifier = modifier
            )
        }

        SurveyActionType.TAKE_PHOTO -> {

            PhotoQuestionStyle(
                questionId = questionId,
                answerFormat = answerFormat,
                onAction = onAction,
                modifier = modifier
            )
        }

        SurveyActionType.SELECT_CONTACT -> {
            //todo select a contact
        }
    }
}


@Composable
fun PhotoDefaultImage(
    modifier: Modifier = Modifier,
    lightTheme: Boolean = MaterialTheme.colors.isLight
) {
    val assetId = if (lightTheme) {
        R.drawable.ic_selfie_light
    } else {
        R.drawable.ic_selfie_dark
    }

    Image(
        painter = painterResource(id = assetId),
        modifier = modifier,
        contentDescription = null
    )
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun PhotoQuestionStyle(
    questionId: Int,
    answerFormat: AnswerFormat.Action?,
    onAction: (Int, SurveyActionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val resource = if (answerFormat != null) {
        Icons.Filled.SwapHoriz
    } else {
        Icons.Filled.AddAPhoto
    }

    OutlinedButton(
        onClick = {
            onAction(questionId, SurveyActionType.TAKE_PHOTO)
        },
        modifier = modifier,
        contentPadding = PaddingValues()
    ) {

        Column {
            if (answerFormat != null && answerFormat.result is SurveyActionResult.Photo) {
                Image(
                    painter = rememberImagePainter(
                        data = answerFormat.result.uri,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(96.dp)
                        .aspectRatio(4 / 3f)
                )

                Text(
                    text = stringResource(id = R.string.photo_succeeded),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 20.dp)
                )
            } else {
                PhotoDefaultImage(modifier = Modifier.padding(horizontal = 86.dp, vertical = 74.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomCenter)
                    .padding(vertical = 26.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = resource, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(
                        id = if (answerFormat != null) {
                            R.string.retake_photo
                        } else {
                            R.string.add_photo
                        }
                    )
                )
            }
        }
    }
}

@Composable
fun DateQuestionStyle(
    questionId: Int,
    answerFormat: AnswerFormat.Action?,
    onAction: (Int, SurveyActionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val date = if (answerFormat != null && answerFormat.result is SurveyActionResult.Date) {
        answerFormat.result.date
    } else {
        SimpleDateFormat(simpleDateFormatPattern, Locale.getDefault()).format(Date())
    }

    Button(
        onClick = { onAction(questionId, SurveyActionType.PICK_DATE) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.onSecondary
        ),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(vertical = 20.dp)
            .height(54.dp),
        elevation = ButtonDefaults.elevation(2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
    ) {
        Text(
            text = date,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )

        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
        )
    }

}
