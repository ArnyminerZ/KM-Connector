package com.arnyminerz.library.kmconnector.android.utils

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.arnyminerz.library.kmconnector.utils.serialization.JsonSerializable
import java.io.Serializable
import org.json.JSONObject

/**
 * Adds all the elements of [pairs] as extras in the given intent.
 * @throws IllegalArgumentException If there's an element that is not supported by [Intent] as extra.
 */
@Suppress("NestedBlockDepth", "CyclomaticComplexMethod")
fun Intent.putExtras(pairs: Iterable<Pair<String, Any>>) {
    for ((key, value) in pairs) {
        when (value) {
            is Byte -> putExtra(key, value)
            is Int -> putExtra(key, value)
            is Double -> putExtra(key, value)
            is Boolean -> putExtra(key, value)
            is String -> putExtra(key, value)
            is Long -> putExtra(key, value)
            is Char -> putExtra(key, value)
            is Float -> putExtra(key, value)
            is Short -> putExtra(key, value)
            is CharSequence -> putExtra(key, value)
            is LongArray -> putExtra(key, value)
            is DoubleArray -> putExtra(key, value)
            is BooleanArray -> putExtra(key, value)
            is FloatArray -> putExtra(key, value)
            is CharArray -> putExtra(key, value)
            is ByteArray -> putExtra(key, value)
            is IntArray -> putExtra(key, value)
            is ShortArray -> putExtra(key, value)
            is Bundle -> putExtra(key, value)
            is Array<*> -> if (value.isNotEmpty()) {
                when (value.first()) {
                    is Parcelable -> putExtra(key, value)
                    is String -> putExtra(key, value)
                    is CharSequence -> putExtra(key, value)
                    else -> throw IllegalArgumentException(
                        "Could not add an array of type ${value::class.simpleName} to Intent extras"
                    )
                }
            }

            is Serializable -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)

            is JSONObject -> putExtra(key, value.toString())
            is JsonSerializable -> putExtra(key, value.toJSON().toString())

            else -> throw IllegalArgumentException(
                "Could not add a value of type ${value::class.simpleName} to Intent extras"
            )
        }
    }
}
