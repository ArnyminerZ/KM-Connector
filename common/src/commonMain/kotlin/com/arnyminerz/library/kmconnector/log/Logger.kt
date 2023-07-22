package com.arnyminerz.library.kmconnector.log

/**
 * Provides an object that takes the current platform's preferred logging method (`Log` in Android, `System.out` in
 * desktop), and uses the methods provided by [LoggerModel] to log messages.
 *
 * See platform-specific implementation for more information.
 */
expect object Logger: LoggerModel
