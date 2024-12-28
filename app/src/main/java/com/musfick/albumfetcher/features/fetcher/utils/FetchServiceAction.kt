package com.musfick.albumfetcher.features.fetcher.utils

sealed class FetchServiceAction {
    data object Start: FetchServiceAction()
    data object Stop: FetchServiceAction()
}