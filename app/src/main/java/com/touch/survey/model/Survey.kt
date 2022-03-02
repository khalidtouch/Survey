package com.touch.survey.model

import androidx.annotation.StringRes

data class Survey(
    @StringRes val title: Int,
    val questions: List<Question>
)

data class SurveyResult(
    val library: String,
    @StringRes val result: Int,
    @StringRes val description: Int
)