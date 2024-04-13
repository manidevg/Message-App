package com.sciflare.message.ui.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.sciflare.message.R
import com.sciflare.message.base.DataBindingActivity
import com.sciflare.message.databinding.ActivityMainBinding
import com.sciflare.message.ui.fragment.SmsReceiverFragment
import com.sciflare.message.ui.fragment.SmsSenderFragment
import com.sciflare.message.ui.viewmodel.MainViewModel

class MainActivity : DataBindingActivity<ActivityMainBinding>() {

    lateinit var mainViewModel: MainViewModel

    override fun layoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        vb.vm = mainViewModel
        initView()
    }

    private fun initView() {
        vb.ivBack.setOnClickListener {
            backFlow()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backFlow()
            }
        })
    }

    private fun backFlow() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navContainer)
        when (navHostFragment?.childFragmentManager?.fragments?.get(0)) {
            is SmsSenderFragment -> finish()
            is SmsReceiverFragment -> {
                vb.navContainer.findNavController().popBackStack()
            }

        }
    }

    public fun setTitle(title: String){
        vb.tvTitle.text = title
    }

}