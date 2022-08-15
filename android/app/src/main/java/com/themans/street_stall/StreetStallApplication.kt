package com.themans.street_stall

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class StreetStallApplication : Application() {
//    val database: ItemRoomDatabase by lazy{ItemRoomDatabase.getDatabase(this)}

    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_API_KEY)
    }
}

