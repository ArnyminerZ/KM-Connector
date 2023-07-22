package com.arnyminerz.library.kmconnector.ui.dialog

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.DpSize
import com.arnyminerz.library.kmconnector.android.utils.putExtras
import com.arnyminerz.library.kmconnector.ui.AppTheme
import com.arnyminerz.library.kmconnector.ui.dialog.data.DialogResult
import com.arnyminerz.library.kmconnector.ui.dialog.data.DialogResultData
import com.arnyminerz.library.kmconnector.ui.window.CommonWindowInterface
import com.arnyminerz.library.kmconnector.ui.window.RequestWindow
import dev.icerock.moko.resources.StringResource
import java.util.UUID
import kotlin.reflect.KClass

actual abstract class DialogWindow actual constructor(
    resizable: Boolean,
    title: StringResource,
    actual val parentWindow: KClass<out RequestWindow>,
    initialSize: DpSize
) : CommonWindowInterface(resizable, title, mutableStateMapOf()) {
    actual companion object : CommonDialogCompanion()

    actual val visible: MutableState<Boolean> = mutableStateOf(false)

    private lateinit var uuid: UUID

    private var result: DialogResult = DialogResult.CANCELLED
    private var resultData: Map<String, Any> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            uuid = intent.extras?.getSerializable(EXTRA_UUID, UUID::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            uuid = intent.extras?.getSerializable(EXTRA_UUID) as UUID
        }

        intent.extras?.let { intentExtras ->
            for (key in intentExtras.keySet()) {
                @Suppress("DEPRECATION") val value = intentExtras.get(key) ?: continue
                extras[key] = value
            }
        }

        onCreate()

        setContent {
            AppTheme {
                BackHandler { close() }

                Content()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        consume(uuid, DialogResultData(result, resultData))
    }

    actual fun close(result: DialogResult) {
        this.result = result
        this.resultData = emptyMap()

        setResult(result.intent)
        finish()
    }

    actual fun close(result: DialogResult, data: Map<String, Any>) {
        this.result = result
        this.resultData = data

        setResult(
            result.intent,
            Intent().apply {
                putExtras(data.toList())
            }
        )
        finish()
    }
}
