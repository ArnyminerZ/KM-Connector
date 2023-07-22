package com.arnyminerz.library.kmconnector.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual abstract class ViewModelConcept: ViewModel() {
    actual val scope: CoroutineScope
        get() = viewModelScope
}
