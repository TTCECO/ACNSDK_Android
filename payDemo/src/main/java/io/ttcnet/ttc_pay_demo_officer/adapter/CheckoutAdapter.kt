package io.ttcnet.ttc_pay_demo_officer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.ttcnet.ttc_pay_demo_officer.R
import io.ttcnet.ttc_pay_demo_officer.constant.Constant
import io.ttcnet.ttc_pay_demo_officer.model.PayChannelModel
import io.ttcnet.ttc_pay_demo_officer.util.Utils
import io.ttcnet.ttc_pay_demo_officer.viewmodel.ChannelViewModel

/**
 * Created by lwq on 2018/12/18.
 */
class CheckoutAdapter : RecyclerView.Adapter<CheckoutAdapter.CheckoutHolder> {

    private var context: Context? = null
    private var data: ArrayList<PayChannelModel>? = null
    private lateinit var channelViewModel: ChannelViewModel
    private var callback: CheckedChannelCallback? = null


    constructor(context: Context?, data: ArrayList<PayChannelModel>?, channelViewModel: ChannelViewModel) : super() {
        this.context = context
        this.data = data
        this.channelViewModel = channelViewModel
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CheckoutHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_checkout_layout, p0, false)
        return CheckoutHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: CheckoutHolder, p1: Int) {
        var channel = data?.get(p1)
        if (channel != null) {
            holder.ivIcon.setImageResource(channel.iconUncheckedResId)
            holder.tvName.setText(channel.name)

            if (channel.checked) {
                holder.root.setBackgroundResource(channel.checkedBgColorId)
                holder.ivIcon.setImageResource(channel.iconCheckedResId)
                holder.tvName.visibility = View.GONE
            } else {
                holder.root.setBackgroundResource(R.color.white)
                holder.ivIcon.setImageResource(channel.iconUncheckedResId)
                holder.tvName.visibility = View.VISIBLE
            }

            holder.itemView.setOnClickListener {
                if (channel.id == Constant.PAY_CHANNEL_TTC_ID || channel.id == Constant.PAY_CHANNEL_ACN_ID) {
                    if (callback != null) {
                        callback!!.done(channel.id)
                    }
                } else {
                    Utils.toast(context, "not open yet")
                }
            }
        }
    }


    inner class CheckoutHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var root: ViewGroup = itemView.findViewById(R.id.item_checkout_root)
        var ivIcon: ImageView = itemView.findViewById(R.id.item_checkout_iv)
        var tvName: TextView = itemView.findViewById(R.id.item_checkout_name)
//        var ivCheckedIcon: ImageView = itemView.findViewById(R.id.item_checkout_checked_iv)
    }

    interface CheckedChannelCallback {
        fun done(channelId: Int)
    }

    fun setCheckedChannelCallback(callback: CheckedChannelCallback) {
        this.callback = callback
    }
}