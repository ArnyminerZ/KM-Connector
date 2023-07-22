package com.arnyminerz.library.kmconnector.storage.preferences

/**
 * Specifies the key name and type for some data that will be stored in the preferences' provider.
 *
 * Use [PreferenceKey.new] to create new keys.
 *
 * Example:
 * ```kotlin
 * val name = PreferenceKey.new("name", PreferenceKeyType.STRING)
 * ```
 *
 * Afterward, [PreferenceKey.all] can be used to fetch a list of all the keys created.
 *
 * @see PreferencesProvider
 */
class PreferenceKey<R : Any, T : PreferenceKeyType<R>> private constructor(val name: String, val type: T) {
    companion object {
        private val allPreferenceKeys = arrayListOf<PreferenceKey<out Any, out PreferenceKeyType<out Any>>>()

        val all: Array<out PreferenceKey<out Any, out PreferenceKeyType<out Any>>>
            get() = allPreferenceKeys.toTypedArray()

        fun <R : Any, T : PreferenceKeyType<R>> new(name: String, type: T): PreferenceKey<R, T> =
            PreferenceKey(name, type).also { allPreferenceKeys.add(it) }
    }
}
