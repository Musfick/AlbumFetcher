package com.musfick.albumfetcher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.musfick.albumfetcher.core.utils.Constants
import com.musfick.albumfetcher.core.utils.Faker
import com.musfick.albumfetcher.features.fetcher.domain.model.AlbumWithPhotoAndUser
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenKtTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun on_start_is_on_loading_showing(){
        composeRule.setContent {
            MainScreen(
                uiState = MainUiState.Loading,
                onRetry = {

                }
            )
        }
        composeRule.onNodeWithTag("loading_view").assertIsDisplayed()
    }

    @Test
    fun on_error_is_on_error_showing(){
        composeRule.setContent {
            MainScreen(
                uiState = MainUiState.Error(Constants.ERROR_OCCURRED),
                onRetry = {

                }
            )
        }
        composeRule.onNodeWithText(Constants.ERROR_OCCURRED).assertIsDisplayed()
    }

    @Test
    fun on_error_is_retry_btn_showing(){
        composeRule.setContent {
            MainScreen(
                uiState = MainUiState.Error(Constants.ERROR_OCCURRED),
                onRetry = {

                }
            )
        }
        composeRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun on_success_is_showing_list_item(){
        val items = mutableListOf(
            AlbumWithPhotoAndUser(
                album = Faker.getAlbum(),
                user = Faker.getUser(),
                photo = Faker.getPhoto()
            )
        )
        composeRule.setContent {
            MainScreen(
                uiState = MainUiState.Success(
                    items
                ),
                onRetry = {

                }
            )
        }
        composeRule.onNodeWithText(items[0].album.title).assertIsDisplayed()
    }
}