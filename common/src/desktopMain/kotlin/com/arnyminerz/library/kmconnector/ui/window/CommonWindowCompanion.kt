package com.arnyminerz.library.kmconnector.ui.window

import androidx.compose.runtime.mutableStateMapOf
import kotlin.reflect.KClass

actual abstract class CommonWindowCompanion {
    companion object {
        val displayingWindow = mutableStateMapOf<KClass<*>, Boolean>()
    }

    context(RequestWindow)
    actual fun <W: RequestWindow> launch(window: KClass<W>, vararg data: Pair<String, Any>) {
        displayingWindow[window] = true
        // TODO: Pass data
    }
}
