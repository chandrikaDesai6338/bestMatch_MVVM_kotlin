package com. chan.marriagepraposals.webservice

import com.chan.marriagepraposals.models.BaseResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Chan on 10/08/20.
 */
interface ApiService {

    @GET("api/")
    fun getUserList(@Query("results")page : Int) : Flowable<BaseResponse>

}