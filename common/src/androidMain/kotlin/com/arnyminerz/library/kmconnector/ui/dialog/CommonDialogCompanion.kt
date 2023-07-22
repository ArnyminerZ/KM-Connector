package com.arnyminerz.library.kmconnector.ui.dialog

import android.content.Intent
import com.arnyminerz.library.kmconnector.android.utils.putExtras
import com.arnyminerz.library.kmconnector.ui.dialog.data.DialogResultData
import com.arnyminerz.library.kmconnector.ui.window.RequestWindow
import java.util.UUID
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KClass

actual abstract class CommonDialogCompanion {
    private val callbacksLock = ReentrantLock()
    private val callbacks: MutableMap<UUID, Continuation<DialogResultData>> = mutableMapOf()

    context(RequestWindow)
    actual suspend fun <D : DialogWindow> launch(dialog: KClass<D>, vararg data: Pair<String, Any>): DialogResultData {
        return suspendCoroutine<DialogResultData> { cont ->
            val uuid = UUID.randomUUID()
            callbacksLock.withLock {
                callbacks[uuid] = cont
            }

            startActivity(
                Intent(this@RequestWindow, dialog.java).apply {
                    putExtras(data.toList())
                    putExtra(EXTRA_UUID, uuid)
                }
            )
        }
    }

    /**
     * Consumes the callback with the given UUID, continuing with the given [result]. Once consumed, the UUID cannot be
     * used again, the calling Activity must be destroyed.
     */
    fun consume(uuid: UUID, result: DialogResultData) = callbacksLock.withLock {
        val callback = callbacks[uuid]
        callback?.resume(result)
        callbacks.remove(uuid)
    }
}
