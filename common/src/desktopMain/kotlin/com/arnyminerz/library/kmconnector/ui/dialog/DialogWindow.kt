package com.arnyminerz.library.kmconnector.ui.dialog

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.DpSize
import com.arnyminerz.library.kmconnector.ui.dialog.data.DialogResult
import com.arnyminerz.library.kmconnector.ui.dialog.data.DialogResultData
import com.arnyminerz.library.kmconnector.ui.window.CommonWindowInterface
import com.arnyminerz.library.kmconnector.ui.window.RequestWindow
import dev.icerock.moko.resources.StringResource
import java.util.UUID
import kotlin.reflect.KClass

actual abstract class DialogWindow actual constructor(
    resizable: Boolean,
    title: StringResource,
    actual val parentWindow: KClass<out RequestWindow>,
    initialSize: DpSize
): CommonWindowInterface(resizable, title, mutableStateMapOf(), initialSize) {
    actual companion object: CommonDialogCompanion() {
        @Suppress("UNCHECKED_CAST")
        val dialogs: List<Class<DialogWindow>>
            get() = DialogWindow::class.java.classes.map { it as Class<DialogWindow> }
    }

    actual val visible: MutableState<Boolean> = mutableStateOf(false)

    lateinit var uuid: UUID

    actual fun close(result: DialogResult) {
        consume(uuid, DialogResultData(result, emptyMap()))
        visible.value = false
    }

    actual fun close(result: DialogResult, data: Map<String, Any>) {
        consume(uuid, DialogResultData(result, data))
        visible.value = false
    }
}
