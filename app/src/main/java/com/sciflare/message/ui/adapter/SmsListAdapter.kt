package com.sciflare.message.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sciflare.message.R
import com.sciflare.message.databinding.LayoutSmsListItemBinding
import com.sciflare.message.model.SmsModel

class SmsListAdapter(
    private var smsData: List<SmsModel>,
) : RecyclerView.Adapter<SmsListAdapter.SmsListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SmsListHolder(
        DataBindingUtil.inflate
            (
            LayoutInflater.from(parent.context),
            R.layout.layout_sms_list_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: SmsListHolder, position: Int) {
        holder.bind(smsData[position])
    }

    override fun getItemCount(): Int {
        return smsData.size
    }

    class SmsListHolder(var layoutSmsListItemBinding: LayoutSmsListItemBinding) :
        RecyclerView.ViewHolder(layoutSmsListItemBinding.root) {

        fun bind(obj: SmsModel) {
            layoutSmsListItemBinding.tvNumber.text = obj.receiverNumber
            layoutSmsListItemBinding.tvMessage.text = obj.message
        }
    }
}