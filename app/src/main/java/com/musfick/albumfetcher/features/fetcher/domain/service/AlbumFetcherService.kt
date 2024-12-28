package com.musfick.albumfetcher.features.fetcher.domain.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.musfick.albumfetcher.core.utils.Constants.ERROR_OCCURRED
import com.musfick.albumfetcher.features.album.domain.repository.AlbumRepository
import com.musfick.albumfetcher.features.fetcher.domain.model.AlbumWithPhotoAndUser
import com.musfick.albumfetcher.features.fetcher.utils.FetchServiceAction
import com.musfick.albumfetcher.features.fetcher.utils.FetchState
import com.musfick.albumfetcher.features.photo.domain.repository.PhotoRepository
import com.musfick.albumfetcher.features.user.domain.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject


@AndroidEntryPoint
class AlbumFetcherService:Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var albumRepository: AlbumRepository
    @Inject
    lateinit var photoRepository: PhotoRepository
    @Inject
    lateinit var userRepository: UserRepository


    val fetchState = MutableStateFlow<FetchState>(FetchState.Loading)

    private val binder: AlbumFetcherServiceBinder by lazy {
        AlbumFetcherServiceBinder()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun executeAction(action: FetchServiceAction) {
        when(action) {
            FetchServiceAction.Start -> startFetching()
            FetchServiceAction.Stop -> stopFetching()
        }
    }

    private fun startFetching(){
        scope.launch(Dispatchers.IO) {
            fetchState.value = FetchState.Loading

            coroutineScope {
                val albumsCall = async { albumRepository.getAlbums() }
                val photosCall = async { photoRepository.getPhotos() }
                val usersCall = async { userRepository.getUsers() }
                try {
                    val albums = albumsCall.await()
                    val photos = photosCall.await()
                    val users = usersCall.await()
                    if(albums.isSuccess && photos.isSuccess && users.isSuccess){
                        //only show when 3 of the response get success
                        withContext(Dispatchers.IO){
                            val userMap = users.getOrDefault(emptyList()).associateBy { it.id }
                            val firstPhotos = photos.getOrDefault(emptyList()).groupBy { it.albumId }.mapValues { it.value.firstOrNull() }
                            val albumWithPhotoAndUserList = albums.getOrDefault(emptyList())
                                .map { album ->
                                    AlbumWithPhotoAndUser(
                                        album = album,
                                        photo = firstPhotos[album.id],
                                        user = userMap[album.userId]
                                    )
                                }
                            fetchState.value = FetchState.Success(albumWithPhotoAndUserList)
                        }

                    }else{
                        //find which one has error
                        if(albums.isFailure){
                            fetchState.value = FetchState.Error(message = albums.exceptionOrNull()?.message ?: ERROR_OCCURRED)
                        }else if (photos.isFailure){
                            fetchState.value = FetchState.Error(message = photos.exceptionOrNull()?.message ?: ERROR_OCCURRED)
                        }else if (users.isFailure){
                            fetchState.value = FetchState.Error(message = users.exceptionOrNull()?.message ?: ERROR_OCCURRED)
                        }
                    }
                }catch (e:Exception){
                    fetchState.value = FetchState.Error(message = e.message ?: ERROR_OCCURRED)
                }
            }
        }
    }

    private fun stopFetching(){
        scope.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    inner class AlbumFetcherServiceBinder: Binder() {
        fun getService(): AlbumFetcherService = this@AlbumFetcherService
    }

    @TestOnly
    fun setDependencies(
        albumRepository: AlbumRepository,
        photoRepository: PhotoRepository,
        userRepository: UserRepository
    ){
        this.albumRepository = albumRepository
        this.photoRepository = photoRepository
        this.userRepository = userRepository
    }
}