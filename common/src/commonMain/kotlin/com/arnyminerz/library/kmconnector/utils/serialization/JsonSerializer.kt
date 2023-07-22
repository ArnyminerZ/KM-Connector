package com.arnyminerz.library.kmconnector.utils.serialization

import org.json.JSONObject

interface JsonSerializer<R> {
    fun fromJson(json: JSONObject): R
}
