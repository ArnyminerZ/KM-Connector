package com.arnyminerz.library.kmconnector.ui.dialog

import com.arnyminerz.library.kmconnector.desktop.windows.mutateDialog
import com.arnyminerz.library.kmconnector.log.Logger
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
    actual suspend fun <D: DialogWindow> launch(dialog: KClass<D>, vararg data: Pair<String, Any>): DialogResultData {
        return suspendCoroutine { cont ->
            val uuid = UUID.randomUUID()
            callbacksLock.withLock { callbacks[uuid] = cont }

            mutateDialog(dialog) {
                Logger.d("Setting visibility of ${dialog.simpleName} to true")
                visible.value = true
                for ((key, value) in data) {
                    extras[key] = value
                }
                this.uuid = uuid
                onCreate()
                this
            }
        }
    }

    /**
     * Consumes the callback with the given UUID, continuing with the given [result]. Once consumed, the UUID cannot be
     * used again, the calling window must be destroyed.
     */
    fun consume(uuid: UUID, result: DialogResultData) = callbacksLock.withLock {
        val callback = callbacks[uuid]
        callback?.resume(result)
        callbacks.remove(uuid)
    }
}
