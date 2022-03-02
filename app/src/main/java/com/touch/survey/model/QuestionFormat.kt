package com.touch.survey.model

import androidx.annotation.StringRes

sealed class QuestionFormat {
    data class SingleChoice(val optionsResource: List<Int>) : QuestionFormat()
    data class SingleChoiceIcon(val optionsIconResource: List<Pair<Int, Int>>) : QuestionFormat()
    data class MultipleChoice(val optionsResource: List<Int>) : QuestionFormat()
    data class MultipleChoiceIcon(val optionsIconResource: List<Pair<Int, Int>>) : QuestionFormat()

    data class Action(
        @StringRes val label: Int,
        val actionType: SurveyActionType
    ) : QuestionFormat()

    data class Slider(
        val range: ClosedFloatingPointRange<Float>,
        val steps: Int,
        @StringRes val startText: Int,
        @StringRes val endText: Int,
        @StringRes val neutralText: Int,
        val defaultValue: Float = 5.5f
    ): QuestionFormat()
}