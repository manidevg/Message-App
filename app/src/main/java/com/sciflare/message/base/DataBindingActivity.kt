package com.sciflare.message.base

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class DataBindingActivity<VB : ViewDataBinding>: BaseActivity() {

    lateinit var vb: VB

    override fun setContentView(layoutResID: Int) {
        vb = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false)
        super.setContentView(vb.root)

    }
}