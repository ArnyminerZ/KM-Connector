package com.arnyminerz.library.kmconnector.ui.window

import com.arnyminerz.library.kmconnector.utils.json.json
import com.arnyminerz.library.kmconnector.utils.serialization.JsonSerializer
import kotlin.reflect.full.companionObjectInstance

inline fun <reified T> RequestWindow.getExtra(key: String): T? {
    val value = extras[key]
    // Check if the result is serializable, then try fetching the value directly, or from string and converting to
    // the class
    val serializer = T::class.companionObjectInstance as JsonSerializer<*>?
    if (serializer != null) {
        if (value is String) {
            return serializer.fromJson(value.json) as T?
        } else if (value is T) {
            return value
        }
    } else if (value != null) {
        return value as T?
    }
    return null
}
