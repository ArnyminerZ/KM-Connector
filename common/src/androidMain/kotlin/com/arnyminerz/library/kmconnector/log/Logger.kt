package com.arnyminerz.library.kmconnector.log

import android.os.Build
import android.util.Log
import java.util.regex.Pattern

actual object Logger: LoggerModel {
    private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    private const val MAX_TAG_LENGTH = 23

    private val fqcnIgnore = listOf(
        Logger::class.java.name,
        LoggerModel::class.java.name
    )

    @get:JvmSynthetic // Hide from public API.
    internal val explicitTag = ThreadLocal<String>()

    @get:JvmSynthetic // Hide from public API.
    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    internal val tag: String
        get() {
            val tag = explicitTag.get()
            if (tag != null) {
                explicitTag.remove()
            }
            return tag ?: Throwable().stackTrace
                .first { it.className !in fqcnIgnore }
                .let(::createStackElementTag)
        }

    /**
     * Extract the tag which should be used for the message from the `element`. By default
     * this will use the class name without any anonymous class suffixes (e.g., `Foo$1`
     * becomes `Foo`).
     *
     * Note: This will not be called if a [manual tag][.tag] was specified.
     */
    private fun createStackElementTag(element: StackTraceElement): String {
        var tag = element.className.substringAfterLast('.')
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        // Tag length limit was removed in API 26.
        return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tag
        } else {
            tag.substring(0, MAX_TAG_LENGTH)
        }
    }

    override fun d(message: String, vararg args: Any?) {
        Log.d(tag, message.format(*args))
    }

    override fun v(message: String, vararg args: Any?) {
        Log.v(tag, message.format(*args))
    }

    override fun i(message: String, vararg args: Any?) {
        Log.i(tag, message.format(*args))
    }

    override fun w(message: String, vararg args: Any?) {
        Log.w(tag, message.format(*args))
    }

    override fun e(message: String, tr: Throwable?, vararg args: Any?) {
        Log.e(tag, message.format(*args), tr)
    }
}