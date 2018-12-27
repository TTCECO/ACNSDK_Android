package io.ttcnet.ttc_pay_demo_officer.adapter

import android.content.Context
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
 * Created by lwq on 2018/12/17.
 */
class FurnitureAdapter : RecyclerView.Adapter<FurnitureAdapter.MainHolder> {

    var goodsList = ArrayList<FurnitureModel>()
    var context: Context? = null
    var cartViewModel: CartViewModel? = null

    constructor(context: Context?, furnitureList: ArrayList<FurnitureModel>, cartViewModel: CartViewModel) {
        this.context = context
        this.goodsList = furnitureList
        this.cartViewModel = cartViewModel
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MainHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.item_furniture_layout, p0, false)

        return MainHolder(view)
    }

    override fun getItemCount(): Int {
        return goodsList.size
    }

    override fun onBindViewHolder(holder: MainHolder, p1: Int) {
        var furniture = goodsList[p1]

        holder.ivCover.setImageResource(furniture.imgResId)
        holder.tvTitle.setText(furniture.title)
        holder.tvDes.setText(furniture.description)
        holder.tvPrice.setText(Utils.getCurrencySymbol(furniture.priceType) + Utils.displayDecimal(furniture.price))

        holder.addlayout.setOnClickListener { cartViewModel?.addFurniture(furniture) }
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivCover = itemView.findViewById<ImageView>(R.id.item_goods_cover_iv)
        var tvTitle = itemView.findViewById<TextView>(R.id.item_goods_title)
        var tvDes = itemView.findViewById<TextView>(R.id.item_goods_des)
        var tvPrice = itemView.findViewById<TextView>(R.id.item_goods_price)
        var addlayout = itemView.findViewById<ViewGroup>(R.id.item_goods_add_layout)
    }
}