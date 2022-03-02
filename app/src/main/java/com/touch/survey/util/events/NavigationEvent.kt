package com.touch.survey.util.events

data class NavigationEvent<out T>(private val destination: T) {
    var isCurrentlyActive = false
        private set //allow external read but not write

    /**
     * returns the content and prevents its re-use
     */
    fun goToIfNotCurrentlyActive(): T? {
        return if (isCurrentlyActive) {
            null
        } else {
            isCurrentlyActive = true
            destination
        }
    }

    /**
     * return the content even after handled
     */
    fun peekDestination(): T = destination
}


