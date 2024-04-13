package com.sciflare.message.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat

object Extensions {

    fun Context.isPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Permission.READ_SMS_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this, Permission.SEND_SMS_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun Context.isPermissionsDenied(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Permission.READ_SMS_PERMISSION
        ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
            this, Permission.SEND_SMS_PERMISSION
        ) == PackageManager.PERMISSION_DENIED
    }

    fun Context.callSettingsPage() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        this.startActivity(intent)
    }

    fun EditText.onChange(cb: (String) -> Unit) {

        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s.toString().replace("  ", " ")
                cb(s.toString().trim())
            }
        })
    }
}