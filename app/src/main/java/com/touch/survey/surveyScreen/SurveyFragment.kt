package com.touch.survey.surveyScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.annotation.StringRes
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.touch.survey.R
import com.touch.survey.model.SurveyActionType
import com.touch.survey.theme.MySurveyTheme
import com.touch.survey.util.PhotoUriManager
import com.touch.survey.util.states.SurveyState

class SurveyFragment : Fragment() {

    private val surveyViewModel: SurveyViewModel by viewModels {
        SurveyViewModelFactory(PhotoUriManager(requireContext().applicationContext))
    }

    private val takePicture =
        registerForActivityResult(TakePicture()) { isPhotoSaved ->
            if (isPhotoSaved) {
                surveyViewModel.onImageSaved()
            } else {
                //todo if photo wasn't saved
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            id = R.id.signInFragment

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContent {
                MySurveyTheme(darkTheme = false) {
                    surveyViewModel.uiState.observeAsState().value?.let { surveyState ->
                        when (surveyState) {
                            is SurveyState.Questions -> SurveyQuestionsScreen(
                                questions = surveyState,
                                shouldAskPermissions = surveyViewModel.askForPermission,
                                onDoNotAskPermissions = { surveyViewModel.doNotAskForPermissions() },
                                onAction = { id, action -> handleSurveyAction(id, action) },
                                onDonePressed = { surveyViewModel.computeResult(surveyState) },
                                onBackPressed = { activity?.onBackPressedDispatcher?.onBackPressed() },
                                openSettings = {
                                    activity?.startActivity(
                                        Intent(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", context.packageName, null)
                                        )
                                    )
                                })

                            is SurveyState.Result -> SurveyResultScreen(result = surveyState,
                                onDonePressed = {
                                    activity?.onBackPressedDispatcher?.onBackPressed()
                                })

                        }
                    }
                }
            }
        }
    }


    private fun handleSurveyAction(questionId: Int, actionType: SurveyActionType) {
        when (actionType) {
            SurveyActionType.PICK_DATE -> showDatePicker(questionId = questionId)
            SurveyActionType.TAKE_PHOTO -> takeAPhoto()
            SurveyActionType.SELECT_CONTACT -> selectContact(questionId)
        }
    }

    private fun showDatePicker(questionId: Int) {
        val date = surveyViewModel.getCurrentDate(questionId = questionId)
        val picker = MaterialDatePicker.Builder.datePicker()
            .setSelection(date)
            .setTitleText(R.string.date_picker_title)
            .build()

        activity?.let {
            picker.show(it.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                surveyViewModel.onDatePicked(questionId, picker.selection)
            }
        }
    }

    private fun takeAPhoto() {
        takePicture.launch(surveyViewModel.getUriToSaveImage())
    }

    private fun selectContact(questionId: Int) {
        //todo UNSUPPORTED FOR NOW
    }
}