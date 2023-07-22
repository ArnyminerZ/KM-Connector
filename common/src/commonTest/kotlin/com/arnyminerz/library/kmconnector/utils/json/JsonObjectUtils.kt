package com.arnyminerz.library.kmconnector.utils.json

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.json.JSONException
import org.junit.Test

class JsonObjectUtils {
    @Test
    fun `String conversion to JSONObject - empty`() {
        val json = "{}".json
        assertTrue(json.isEmpty)
    }

    @Test
    fun `String conversion to JSONObject - string`() {
        val json = "{\"test\":\"value\"}".json
        assertFalse(json.isEmpty)
        assertEquals("value", json.getString("test"))
    }

    @Test
    fun `String conversion to JSONObject - invalid`() {
        assertFailsWith(JSONException::class) { "invalid".json }
    }
}
