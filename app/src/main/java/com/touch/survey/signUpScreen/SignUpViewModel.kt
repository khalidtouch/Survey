package com.touch.survey.signUpScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.touch.survey.model.UserRepository
import com.touch.survey.util.events.NavigationEvent
import com.touch.survey.util.navigation.Screen


class SignUpViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _navigateTo = MutableLiveData<NavigationEvent<Screen>>()
    val navigateTo: LiveData<NavigationEvent<Screen>> get() = _navigateTo

    /**
     * Consider all sign ups successful
     */
    fun signUp(email: String, password: String) {
        userRepository.signUp(email, password)
        _navigateTo.value = NavigationEvent(Screen.Survey)
    }

    fun signInAsGuest() {
        userRepository.signInAsGuest()
        _navigateTo.value = NavigationEvent(Screen.Survey)
    }

    fun signIn() {
        _navigateTo.value = NavigationEvent(Screen.SignIn)
    }
}


class SignUpViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(UserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}