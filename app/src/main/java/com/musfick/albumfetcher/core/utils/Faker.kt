package com.musfick.albumfetcher.core.utils

import com.google.gson.Gson
import com.musfick.albumfetcher.features.album.domain.model.Album
import com.musfick.albumfetcher.features.photo.domain.model.Photo
import com.musfick.albumfetcher.features.user.domain.model.User

object Faker {

    fun getUser(): User {
        val jsonString = """{
            "id": 1,
            "name": "Leanne Graham",
            "username": "Bret",
            "email": "Sincere@april.biz",
            "address": {
              "street": "Kulas Light",
              "suite": "Apt. 556",
              "city": "Gwenborough",
              "zipcode": "92998-3874",
              "geo": {
                "lat": "-37.3159",
                "lng": "81.1496"
              }
            },
            "phone": "1-770-736-8031 x56442",
            "website": "hildegard.org",
            "company": {
              "name": "Romaguera-Crona",
              "catchPhrase": "Multi-layered client-server neural-net",
              "bs": "harness real-time e-markets"
            }}"""

        return Gson().fromJson(jsonString, User::class.java)
    }

    fun getPhoto(): Photo {
        val jsonString = """{
              "albumId": 1,
              "id": 1,
              "title": "accusamus beatae ad facilis cum similique qui sunt",
              "url": "https://via.placeholder.com/600/92c952",
              "thumbnailUrl": "https://via.placeholder.com/150/92c952"
            }"""

        return Gson().fromJson(jsonString, Photo::class.java)
    }

    fun getAlbum():Album {
        val jsonString = """{
            "userId": 1,
            "id": 1,
            "title": "quidem molestiae enim"
          }"""

        return Gson().fromJson(jsonString, Album::class.java)
    }
}