package com.arnyminerz.library.kmconnector.ui.viewmodel

import com.arnyminerz.library.kmconnector.ui.window.CommonWindowInterface
import kotlin.reflect.full.primaryConstructor

actual inline fun <reified VM : ViewModelConcept> CommonWindowInterface.viewModels(): Lazy<VM> =
    object: Lazy<VM> {
        override val value: VM = VM::class.primaryConstructor!!.call()

        override fun isInitialized(): Boolean = true
    }
