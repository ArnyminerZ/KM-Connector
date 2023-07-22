package com.arnyminerz.library.kmconnector.storage.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.arnyminerz.library.kmconnector.BuildKonfig
import com.arnyminerz.library.kmconnector.log.Logger
import java.util.prefs.PreferenceChangeListener
import java.util.prefs.Preferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking

actual object PreferencesProvider {
    private val prefs by lazy {
        Preferences.userRoot().node(BuildKonfig.PreferencesContainer)
    }

    actual suspend fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> set(key: K, value: R) {
        Logger.d("PREFERENCES :: $value -> ${key.name}")
        prefs.put(key.name, key.type.convertToString(value))
    }

    actual suspend fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> get(key: K): R? =
        prefs.get(key.name, null)?.let { key.type.convertFromString(it) }

    actual suspend fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> get(key: K, default: R): R =
        prefs.get(key.name, key.type.convertToString(default)).let { key.type.convertFromString(it) }

    @Composable
    actual fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> getLive(key: K): State<R?> =
        getAsFlow(key).collectAsState(null)

    actual fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> getAsFlow(key: K): Flow<R?> {
        var listener: PreferenceChangeListener? = null
        var isListening = true
        val flow = channelFlow {
            listener = PreferenceChangeListener { event ->
                if (event.key == key.name)
                    runBlocking {
                        Logger.d("PREFERENCES :: Update! ${event.newValue} -> ${event.key}")
                        send(key.type.convertFromString(event.newValue))
                    }
            }
            Logger.d("PREFERENCES :: Adding change listener for ${key.name}")
            prefs.addPreferenceChangeListener(listener)
            send(get(key))
            while (isListening) { delay(1) }
        }.cancellable()
        flow.onCompletion {
            Logger.d("PREFERENCES :: Removing preference change listener...")
            prefs.removePreferenceChangeListener(listener)
            isListening = false
        }
        return flow
    }

    actual suspend fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> remove(key: K) {
        prefs.remove(key.name)
    }
}
