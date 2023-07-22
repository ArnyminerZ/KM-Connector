package com.arnyminerz.library.kmconnector.ui.dialog

import com.arnyminerz.library.kmconnector.ui.dialog.data.DialogResultData
import com.arnyminerz.library.kmconnector.ui.window.RequestWindow
import kotlin.reflect.KClass

expect abstract class CommonDialogCompanion() {
    context(RequestWindow)
    suspend fun <D: DialogWindow> launch(dialog: KClass<D>, vararg data: Pair<String, Any>): DialogResultData
}
