package com.touch.survey.repositories

import android.os.Build
import com.touch.survey.R
import com.touch.survey.model.*


object SurveyRepository {
    suspend fun getSurvey() = firstSurvey

    @Suppress("UNUSED_PARAMETER")
    fun getSurveyResult(answers: List<AnswerFormat<*>>): SurveyResult {
        return SurveyResult(
            library = "Compose",
            result = R.string.survey_result,
            description = R.string.survey_result_description
        )
    }
}

/**
 * Static data of Questions
 */
private val myQuestions = mutableListOf(
    Question(
        id = 1,
        questionText = R.string.question_one,
        format = QuestionFormat.MultipleChoice(
            optionsResource = listOf(
                R.string.work_out,
                R.string.dance,
                R.string.play_video_games,
                R.string.watch_tv
            )
        ),
        description = R.string.select_all
    ),

    Question(
        id = 2,
        questionText = R.string.your_favorite_fruit,
        format = QuestionFormat.SingleChoiceIcon(
            optionsIconResource = listOf(
                Pair(R.drawable.mango, R.string.mango),
                Pair(R.drawable.pineapple, R.string.pineapple),
                Pair(R.drawable.banana, R.string.banana),
                Pair(R.drawable.cashew, R.string.cashew)
            )
        ),
        description = R.string.select_one
    ),

    Question(
        id = 3,
        questionText = R.string.last_time,
        format = QuestionFormat.Action(
            label = R.string.pick_a_date,
            actionType = SurveyActionType.PICK_DATE
        ),
        description = R.string.select_date
    ),

    Question(
        id = 4,
        questionText = R.string.how_do_feel,
        format = QuestionFormat.Slider(
            range = 1f..10f,
            steps = 3,
            startText = R.string.start_text,
            endText = R.string.end_text,
            neutralText = R.string.neutral_text
        )
    )
).apply {
    if (Build.VERSION.SDK_INT >= 23) {
        add(
            Question(
                id = 975,
                questionText = R.string.do_you_mind,
                format = QuestionFormat.Action(
                    label = R.string.add_photo,
                    actionType = SurveyActionType.TAKE_PHOTO
                ),
                permissionsRequired =
                when (Build.VERSION.SDK_INT) {
                    in 23..28 -> listOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    else -> {
                        emptyList()
                    }
                },
                permissionsRationaleText = R.string.permission_rationale_text
            )
        )
    }
}.toList()


private val firstSurvey = Survey(
    title = R.string.about_you,
    questions = myQuestions
)