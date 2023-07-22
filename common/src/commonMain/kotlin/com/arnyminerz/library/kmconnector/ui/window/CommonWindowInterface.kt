package com.arnyminerz.library.kmconnector.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.unit.DpSize
import dev.icerock.moko.resources.StringResource

expect abstract class CommonWindowInterface(
    resizable: Boolean,
    title: StringResource,
    extras: SnapshotStateMap<String, Any>,
    initialSize: DpSize = DpSize.Unspecified
) {
    val resizable: Boolean
    val title: StringResource
    val extras: SnapshotStateMap<String, Any>
    val initialSize: DpSize

    open fun onCreate()

    @Composable
    abstract fun Content()
}
