package com.arnyminerz.library.kmconnector.storage.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.Flow

expect object PreferencesProvider {
    suspend fun <R: Any, T: PreferenceKeyType<R>, K: PreferenceKey<R, T>> set(key: K, value: R)

    suspend fun <R: Any, T: PreferenceKeyType<R>, K: PreferenceKey<R, T>> get(key: K): R?

    suspend fun <R: Any, T: PreferenceKeyType<R>, K: PreferenceKey<R, T>> get(key: K, default: R): R

    @Composable
    fun <R: Any, T: PreferenceKeyType<R>, K: PreferenceKey<R, T>> getLive(key: K): State<R?>

    fun <R: Any, T: PreferenceKeyType<R>, K: PreferenceKey<R, T>> getAsFlow(key: K): Flow<R?>

    suspend fun <R: Any, T: PreferenceKeyType<R>, K: PreferenceKey<R, T>> remove(key: K)
}
