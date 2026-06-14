package org.sopt.official.sopletter.name

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SopletterNameViewModel @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow(NameState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<NameSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()


}