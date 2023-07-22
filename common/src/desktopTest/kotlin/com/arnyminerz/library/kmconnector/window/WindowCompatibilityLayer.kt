package com.arnyminerz.library.kmconnector.window

import androidx.compose.runtime.Composable
import com.arnyminerz.library.kmconnector.MR
import com.arnyminerz.library.kmconnector.ui.dialog.DialogWindow
import com.arnyminerz.library.kmconnector.ui.window.RequestWindow
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import org.junit.Test

@Suppress("FunctionNaming")
class WindowCompatibilityLayer {
    @Suppress("TestFunctionName", "EmptyFunctionBlock", "unused")
    class ExampleWindow : RequestWindow(false, MR.strings.example_window) {
        @Composable
        override fun Content() {
        }

        override val parentWindow: KClass<out RequestWindow>? = null
    }

    @Suppress("TestFunctionName", "EmptyFunctionBlock", "unused")
    class ExampleDialog : DialogWindow(false, MR.strings.example_window, ExampleWindow::class) {
        @Composable
        override fun Content() {
        }
    }

    @Test
    fun `check that declared windows are available`() {
        val classes = RequestWindow.windows
        // Only ExampleWindow is declared, so size should be 1
        assertEquals(1, classes.size)
    }

    @Test
    fun `check that declared dialogs are available`() {
        val classes = DialogWindow.dialogs
        // Only ExampleDialog is declared, so size should be 1
        assertEquals(1, classes.size)
    }
}