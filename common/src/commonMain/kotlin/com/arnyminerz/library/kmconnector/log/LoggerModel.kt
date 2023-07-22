package com.arnyminerz.library.kmconnector.log

interface LoggerModel {
    fun d(message: String, vararg args: Any?)

    fun v(message: String, vararg args: Any?)

    fun i(message: String, vararg args: Any?)

    fun w(message: String, vararg args: Any?)

    fun e(message: String, tr: Throwable? = null, vararg args: Any?)
}
