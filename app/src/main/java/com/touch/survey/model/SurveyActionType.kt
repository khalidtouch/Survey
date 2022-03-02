package com.touch.survey.model

import android.net.Uri

enum class SurveyActionType { PICK_DATE, TAKE_PHOTO, SELECT_CONTACT }

sealed class SurveyActionResult {
    data class Date(val date: String): SurveyActionResult()
    data class Photo(val uri: Uri): SurveyActionResult()
    data class Contact(val contact: String): SurveyActionResult()
}