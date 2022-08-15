package com.themans.street_stall.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.themans.street_stall.model.LoginRequest
import com.themans.street_stall.network.StreetStallApi
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    init{
        Log.d("api test","start")
        testApi()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [LiveData].
     */
    private fun testApi() {
        viewModelScope.launch {
            try {
                Log.d("api test","start2")
                StreetStallApi.retrofitService.userLogin(LoginRequest("tlarccdf","abc1234")).toString()
                Log.d("api test","start3")
            } catch (e: Exception) {
            }
        }
    }
}