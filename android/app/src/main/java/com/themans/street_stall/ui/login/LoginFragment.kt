package com.themans.street_stall.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.themans.street_stall.databinding.FragmentLoginBinding



class LoginFragment : Fragment(){

    companion object {
        private const val TAG = "LoginFragment"
    }

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        viewModel.userInfo.observe(viewLifecycleOwner){
            Log.d(TAG, "userInfo: $it")

            //유저타입에 따라 다음화면으로 이동
            if(it.userState == 0){

            } else if(it.userState == 1){

            } else if(it.userState == 2){

            }
        }

        return binding.root
    }
}