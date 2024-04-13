package com.sciflare.message.ui.fragment

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sciflare.message.R
import com.sciflare.message.base.DataBindingFragment
import com.sciflare.message.databinding.FragmentSmsSenderBinding
import com.sciflare.message.ui.activity.MainActivity
import com.sciflare.message.ui.adapter.SmsListAdapter
import com.sciflare.message.ui.dialog.SendSmsDialog
import com.sciflare.message.ui.viewmodel.SmsSenderFragmentViewModel
import com.sciflare.message.utils.Extensions.callSettingsPage
import com.sciflare.message.utils.Extensions.isPermissionsDenied
import com.sciflare.message.utils.Extensions.isPermissionsGranted

class SmsSenderFragment : DataBindingFragment<FragmentSmsSenderBinding>(), View.OnClickListener {

    lateinit var smsSenderFragmentViewModel: SmsSenderFragmentViewModel

    override fun layoutId() = R.layout.fragment_sms_sender

    private var isFirstTime = true
    private var isFromSettings = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        smsSenderFragmentViewModel =
            ViewModelProvider(this).get(SmsSenderFragmentViewModel::class.java)
        vb.vm = smsSenderFragmentViewModel
        initView()
        observer()
    }

    private fun initView() {
        vb.fbReceived.setOnClickListener(this)
        vb.fbSend.setOnClickListener(this)
        vb.fbReceived.visibility = View.GONE
        vb.fbSend.visibility = View.GONE
        checkPermission()
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

    override fun onClick(view: View?) {
        when (view?.id) {
            vb.fbReceived.id -> {
                findNavController().navigate(R.id.action_smsSenderFragment_to_smsReceiverFragment)
            }

            vb.fbSend.id -> {
                showSendSmsDialog()
            }
        }
    }

    private fun loadSms() {
        vb.fbReceived.visibility = View.VISIBLE
        vb.fbSend.visibility = View.VISIBLE
        vb.tvNodata.visibility = View.GONE
        vb.ilLoader.clProgress.visibility = View.VISIBLE
        smsSenderFragmentViewModel.getAllSms(requireContext(), "2")
    }

    private fun checkPermission() {
        if (requireContext().isPermissionsGranted()) {
            loadSms()
        } else if (requireContext().isPermissionsDenied()) {
            launchPermissions()
        } else if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)
            || !shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)
        ) {
            launchPermissions()
        }
    }

    private fun launchPermissions() {
        multiplePermissionsLauncher.launch(
            arrayOf(
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
            )
        )
    }

    private val multiplePermissionsLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                checkPermission()
            } else {
                if (isFirstTime) {
                    isFromSettings = true
                    requireContext().callSettingsPage()
                }
            }
        }

    override fun onResume() {
        super.onResume()
        (requireContext() as MainActivity).setTitle(getString(R.string.send_sms))
        if (!isFirstTime || isFromSettings) {
            isFromSettings = false
            if (requireContext().isPermissionsGranted()) {
                loadSms()
            }
        }
    }

    private fun showSendSmsDialog() {
        SendSmsDialog(requireContext(), object : SendSmsDialog.SmsCallBack {
            override fun smsSent() {
                Toast.makeText(requireContext(),getString(R.string.sms_sent_successfully),Toast.LENGTH_SHORT).show()
                loadSms()
            }
        }).show()
    }


}