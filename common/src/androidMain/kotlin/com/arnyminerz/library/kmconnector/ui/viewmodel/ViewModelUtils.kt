package com.arnyminerz.library.kmconnector.ui.viewmodel

import androidx.activity.viewModels
import com.arnyminerz.library.kmconnector.ui.window.CommonWindowInterface

actual inline fun <reified VM : ViewModelConcept> CommonWindowInterface.viewModels(): Lazy<VM> = this.viewModels<VM>()
