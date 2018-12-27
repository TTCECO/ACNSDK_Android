package io.ttcnet.ttc_pay_demo_officer.adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.ttcnet.ttc_pay_demo_officer.R
import io.ttcnet.ttc_pay_demo_officer.model.FurnitureModel
import io.ttcnet.ttc_pay_demo_officer.util.Utils
import io.ttcnet.ttc_pay_demo_officer.viewmodel.CartViewModel
import java.util.*

/**
 * Created by lwq on 2018/12/18.
 */
class CartAdapter : RecyclerView.Adapter<CartAdapter.CartHolder> {
    var context: Context? = null
    var data: ArrayList<FurnitureModel>? = null
    var cartViewModel: CartViewModel? = null

    constructor(context: Context?, data: ArrayList<FurnitureModel>?, cartViewModel: CartViewModel) {
        this.context = context
        this.data = data
        this.cartViewModel = cartViewModel
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CartHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_cart_layout, p0, false)
        return CartHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: CartHolder, index: Int) {
        var furniture = data?.get(index)
        if (furniture != null) {
            holder.ivCover.setImageResource(furniture.imgResId)
            holder.tvTitle.setText(furniture.title)
            holder.tvDes.setText(furniture.description)
            holder.tvPrice.setText(Utils.getCurrencySymbol(furniture.priceType) + Utils.displayDecimal(furniture.price))

            holder.itemView.setOnLongClickListener {

                Utils.showDialog(context, R.string.are_u_sure_delete_it,
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        cartViewModel?.removeFurniture(furniture)
                    })

                false
            }
        }
    }


    inner class CartHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivCover = itemView.findViewById<ImageView>(R.id.item_goods_cover_iv)
        var tvTitle = itemView.findViewById<TextView>(R.id.item_goods_title)
        var tvDes = itemView.findViewById<TextView>(R.id.item_goods_des)
        var tvPrice = itemView.findViewById<TextView>(R.id.item_goods_price)

    }

}