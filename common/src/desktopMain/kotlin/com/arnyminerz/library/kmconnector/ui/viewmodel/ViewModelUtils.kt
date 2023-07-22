package com.arnyminerz.library.kmconnector.ui.viewmodel

import com.arnyminerz.library.kmconnector.ui.dialog.DialogWindow
import com.arnyminerz.library.kmconnector.ui.window.RequestWindow
import kotlin.reflect.full.primaryConstructor

actual inline fun <reified VM : ViewModelConcept> RequestWindow.viewModels(): Lazy<VM> =
    object: Lazy<VM> {
        override val value: VM = VM::class.primaryConstructor!!.call()

        override fun isInitialized(): Boolean = true
    }

actual inline fun <reified VM : ViewModelConcept> DialogWindow.viewModels(): Lazy<VM> =
    object: Lazy<VM> {
        override val value: VM = VM::class.primaryConstructor!!.call()

        override fun isInitialized(): Boolean = true
    }
