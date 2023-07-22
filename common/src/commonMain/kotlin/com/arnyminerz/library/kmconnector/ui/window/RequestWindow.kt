package com.arnyminerz.library.kmconnector.ui.window

import androidx.compose.ui.unit.DpSize
import dev.icerock.moko.resources.StringResource
import kotlin.reflect.KClass

expect abstract class RequestWindow(
    resizable: Boolean,
    title: StringResource,
    initialSize: DpSize = DpSize.Unspecified
) : CommonWindowInterface {
    companion object: CommonWindowCompanion

    open val parentWindow: KClass<out RequestWindow>?

    open fun onBackRequested()

    protected fun addBackListener()
}
