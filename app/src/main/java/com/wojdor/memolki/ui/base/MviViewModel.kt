package com.wojdor.memolki.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<I : UiIntent, S : UiState>(initialState: S) : ViewModel() {

    private val _uiIntent = Channel<I>(Channel.BUFFERED)

    private val _uiEffect = Channel<UiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<UiEffect>
        get() = _uiEffect.receiveAsFlow()

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S>
        get() = _uiState

    init {
        startCollectingIntents()
    }

    fun sendIntent(intent: I) {
        viewModelScope.launch { _uiIntent.send(intent) }
    }

    protected abstract fun onIntent(intent: I)

    protected fun sendEffect(effect: UiEffect) {
        viewModelScope.launch { _uiEffect.send(effect) }
    }

    protected fun sendState(update: S.() -> S) {
        _uiState.update(update)
    }

    private fun startCollectingIntents() {
        viewModelScope.launch { _uiIntent.receiveAsFlow().collect { onIntent(it) } }
    }
}
