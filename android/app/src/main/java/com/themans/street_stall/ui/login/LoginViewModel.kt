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
        checkLogin()
    }

    private fun tryLogin() {
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
                //TODO 재 로그인 시도 띄우기

            } else if (user != null) {
                Log.i(
                    TAG,
                    "사용자 정보 요청 성공" + "\n회원번호: ${user.id}" + "\n닉네임: ${user.kakaoAccount?.profile?.nickname}"
                )

                getUserInfo(user.id.toString(), user.kakaoAccount?.profile?.nickname ?: "닉네임")

            }
        }

    }

    private fun checkLogin() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        //로그인 필요
                    } else {
                        //기타 에러
                        Log.e(TAG, "토큰 접근 실패", error)
                    }
                } else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    tryLogin()
                }
            }
        } else {
            //로그인 필요
        }
    }

    fun loginWithKakao() {

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")

                tryLogin()
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(getApplication())) {
            UserApiClient.instance.loginWithKakaoTalk(getApplication()) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        getApplication(), callback = callback
                    )

                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    tryLogin()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(getApplication(), callback = callback)
        }
    }

    /**
     * 로그인을 시도하고 유저 정보를 받음
     * [UserType] [LiveData].
     */
    private fun getUserInfo(id: String, nickname: String) = viewModelScope.launch {
        try {
            _userInfo.value =
                StreetStallApi.retrofitService.userLogin(LoginRequest(id, nickname))

        } catch (e: Exception) {
            Log.e(TAG, "getUserInfo: $e")
            //TODO 재 로그인 시도 띄우기
        }
    }


}