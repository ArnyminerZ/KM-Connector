package com.arnyminerz.library.kmconnector.ui.window

import android.app.Activity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import com.arnyminerz.library.kmconnector.ui.AppTheme
import dev.icerock.moko.resources.StringResource
import kotlin.reflect.KClass

actual abstract class RequestWindow @Suppress("unused") actual constructor(
    // All these properties are ignored on Android, but must be declared
    actual val resizable: Boolean,
    actual val title: StringResource,
    actual val initialSize: DpSize
): CommonWindowInterface, AppCompatActivity() {
    actual companion object : CommonWindowCompanion()

    actual val extras: SnapshotStateMap<String, Any> = mutableStateMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)

        addBackListener()

        intent.extras?.let { intentExtras ->
            for (key in intentExtras.keySet()) {
                @Suppress("DEPRECATION") val value = intentExtras.get(key) ?: continue
                extras[key] = value
            }
        }

        onCreate()

        setContent {
            AppTheme {
                Box(Modifier.fillMaxSize().imePadding()) {
                    Content()
                }
            }
        }
    }

    actual open val parentWindow: KClass<out RequestWindow>? = null

    actual open fun onBackRequested() {
        setResult(Activity.RESULT_CANCELED)
        finish()
        parentWindow?.let { RequestWindow.launch(it) }
    }

    protected actual fun addBackListener() {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackRequested()
            }
        })
    }
}
