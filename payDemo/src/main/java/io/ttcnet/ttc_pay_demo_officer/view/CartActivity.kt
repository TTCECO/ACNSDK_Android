package io.ttcnet.ttc_pay_demo_officer.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.ttcnet.ttc_pay_demo_officer.R
import io.ttcnet.ttc_pay_demo_officer.adapter.CartAdapter
import io.ttcnet.ttc_pay_demo_officer.constant.Constant
import io.ttcnet.ttc_pay_demo_officer.model.FurnitureModel
import io.ttcnet.ttc_pay_demo_officer.util.Utils
import io.ttcnet.ttc_pay_demo_officer.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.appbar_layout.*

class CartActivity : BaseActivity() {


    private var furnitures = ArrayList<FurnitureModel>()
    private lateinit var cartViewModel: CartViewModel
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)

        appbar_left_iv1.setImageResource(R.mipmap.black_back)
        appbar_left_iv1.setOnClickListener { activity.onBackPressed() }

        appbar_left_text1.setText("Cart")

        adapter = CartAdapter(activity, furnitures, cartViewModel)
        cart_fragment_rv.layoutManager = LinearLayoutManager(activity)
        cart_fragment_rv.adapter = adapter

        cartViewModel.checkedFurniture.observe(this, Observer {
            if (it != null) {
                furnitures.clear()
                furnitures.addAll(it)
                adapter.notifyDataSetChanged()

                cart_fragment_price.setText("Total:"+ Utils.getCurrencySymbol(Constant.PRICE_TYPE_DOLLAR) + Utils.displayDecimal(calTotal()))

                if (it.size == 0) {
                    activity.finish()
                }
            }else{
                activity.finish()
            }
        })


        cart_fragment_checkout.setOnClickListener {
            activity.startActivityForResult(Intent(activity, CheckoutActivity::class.java), 10)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
    }


    private fun calTotal(): Double{
        var total = 0.0
        for (f in furnitures) {
            total += f.price
        }
        return  total
    }

}
