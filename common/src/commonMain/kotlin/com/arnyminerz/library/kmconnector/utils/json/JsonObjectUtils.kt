package com.arnyminerz.library.kmconnector.utils.json

import org.json.JSONException
import org.json.JSONObject

/**
 * Converts `this` [String] into a [JSONObject].
 *
 * @throws JSONException If `this` is not a valid JSON object.
 */
val String.json: JSONObject
    get() = JSONObject(this)
