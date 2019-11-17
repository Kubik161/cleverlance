package cz.beesli.cleverlancetest.network

import cz.beesli.cleverlancetest.data.model.ImageEntity
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface CleverlanceApi {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("download/bootcamp/image.php")
    fun postUserAsync(
        @Header("Authorization") password : String,
        @Body body : String)
            : Deferred<Response<ImageEntity>>
}