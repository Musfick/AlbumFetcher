package com.musfick.albumfetcher.features.user.domain.repository
import com.musfick.albumfetcher.core.utils.Constants
import com.musfick.albumfetcher.features.user.data.mapper.toModel
import com.musfick.albumfetcher.features.user.data.network.UserApiService
import com.musfick.albumfetcher.features.user.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
) : UserRepository {
    override suspend fun getUsers(): Result<List<User>> {
        return withContext(Dispatchers.IO){
            try {
                val response = userApiService.getUsers()
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