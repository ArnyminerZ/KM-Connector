package com.arnyminerz.library.kmconnector.ui.window

import androidx.compose.runtime.Composable

actual interface CommonWindowInterface {
    actual fun onCreate() {}

    @Composable
    actual fun Content()
}
