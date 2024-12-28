package com.musfick.albumfetcher.features.album.domain.repository
import com.musfick.albumfetcher.core.utils.Constants
import com.musfick.albumfetcher.features.album.data.mapper.toModel
import com.musfick.albumfetcher.features.album.data.network.AlbumApiService
import com.musfick.albumfetcher.features.album.domain.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepositoryImpl @Inject constructor(
    private val albumApiService: AlbumApiService,
): AlbumRepository {
    override suspend fun getAlbums(): Result<List<Album>> {
        return withContext(Dispatchers.IO){
            try {
                val response = albumApiService.getAlbums()
                if(response.isSuccessful){
                    val items = response.body()
                    if(items.isNullOrEmpty()){
                        Result.failure(Exception(Constants.ERROR_NO_ITEM_FOUND))
                    }else{
                        Result.success(
                            items.map { it.toModel() }
                        )
                    }
                }else{
                    Result.failure(Exception(Constants.ERROR_OCCURRED))
                }
            }catch (e:Exception){
                Result.failure(e)
            }
        }
    }

}