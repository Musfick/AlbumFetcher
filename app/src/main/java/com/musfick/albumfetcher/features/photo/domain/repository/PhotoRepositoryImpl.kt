package com.musfick.albumfetcher.features.photo.domain.repository

import com.musfick.albumfetcher.core.utils.Constants
import com.musfick.albumfetcher.features.photo.data.mapper.toModel
import com.musfick.albumfetcher.features.photo.data.network.PhotoApiService
import com.musfick.albumfetcher.features.photo.domain.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(
    private val photoApiService: PhotoApiService,
) : PhotoRepository {
    override suspend fun getPhotos(): Result<List<Photo>> {
        return withContext(Dispatchers.IO){
            try {
                val response = photoApiService.getPhotos()
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