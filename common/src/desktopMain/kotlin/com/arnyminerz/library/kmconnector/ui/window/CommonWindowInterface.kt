package com.arnyminerz.library.kmconnector.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.unit.DpSize
import dev.icerock.moko.resources.StringResource

actual abstract class CommonWindowInterface actual constructor(
    actual val resizable: Boolean,
    actual val title: StringResource,
    actual val extras: SnapshotStateMap<String, Any>,
    actual val initialSize: DpSize
) {
    actual open fun onCreate() {}

    @Composable
    actual abstract fun Content()
}
