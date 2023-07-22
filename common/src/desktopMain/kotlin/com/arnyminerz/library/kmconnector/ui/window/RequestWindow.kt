package com.arnyminerz.library.kmconnector.ui.window

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.unit.DpSize
import com.arnyminerz.library.kmconnector.ui.window.CommonWindowCompanion.Companion.displayingWindow
import dev.icerock.moko.resources.StringResource
import kotlin.reflect.KClass

actual abstract class RequestWindow actual constructor(
    resizable: Boolean,
    title: StringResource,
    initialSize: DpSize
): CommonWindowInterface(resizable, title, mutableStateMapOf(), initialSize) {
    actual companion object : CommonWindowCompanion() {
        @Suppress("UNCHECKED_CAST")
        val windows: List<Class<RequestWindow>>
            get() = RequestWindow::class.java.classes.map { it as Class<RequestWindow> }
    }

    init {
        addBackListener()
        onCreate()
    }

    actual open val parentWindow: KClass<out RequestWindow>? = null

    /**
     * Used for closing the window through [displayingWindow]. Set by `WindowCompatibilityLayer`.
     */
    lateinit var thisClass: KClass<*>

    actual open fun onBackRequested() {
        displayingWindow[thisClass] = false
        parentWindow?.let { RequestWindow.launch(it) }
    }

    protected actual fun addBackListener() {
        // Listener is added in WindowCompatibilityLayer
    }
}
