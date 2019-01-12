package org.ignus.app.services

import com.google.gson.Gson
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.ignus.app.mockdata.VALID_JWT_TOKEN
import org.ignus.app.mockdata.eventCategoryList

class MockServerDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest?): MockResponse {
        if (request != null) {
            return when (request.path) {
                "/token-auth/" -> MockResponse().setResponseCode(200).setBody("{\"token\":\"$VALID_JWT_TOKEN\"}")
                "/event/category-list/" -> MockResponse().setResponseCode(200).setBody(Gson().toJson(eventCategoryList))
                else -> MockResponse().setResponseCode(404)
            }
        }

        return MockResponse().setResponseCode(404)
    }
}