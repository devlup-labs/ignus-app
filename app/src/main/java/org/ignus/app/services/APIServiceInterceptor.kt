package org.ignus.app.services

import okhttp3.Interceptor
import okhttp3.Response
import org.ignus.app.config.INVALID_JWT_TOKEN
import org.ignus.app.config.NO_AUTH_HEADER_KEY
import java.io.IOException
import javax.inject.Singleton
import javax.inject.Inject


@Singleton
class APIServiceInterceptor @Inject constructor() : Interceptor {
    var jwtToken: String = INVALID_JWT_TOKEN

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = if (request.header(NO_AUTH_HEADER_KEY) == null) {
            if (jwtToken == INVALID_JWT_TOKEN)
                throw RuntimeException(":param `jwtToken` should be set for authorization required endpoints")
            else request.newBuilder()
                    .addHeader("Authorization", "JWT $jwtToken")
                    .build()
        } else request.newBuilder()
                .removeHeader(NO_AUTH_HEADER_KEY)
                .build()
        return chain.proceed(request)
    }
}
