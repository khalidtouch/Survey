package com.touch.survey.model

import androidx.annotation.StringRes

data class Question(
    val id: Int,
    @StringRes val questionText: Int,
    val format: QuestionFormat,
    @StringRes val description: Int? = null,
    val permissionsRequired: List<String> = emptyList(),
    @StringRes val permissionsRationaleText: Int? = null
) {
}