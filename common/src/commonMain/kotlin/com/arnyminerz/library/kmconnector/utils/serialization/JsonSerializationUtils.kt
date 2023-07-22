package com.arnyminerz.library.kmconnector.utils.serialization

import org.json.JSONObject

inline fun <R, reified S: JsonSerializer<R>> JSONObject.serialized(): R {
    val serializer = S::class.objectInstance
    require(serializer != null) { "The passed serializer doesn't have an associated instance." }
    return serializer.fromJson(this)
}
