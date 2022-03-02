package com.touch.survey.authentication.welcomeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.touch.survey.model.UserRepository
import com.touch.survey.util.events.NavigationEvent

import com.touch.survey.util.navigation.Screen

class WelcomeViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _navigateTo = MutableLiveData<NavigationEvent<Screen>>()
    val mNavigateTo: LiveData<NavigationEvent<Screen>> = _navigateTo

    fun signInAsGuest() {
        userRepository.signInAsGuest()
        _navigateTo.value = NavigationEvent(Screen.Survey)
    }


    /**
     * todo employ a dummy authentication
     */

    fun handleAuthentication(email: String) {
//        if(userRepository.isUserEmailKnown(email)) {
//            _navigateTo.value = NavigationEvent(Screen.SignIn)
//        } else {
//            _navigateTo.value = NavigationEvent(Screen.SignUp)
//        }
        _navigateTo.value = NavigationEvent(Screen.SignUp)
    }

}


class WelcomeViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel(UserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}