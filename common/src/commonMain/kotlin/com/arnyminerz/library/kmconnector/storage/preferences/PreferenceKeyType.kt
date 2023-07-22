package com.arnyminerz.library.kmconnector.storage.preferences

sealed class PreferenceKeyType <T: Any> {
    object STRING: PreferenceKeyType<String>() {
        override fun convertFromString(value: String): String = value

        override fun convertToString(value: String): String = value
    }

    object INTEGER: PreferenceKeyType<Int>() {
        override fun convertFromString(value: String): Int = value.toInt()

        override fun convertToString(value: Int): String = value.toString()
    }

    object STRING_SET: PreferenceKeyType<Set<String>>() {
        override fun convertFromString(value: String): Set<String> {
            require(!value.contains(";")) { "Value cannot contain \";\"" }
            return value.splitToSequence(";").toSet()
        }

        override fun convertToString(value: Set<String>): String = value.joinToString(";")
    }

    abstract fun convertFromString(value: String): T

    abstract fun convertToString(value: T): String
}
