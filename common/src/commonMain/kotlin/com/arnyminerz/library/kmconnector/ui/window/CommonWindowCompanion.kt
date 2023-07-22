package com.arnyminerz.library.kmconnector.ui.window

import kotlin.reflect.KClass

expect abstract class CommonWindowCompanion() {
    context(RequestWindow)
    fun <W: RequestWindow> launch(window: KClass<W>, vararg data: Pair<String, Any>)
}
