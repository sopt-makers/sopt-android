package org.sopt.official.data.service

import org.sopt.official.data.model.RequestAuthEmail
import org.sopt.official.data.model.ResponseAuthEmail
import retrofit2.http.POST

interface AuthService {
    @POST
    suspend fun authenticateEmail(param: RequestAuthEmail): ResponseAuthEmail
}
