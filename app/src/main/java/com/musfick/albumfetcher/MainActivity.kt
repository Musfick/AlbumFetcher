package com.musfick.albumfetcher

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.musfick.albumfetcher.features.fetcher.domain.service.AlbumFetcherService
import com.musfick.albumfetcher.features.fetcher.utils.FetchServiceAction
import com.musfick.albumfetcher.ui.theme.AlbumFetcherTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel:MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlbumFetcherTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                MainScreen(
                    uiState = uiState,
                    onRetry = viewModel::onRetry
                )
            }
        }
    }

    private val boundServiceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, binding: IBinder) {
                val binder: AlbumFetcherService.AlbumFetcherServiceBinder = binding as AlbumFetcherService.AlbumFetcherServiceBinder
                val service = binder.getService()
                viewModel.albumFetchService.value = service
                service.executeAction(FetchServiceAction.Start)
                viewModel.isFetchServiceBound = true
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                viewModel.albumFetchService.value?.executeAction(FetchServiceAction.Stop)
                viewModel.albumFetchService.value = null
                viewModel.isFetchServiceBound = false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!viewModel.isFetchServiceBound){
            bindToFetchService()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindFetchService()
    }

    private fun unbindFetchService() {
        if(viewModel.isFetchServiceBound) {
            unbindService(boundServiceConnection)
        }
    }

    private fun bindToFetchService(){
        val intent = Intent(
            this,
            AlbumFetcherService::class.java
        )

        bindService(
            intent,
            boundServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }
}
