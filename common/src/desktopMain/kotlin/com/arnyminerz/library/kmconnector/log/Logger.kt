package com.arnyminerz.library.kmconnector.log

import com.arnyminerz.library.kmconnector.desktop.ANSI_BLUE
import com.arnyminerz.library.kmconnector.desktop.ANSI_GREEN
import com.arnyminerz.library.kmconnector.desktop.ANSI_RED
import com.arnyminerz.library.kmconnector.desktop.ANSI_YELLOW
import java.util.regex.Pattern

actual object Logger: LoggerModel {
    private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    private const val TAG_MAX_LENGTH = 48

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
        return tag.substring(0, minOf(TAG_MAX_LENGTH, tag.length))
    }

    private val cutTag: String
        get() = tag.padStart(TAG_MAX_LENGTH)

    override fun d(message: String, vararg args: Any?) {
        println("$ANSI_BLUE D/ ${message.format(*args)}")
    }

    override fun v(message: String, vararg args: Any?) {
        println("$ANSI_BLUE$cutTag V/ ${message.format(*args)}")
    }

    override fun i(message: String, vararg args: Any?) {
        println("$ANSI_GREEN I/ ${message.format(*args)}")
    }

    override fun w(message: String, vararg args: Any?) {
        println("$ANSI_YELLOW W/ ${message.format(*args)}")
    }

    override fun e(message: String, tr: Throwable?, vararg args: Any?) {
        if (tr != null) {
            System.err.println("$ANSI_RED E/ ${message.format(*args)}. Error: $tr")
            tr.printStackTrace(System.err)
        } else {
            System.err.println("$ANSI_RED$cutTag E/ ${message.format(*args)}")
        }
    }
}