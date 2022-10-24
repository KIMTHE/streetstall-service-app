package com.themans.street_stall.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.themans.street_stall.BuildConfig
import com.themans.street_stall.model.LoginRequest
import com.themans.street_stall.model.UserInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


object StreetStallApi {
    private const val BASE_URL =
        BuildConfig.BASE_URL

    /**
     * 로그 캣에서 패킹 모니터링
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    /**
     * Build the Moshi object with Kotlin adapter factory that Retrofit will be using.
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * The Retrofit object with the Moshi converter.
     */
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()

    /**
     * A public Api object that exposes the lazy-initialized Retrofit service
     */
    val retrofitService: StreetStallService by lazy {
        retrofit.create(StreetStallService::class.java)
    }

}


/**
 * A public interface that exposes rest api methods
 */
interface StreetStallService {

    /*
    * 로그인 시도 및 회원가입
    * */
    @POST("login")
    suspend fun userLogin(
        @Body param: LoginRequest
    ): UserInfo
}