package com.touch.survey.model

import androidx.annotation.StringRes

sealed class AnswerFormat<T : QuestionFormat> {
    object PermissionsDenied : AnswerFormat<Nothing>()
    data class SingleChoice(@StringRes val answer: Int) :
        AnswerFormat<QuestionFormat.SingleChoice>()

    data class MultipleChoice(val answersResource: Set<Int>) :
        AnswerFormat<QuestionFormat.MultipleChoice>()

    data class Action(val result: SurveyActionResult) : AnswerFormat<QuestionFormat.Action>()

    data class Slider(val answerValue: Float) : AnswerFormat<QuestionFormat.Slider>()
}


/**
 * Add or remove an answer from the list of selected answers depending on whether the answer was
 * selected or deselected.
 */

fun AnswerFormat.MultipleChoice.withAnswerSelected(
    @StringRes answer: Int,
    selected: Boolean
) : AnswerFormat.MultipleChoice {
    val newStringRes = answersResource.toMutableSet()
    if(!selected) {
        newStringRes.remove(answer)
    } else {
        newStringRes.add(answer)
    }

    return AnswerFormat.MultipleChoice(newStringRes)
}