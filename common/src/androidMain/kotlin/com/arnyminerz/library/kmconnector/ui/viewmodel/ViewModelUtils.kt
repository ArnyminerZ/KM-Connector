package com.arnyminerz.library.kmconnector.ui.viewmodel

import androidx.activity.viewModels
import com.arnyminerz.library.kmconnector.ui.dialog.DialogWindow
import com.arnyminerz.library.kmconnector.ui.window.RequestWindow

actual inline fun <reified VM : ViewModelConcept> RequestWindow.viewModels(): Lazy<VM> = this.viewModels<VM>()

actual inline fun <reified VM : ViewModelConcept> DialogWindow.viewModels(): Lazy<VM> = this.viewModels<VM>()
