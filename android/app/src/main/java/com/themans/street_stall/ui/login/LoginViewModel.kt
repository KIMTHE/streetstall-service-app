package com.themans.street_stall.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.themans.street_stall.model.LoginRequest
import com.themans.street_stall.model.UserInfo
import com.themans.street_stall.network.StreetStallApi
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo


    init {

    }

    /**
     * 로그인을 시도하고 유저 정보를 받음
     * [UserType] [LiveData].
     */
    fun getUserInfo(id: String, nickname: String) = viewModelScope.launch {
        try {
            _userInfo.value =
                StreetStallApi.retrofitService.userLogin(LoginRequest(id, nickname))

        } catch (e: Exception) {
            Log.e(TAG, "getUserInfo: $e")
        }
    }


}