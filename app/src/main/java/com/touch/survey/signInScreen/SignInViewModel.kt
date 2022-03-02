package com.touch.survey.signInScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.touch.survey.model.UserRepository
import com.touch.survey.util.events.NavigationEvent
import com.touch.survey.util.navigation.Screen

class SignInViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _navigateTo = MutableLiveData<NavigationEvent<Screen>>()
    val navigateTo: LiveData<NavigationEvent<Screen>> get() = _navigateTo

    /**
     * Consider all sign ins successful
     */
    fun signIn(email: String, password: String) {
        userRepository.signIn(email, password)
        _navigateTo.value = NavigationEvent(Screen.Survey)
    }


    fun signInAsGuest() {
        userRepository.signInAsGuest()
        _navigateTo.value = NavigationEvent(Screen.SignUp)
    }

    fun signUp() {
        _navigateTo.value = NavigationEvent(Screen.SignUp)
    }
}

class SignInViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(UserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}