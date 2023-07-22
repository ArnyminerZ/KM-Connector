package com.arnyminerz.library.kmconnector.ui.window

import android.content.Intent
import com.arnyminerz.library.kmconnector.android.utils.putExtras
import kotlin.reflect.KClass

actual abstract class CommonWindowCompanion {
    context(RequestWindow)
    actual fun <W: RequestWindow> launch(window: KClass<W>, vararg data: Pair<String, Any>) {
        startActivity(
            Intent(this@RequestWindow, window.java).apply {
                putExtras(data.toList())
            }
        )
    }
}
