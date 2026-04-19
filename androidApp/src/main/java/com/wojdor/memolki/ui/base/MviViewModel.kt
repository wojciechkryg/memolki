package com.wojdor.memolki.ui.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<I : UiIntent, S : UiState>(
    private val savedStateHandle: SavedStateHandle,
    initialState: S
) : ViewModel() {

    private val stateKey = this::class.simpleName ?: "state"

    private val _uiIntent = Channel<I>(Channel.BUFFERED)

    private val _uiEffect = Channel<UiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<UiEffect>
        get() = _uiEffect.receiveAsFlow()

    private val _uiState = MutableStateFlow(savedStateHandle[stateKey] ?: initialState)
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
        savedStateHandle[stateKey] = _uiState.value
    }

    private fun startCollectingIntents() {
        viewModelScope.launch { _uiIntent.receiveAsFlow().collect { onIntent(it) } }
    }
}
