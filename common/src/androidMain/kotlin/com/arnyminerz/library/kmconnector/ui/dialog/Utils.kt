package com.arnyminerz.library.kmconnector.ui.dialog

import android.app.Activity
import com.arnyminerz.library.kmconnector.ui.dialog.data.DialogResult

internal const val EXTRA_UUID = "uuid"

val DialogResult.intent: Int
    get() = when (this) {
        DialogResult.SUCCESS -> Activity.RESULT_OK
        else -> Activity.RESULT_CANCELED
    }
