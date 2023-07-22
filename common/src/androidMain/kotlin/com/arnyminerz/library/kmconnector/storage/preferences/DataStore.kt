package com.arnyminerz.library.kmconnector.storage.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arnyminerz.library.kmconnector.BuildKonfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = BuildKonfig.PreferencesContainer)

@Suppress("UNCHECKED_CAST")
val <R : Any, T : PreferenceKeyType<R>> PreferenceKey<R, T>.dataStoreKey: Preferences.Key<R>
    get() = when (type) {
        PreferenceKeyType.INTEGER -> intPreferencesKey(name)
        PreferenceKeyType.STRING_SET -> stringSetPreferencesKey(name)
        else -> stringPreferencesKey(name)
    } as Preferences.Key<R>
