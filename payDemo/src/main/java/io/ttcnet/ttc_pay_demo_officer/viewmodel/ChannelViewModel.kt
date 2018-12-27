package io.ttcnet.ttc_pay_demo_officer.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.ttcnet.ttc_pay_demo_officer.model.PayChannelModel
import io.ttcnet.ttc_pay_demo_officer.repository.PayChannelRepository

/**
 * Created by lwq on 2018/12/18.
 */
class ChannelViewModel : ViewModel() {

    var channels: LiveData<ArrayList<PayChannelModel>> = PayChannelRepository.getAll()

    fun setChecked(channelId: Int) {
        var all = channels.value
        if (all != null) {
            for (channel in all) {
                if (channel.id == channelId) {
                    channel.checked = true
                    break
                }
            }
        }
//        channels = PayChannelRepository.getAll()
    }

    fun setUnchecked(channelId: Int) {
        var all = channels.value
        if (all != null) {
            for (channel in all) {
                if (channel.id == channelId) {
                    channel.checked = false
                    break
                }
            }
        }
//        channels = PayChannelRepository.getAll()
    }

    fun clearChecked() {
        var all = channels.value
        if (all != null) {
            for (channel in all) {
                channel.checked = false
            }
        }
//        channels = PayChannelRepository.getAll()
    }
}