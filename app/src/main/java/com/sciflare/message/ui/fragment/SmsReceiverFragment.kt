package com.sciflare.message.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.sciflare.message.R
import com.sciflare.message.base.DataBindingFragment
import com.sciflare.message.databinding.FragmentSmsReceiverBinding
import com.sciflare.message.ui.activity.MainActivity
import com.sciflare.message.ui.adapter.SmsListAdapter
import com.sciflare.message.ui.viewmodel.SmsSenderFragmentViewModel


class SmsReceiverFragment : DataBindingFragment<FragmentSmsReceiverBinding>() {
    lateinit var smsSenderFragmentViewModel: SmsSenderFragmentViewModel

    override fun layoutId() = R.layout.fragment_sms_receiver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        smsSenderFragmentViewModel =
            ViewModelProvider(this).get(SmsSenderFragmentViewModel::class.java)
        vb.vm = smsSenderFragmentViewModel
        initView()
        observer()
    }

    private fun initView() {
        vb.tvNodata.visibility = View.GONE
        vb.ilLoader.clProgress.visibility = View.VISIBLE
        smsSenderFragmentViewModel.getAllSms(requireContext(), "1")
    }

    private fun observer() {
        smsSenderFragmentViewModel.movieList.observe(viewLifecycleOwner) {
            vb.ilLoader.clProgress.visibility = View.GONE
            if (!it.isNullOrEmpty()) {
                vb.rvMessages.adapter = SmsListAdapter(it)
            } else {
                vb.tvNodata.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireContext() as MainActivity).setTitle(getString(R.string.received_sms))
    }
}