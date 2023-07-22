package com.arnyminerz.library.kmconnector.ui.viewmodel

import kotlinx.coroutines.CoroutineScope

expect abstract class ViewModelConcept() {
    val scope: CoroutineScope
}