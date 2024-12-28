package com.musfick.albumfetcher.utils

import com.musfick.albumfetcher.core.utils.Constants
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.util.concurrent.TimeUnit

class MockWebServerDispatcher {

    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/albums" ->
                    MockResponse().setResponseCode(200)
                        .setBody(
                            """[
                              {
                                "userId": 1,
                                "id": 1,
                                "title": "quidem molestiae enim"
                              },
                              {
                                "userId": 2,
                                "id": 2,
                                "title": "sunt qui excepturi placeat culpa"
                              }
                            ]
                            """
                        )
                        .throttleBody(1024, 200L, TimeUnit.MILLISECONDS)

                "/users" ->
                    MockResponse().setResponseCode(200)
                        .setBody(
                            """[
                              {
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
                                }
                              },
                              {
                                "id": 2,
                                "name": "Ervin Howell",
                                "username": "Antonette",
                                "email": "Shanna@melissa.tv",
                                "address": {
                                  "street": "Victor Plains",
                                  "suite": "Suite 879",
                                  "city": "Wisokyburgh",
                                  "zipcode": "90566-7771",
                                  "geo": {
                                    "lat": "-43.9509",
                                    "lng": "-34.4618"
                                  }
                                },
                                "phone": "010-692-6593 x09125",
                                "website": "anastasia.net",
                                "company": {
                                  "name": "Deckow-Crist",
                                  "catchPhrase": "Proactive didactic contingency",
                                  "bs": "synergize scalable supply-chains"
                                }
                              }
                            ]
                            """
                        )
                        .throttleBody(1024, 200L, TimeUnit.MILLISECONDS)

                "/photos" ->
                    MockResponse().setResponseCode(200)
                        .setBody(
                            """[{
                                  "albumId": 1,
                                  "id": 1,
                                  "title": "accusamus beatae ad facilis cum similique qui sunt",
                                  "url": "https://via.placeholder.com/600/92c952",
                                  "thumbnailUrl": "https://via.placeholder.com/150/92c952"
                                },
                                  {
                                    "albumId": 1,
                                    "id": 2,
                                    "title": "reprehenderit est deserunt velit ipsam",
                                    "url": "https://via.placeholder.com/600/771796",
                                    "thumbnailUrl": "https://via.placeholder.com/150/771796"
                                  },
                                  {
                                    "albumId": 2,
                                    "id": 3,
                                    "title": "officia porro iure quia iusto qui ipsa ut modi",
                                    "url": "https://via.placeholder.com/600/24f355",
                                    "thumbnailUrl": "https://via.placeholder.com/150/24f355"
                                  },
                                  {
                                    "albumId": 2,
                                    "id": 4,
                                    "title": "culpa odio esse rerum omnis laboriosam voluptate repudiandae",
                                    "url": "https://via.placeholder.com/600/d32776",
                                    "thumbnailUrl": "https://via.placeholder.com/150/d32776"
                                  }]
                            """
                        )
                        .throttleBody(1024, 200L, TimeUnit.MILLISECONDS)

                else -> MockResponse().setResponseCode(400)
            }
        }
    }

    internal inner class ErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400)
                .setBody(Constants.ERROR_OCCURRED)
        }
    }

    internal inner class EmptyDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(200)
                .setBody("[]")
        }
    }
}