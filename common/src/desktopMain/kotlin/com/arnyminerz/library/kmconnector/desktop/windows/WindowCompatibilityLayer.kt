package com.arnyminerz.library.kmconnector.desktop.windows

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberDialogState
import androidx.compose.ui.window.rememberWindowState
import com.arnyminerz.library.kmconnector.log.Logger
import com.arnyminerz.library.kmconnector.ui.AppTheme
import com.arnyminerz.library.kmconnector.ui.dialog.DialogWindow
import com.arnyminerz.library.kmconnector.ui.window.CommonWindowCompanion.Companion.displayingWindow
import com.arnyminerz.library.kmconnector.ui.window.RequestWindow
import dev.icerock.moko.resources.compose.stringResource
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Stores all the dialogs each window has.
 */
private val dialogsRegistry = mutableStateMapOf<KClass<out RequestWindow>, Set<DialogWindow>>()

fun <D: DialogWindow> mutateDialog(kClass: KClass<D>, options: DialogWindow.() -> DialogWindow) {
    val dialog = kClass.primaryConstructor!!.call()
    val window = dialog.parentWindow
    require(dialogsRegistry.containsKey(window)) {
        "Could not mutate dialog with an invalid parent window: ${window.simpleName}"
    }
    val dialogs = dialogsRegistry.getValue(window)
    val index = dialogs.indexOfFirst { it::class.simpleName == kClass.simpleName }
    Logger.d("Found dialog to mutate at #$index of ${window.simpleName}")
    val newDialogs = dialogs.toMutableSet()

    require(index >= 0) { "${kClass.simpleName} has not been registered." }
    val oldDialog = dialogs.elementAt(index)
    newDialogs.remove(oldDialog)

    val newDialog = options(oldDialog)
    newDialogs.add(newDialog)

    Logger.d("Updating dialogs registry...")
    dialogsRegistry[window] = newDialogs
}

private fun <D: DialogWindow> registerDialog(kClass: KClass<D>) {
    val dialog = kClass.primaryConstructor!!.call()
    val list = dialogsRegistry[dialog.parentWindow] ?: emptyList()
    val mutable = list.toMutableSet()
    mutable.add(dialog)
    dialogsRegistry[dialog.parentWindow] = mutable
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun <W : RequestWindow> registerWindow(kClass: KClass<W>, showByDefault: Boolean = false) {
    var displaying = displayingWindow[kClass]
    if (displaying == null) {
        Logger.d("Updating default visibility of ${kClass.simpleName} to $showByDefault")
        displayingWindow[kClass] = showByDefault
        displaying = showByDefault
    }
    if (displaying == true) {
        val window = remember {
            kClass.primaryConstructor!!.call()
        }
        window.thisClass = kClass

        val state = rememberWindowState(
            size = window.initialSize.takeIf { it.isSpecified } ?: DpSize(800.dp, 600.dp)
        )
        Window(
            state = state,
            title = stringResource(window.title),
            icon = painterResource("icon.svg"),
            resizable = window.resizable,
            onCloseRequest = { displayingWindow[kClass] = false }
        ) {
            // TODO: Check if window sizing is available on desktop
            // currentWindowSize.value = state.size

            AppTheme {
                // Display all dialogs
                Logger.d("Loading ${dialogsRegistry.size} dialogs...")
                dialogsRegistry[kClass]?.forEach { dialog ->
                    val visible by dialog.visible
                    Logger.d("  ${dialog::class.simpleName} :: visible=$visible")
                    if (visible) {
                        val dialogState = rememberDialogState(
                            size = dialog.initialSize.takeIf { it.isSpecified } ?: DpSize(400.dp, 300.dp)
                        )
                        Dialog(
                            icon = painterResource("icon.svg"),
                            title = stringResource(dialog.title),
                            resizable = dialog.resizable,
                            state = dialogState,
                            onCloseRequest = {
                                mutateDialog(dialog::class) {
                                    Logger.d("Setting visibility of ${dialog::class.simpleName} to true")
                                    this.visible.value = false
                                    this
                                }
                            }
                        ) {
                            dialog.Content()
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .onPointerEvent(PointerEventType.Press) { event ->
                            if (event.button == PointerButton.Back)
                                window.onBackRequested()
                        }
                ) {
                    window.Content()
                }
            }
        }
    }
}

@Composable
fun ApplicationScope.WindowCompatibilityLayer() {
    if (displayingWindow.isNotEmpty() && displayingWindow.all { !it.value }) {
        Logger.i("Closed all windows. Closing application...")
        exitApplication()
    }

    RequestWindow.windows.forEach { windowClass ->
        registerWindow(windowClass.kotlin, true)
    }
    DialogWindow.dialogs.forEach { dialogClass ->
        registerDialog(dialogClass.kotlin)
    }
}
