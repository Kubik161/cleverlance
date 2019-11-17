package cz.beesli.cleverlancetest.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import cz.beesli.cleverlancetest.Config
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class CleverlanceServiceApi {

    fun instance() : CleverlanceApi {

        val retrofit = Retrofit.Builder()
            .baseUrl(Config.CLEVERLANCE_API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofit.create(CleverlanceApi::class.java)
    }
}