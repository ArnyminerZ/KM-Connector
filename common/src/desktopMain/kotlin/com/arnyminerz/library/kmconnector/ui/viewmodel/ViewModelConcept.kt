package com.arnyminerz.library.kmconnector.ui.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

actual abstract class ViewModelConcept {
    actual val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
}
