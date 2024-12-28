package com.musfick.albumfetcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musfick.albumfetcher.features.fetcher.domain.model.AlbumWithPhotoAndUser
import com.musfick.albumfetcher.features.fetcher.domain.service.AlbumFetcherService
import com.musfick.albumfetcher.features.fetcher.utils.FetchServiceAction
import com.musfick.albumfetcher.features.fetcher.utils.FetchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel(){
    var isFetchServiceBound:Boolean = false
    val albumFetchService = MutableStateFlow<AlbumFetcherService?>(null)

    val uiState = albumFetchService.flatMapLatest {
        it?.fetchState?.mapLatest {
            when(it){
                is FetchState.Loading -> return@mapLatest MainUiState.Loading
                is FetchState.Success -> return@mapLatest MainUiState.Success(it.items)
                is FetchState.Error -> return@mapLatest MainUiState.Error(it.message)
            }
        } ?: flow { MainUiState.Loading }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainUiState.Loading
    )

    fun onRetry(){
        albumFetchService.value?.executeAction(FetchServiceAction.Start)
    }

}

sealed class MainUiState(){
    data object Loading: MainUiState()
    data class Success(val items:List<AlbumWithPhotoAndUser>): MainUiState()
    data class Error(val message:String) : MainUiState()
}

