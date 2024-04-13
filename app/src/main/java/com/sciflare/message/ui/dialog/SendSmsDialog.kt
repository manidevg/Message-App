package com.sciflare.message.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.sciflare.message.R
import com.sciflare.message.databinding.DialogSendSmsBinding
import com.sciflare.message.utils.Extensions.onChange
import com.sciflare.message.utils.SecurityUtils
import com.sciflare.message.utils.UtilFunctions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SendSmsDialog(
    private val context: Context,
    private val smsCallBack: SmsCallBack
) : Dialog(context), OnClickListener {
    private var vb: DialogSendSmsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = DialogSendSmsBinding.inflate(layoutInflater)
        vb?.root?.let { setContentView(it) }

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            ((context.resources.displayMetrics.widthPixels * 0.90).toInt()),
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        initView()

    }

    private fun initView() {
        vb?.ivCancel?.setOnClickListener(this)
        vb?.btnSend?.setOnClickListener(this)
        vb?.etMessage?.setText("")
        enableBtn()
        vb?.etMobileNumber?.onChange {
            enableBtn()
        }
        vb?.etMessage?.onChange {
            enableBtn()
        }
    }

    private fun enableBtn() {
        val mobileNumber = vb?.etMobileNumber?.text?.trim().toString()
        val message = vb?.etMessage?.text?.trim().toString()
        vb?.tvMessageCount?.text =
            context.getString(R.string.message_count, message.length.toString())
        if ((mobileNumber.length == 10) && UtilFunctions.isValidPhoneNumber(
                mobileNumber
            ) && (message.length!! >= 2)
        ) {
            vb?.btnSend?.alpha = 1F
            vb?.btnSend?.isEnabled = true
        } else {
            vb?.btnSend?.alpha = 0.3F
            vb?.btnSend?.isEnabled = false
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            vb?.ivCancel?.id -> {
                dismiss()
            }

            vb?.btnSend?.id -> {
                vb?.btnSend?.text = context.getString(R.string.loading)
                runBlocking {
                    launch {
                        val encryptText = SecurityUtils.encrypt(
                            vb?.etMessage?.text.toString().trim() + "|${context.packageName}|"
                        )
                        UtilFunctions.sendSMS(
                            context,
                            vb?.etMobileNumber?.text.toString().trim(),
                            encryptText
                        )
                        delay(1500)
                        dismiss()
                        smsCallBack.smsSent()
                    }
                }
            }
        }
    }

    interface SmsCallBack {
        fun smsSent()
    }
}