package com.arnyminerz.library.kmconnector.storage.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

actual object PreferencesProvider {
    lateinit var dataStore: DataStore<Preferences>

    actual suspend fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> set(key: K, value: R) {
        dataStore.edit {
            it[key.dataStoreKey] = value
        }
    }

    actual suspend fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> get(key: K): R? =
        getAsFlow(key).first()

    actual suspend fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> get(key: K, default: R): R =
        getAsFlow(key).first() ?: default

    @Composable
    actual fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> getLive(key: K): State<R?> =
        getAsFlow(key).collectAsState(null)

    actual fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> getAsFlow(key: K): Flow<R?> =
        dataStore.data.map { it[key.dataStoreKey] }

    actual suspend fun <R : Any, T : PreferenceKeyType<R>, K : PreferenceKey<R, T>> remove(key: K) {
        dataStore.edit {
            it.remove(key.dataStoreKey)
        }
    }
}
