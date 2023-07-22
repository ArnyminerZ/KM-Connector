package com.arnyminerz.library.kmconnector.ui.viewmodel

import com.arnyminerz.library.kmconnector.ui.window.CommonWindowInterface

expect inline fun <reified VM: ViewModelConcept> CommonWindowInterface.viewModels(): Lazy<VM>
