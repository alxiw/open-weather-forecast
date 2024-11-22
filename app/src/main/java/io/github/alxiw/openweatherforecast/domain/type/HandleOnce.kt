package io.github.alxiw.openweatherforecast.domain.type

open class HandleOnce<out T>(private val content: T) {

    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    override fun toString(): String {
        return "HandleOnce has " + (if (hasBeenHandled) "" else "not ") + "been handled, content is $content"
    }
}
