package com.arnyminerz.library.kmconnector.utils.serialization

import org.json.JSONObject

interface JsonSerializable {
    fun toJSON(): JSONObject
}
