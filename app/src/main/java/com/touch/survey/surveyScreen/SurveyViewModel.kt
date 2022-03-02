package com.touch.survey.surveyScreen

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.touch.survey.model.AnswerFormat
import com.touch.survey.model.SurveyActionResult
import com.touch.survey.repositories.SurveyRepository
import com.touch.survey.util.PhotoUriManager
import com.touch.survey.util.states.QuestionState
import com.touch.survey.util.states.SurveyState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val simpleDateFormatPattern = "EEE, MMM d"

class SurveyViewModel(
    private val surveyRepository: SurveyRepository,
    private val photoUriManager: PhotoUriManager
) : ViewModel() {

    private val _uiState = MutableLiveData<SurveyState>()
    val uiState: LiveData<SurveyState> get() = _uiState

    var askForPermission by mutableStateOf(true)
        private set

    private lateinit var surveyInitialState: SurveyState

    //Uri used to save photos taken with the Camera
    private var uri: Uri? = null

    init {
        viewModelScope.launch {
            val survey = surveyRepository.getSurvey()

            // Create the default questions state based on the survey questions
            val questions: List<QuestionState> = survey.questions.mapIndexed { index, question ->
                val showPrevious = index > 0
                val showDone = index == survey.questions.size - 1
                QuestionState(
                    question = question,
                    questionIndex = index,
                    totalQuestionsCount = survey.questions.size,
                    showPrevious = showPrevious,
                    showDone = showDone
                )
            }
            surveyInitialState = SurveyState.Questions(
                survey.title, questions
            )
            _uiState.value = surveyInitialState
        }
    }

    fun computeResult(surveyQuestions: SurveyState.Questions) {
        val answers = surveyQuestions.questionsState.mapNotNull { it.answer }
        val result = surveyRepository.getSurveyResult(answers = answers)
        _uiState.value = SurveyState.Result(surveyQuestions.surveyTitle, result)
    }

    fun onDatePicked(questionId: Int, pickerSelection: Long?) {
        val selectedDate = Date().apply {
            time = pickerSelection ?: getCurrentDate(questionId)
        }
        val formattedDate =
            SimpleDateFormat(simpleDateFormatPattern,Locale.getDefault()).format(selectedDate)
        updateStateWithActionResult(questionId, SurveyActionResult.Date(formattedDate))
    }


    fun getCurrentDate(questionId: Int): Long {
        return getSelectedDate(questionId)
    }

    fun getSelectedDate(questionId: Int): Long {
        val latestState = _uiState.value
        var ret = Date().time
        if (latestState != null && latestState is SurveyState.Questions) {
            val question =
                latestState.questionsState.first { questionState ->
                    questionState.question.id == questionId
                }
            val answer: AnswerFormat.Action? = question.answer as AnswerFormat.Action?
            if (answer != null && answer.result is SurveyActionResult.Date) {
                val formatter = SimpleDateFormat(simpleDateFormatPattern, Locale.ENGLISH)
                val formatted = formatter.parse(answer.result.date)
                if (formatted is Date) ret = formatted.time
            }
        }
        return ret
    }

    fun getUriToSaveImage(): Uri? {
        uri = photoUriManager.buildNewUri()
        return uri
    }

    fun onImageSaved() {
        uri?.let { uri ->
            getLatestQuestionId()?.let { questionId ->
                updateStateWithActionResult(questionId, SurveyActionResult.Photo(uri))
            }
        }
    }

    private fun getLatestQuestionId(): Int? {
        val latestState = _uiState.value
        if (latestState != null && latestState is SurveyState.Questions) {
            return latestState.questionsState[latestState.currentQuestionIndex].question.id
        }
        return null
    }


    fun doNotAskForPermissions() {
        askForPermission = false
    }

    private fun updateStateWithActionResult(
        questionId: Int,
        result: SurveyActionResult
    ) {
        val latestState = _uiState.value
        if (latestState != null && latestState is SurveyState.Questions) {
            val question =
                latestState.questionsState.first { questionState ->
                    questionState.question.id == questionId
                }
            question.answer = AnswerFormat.Action(result)
            question.enableNext = true

        }
    }
}


class SurveyViewModelFactory(
    private val photoUriManager: PhotoUriManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SurveyViewModel::class.java)) {
            return SurveyViewModel(SurveyRepository, photoUriManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}