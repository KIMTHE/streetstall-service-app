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
import com.themans.street_stall.network.StreetStallApi
import kotlinx.coroutines.launch

enum class UserType { GUEST, STALL, MANAGER }

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _userState = MutableLiveData<UserType>()
    val state: LiveData<UserType> = _userState

    init {
        checkLogin()
    }

    private fun register() {
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}"
                )
                //회원가입 rest api 호출
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
                    }
                } else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    //다음화면으로 이동
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

                register()
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
                        getApplication(),
                        callback = callback
                    )

                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(getApplication(), callback = callback)
        }
    }

    fun logout() { //설정화면에서 사용
        // 로그아웃
        // 사용자 액세스 토큰과 리프레시 토큰을 모두 만료시켜,
        // 더 이상 해당 사용자 정보로 카카오 API를 호출할 수 없도록 합니다.
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                //로그인 화면으로 이동
            }
        }
    }

    fun userWithdraw() { //설정화면에서 사용
        // 회원탈퇴
        // 사용자의 카카오계정을 삭제합니다.
        // 사용자의 액세스 토큰과 리프레시 토큰을 모두 만료시켜,
        // 더 이상 해당 사용자 정보로 카카오 API를 호출할 수 없도록 합니다.
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(TAG, "회원탈퇴 실패", error)
            } else {
                Log.i(TAG, "회원탈퇴 성공. SDK에서 토큰 삭제됨")
                //회원정보 삭제 rest api 호출
                //로그인 화면으로 이동
            }
        }

    }


    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [LiveData].
     */
    private fun getUser() {
        viewModelScope.launch {
            try {
                StreetStallApi.retrofitService.userLogin(LoginRequest("tlarccdf", "abc1234"))
                    .toString()
            } catch (e: Exception) {
            }
        }
    }

//    private fun getMarsPhotos() {
//        viewModelScope.launch {
//            _status.value = MarsApiStatus.LOADING
//            try {
//                _photos.value = MarsApi.retrofitService.getPhotos()
//                _status.value = MarsApiStatus.DONE
//            } catch (e: Exception) {
//                _status.value = MarsApiStatus.ERROR
//                _photos.value = listOf()
//            }
//        }
//    }
}