package com.sciflare.message.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sciflare.message.model.SmsModel
import com.sciflare.message.utils.UtilFunctions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SmsSenderFragmentViewModel : ViewModel() {

    val movieList = MutableLiveData<List<SmsModel>?>()
    private var job: Job? = null

    fun getAllSms(context: Context, smsType: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = UtilFunctions.readSms(context, smsType)
            withContext(Dispatchers.Main) {
                if (response != null) {
                    movieList.postValue(response)
                } else {
                    movieList.postValue(null)
                }
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}