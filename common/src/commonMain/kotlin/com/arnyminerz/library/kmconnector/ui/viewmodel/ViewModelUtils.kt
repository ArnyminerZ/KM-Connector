package com.arnyminerz.library.kmconnector.ui.viewmodel

import com.arnyminerz.library.kmconnector.ui.dialog.DialogWindow
import com.arnyminerz.library.kmconnector.ui.window.RequestWindow

expect inline fun <reified VM: ViewModelConcept> RequestWindow.viewModels(): Lazy<VM>

expect inline fun <reified VM: ViewModelConcept> DialogWindow.viewModels(): Lazy<VM>
