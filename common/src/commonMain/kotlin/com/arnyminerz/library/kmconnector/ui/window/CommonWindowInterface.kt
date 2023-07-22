package com.arnyminerz.library.kmconnector.ui.window

import androidx.compose.runtime.Composable

expect interface CommonWindowInterface {
    open fun onCreate()

    @Composable
    fun Content()
}
