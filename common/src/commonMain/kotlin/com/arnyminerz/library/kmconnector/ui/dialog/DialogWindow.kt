package com.arnyminerz.library.kmconnector.ui.dialog

import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.DpSize
import com.arnyminerz.library.kmconnector.ui.dialog.data.DialogResult
import com.arnyminerz.library.kmconnector.ui.window.CommonWindowInterface
import com.arnyminerz.library.kmconnector.ui.window.RequestWindow
import dev.icerock.moko.resources.StringResource
import kotlin.reflect.KClass

expect abstract class DialogWindow(
    resizable: Boolean,
    title: StringResource,
    parentWindow: KClass<out RequestWindow>,
    initialSize: DpSize = DpSize.Unspecified
): CommonWindowInterface {
    companion object: CommonDialogCompanion

    val parentWindow: KClass<out RequestWindow>

    val visible: MutableState<Boolean>

    fun close(result: DialogResult = DialogResult.CANCELLED)

    fun close(result: DialogResult, data: Map<String, Any>)
}
