package io.ttcnet.ttc_pay_demo_officer.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.ttcnet.ttc_pay_demo_officer.R
import io.ttcnet.ttc_pay_demo_officer.adapter.FurnitureAdapter
import io.ttcnet.ttc_pay_demo_officer.model.FurnitureModel
import io.ttcnet.ttc_pay_demo_officer.util.Utils
import io.ttcnet.ttc_pay_demo_officer.viewmodel.CartViewModel
import io.ttcnet.ttc_pay_demo_officer.viewmodel.FurnitureViewModel
import kotlinx.android.synthetic.main.furniture_fragment.*


class FurnitureFragment : Fragment() {

    private lateinit var adapter: FurnitureAdapter
    private var furnitures = ArrayList<FurnitureModel>()

    companion object {
        fun newInstance() = FurnitureFragment()
    }

    private lateinit var furnitureViewModel: FurnitureViewModel
    private lateinit var cartViewModel: CartViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.furniture_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        furnitureViewModel = ViewModelProviders.of(this).get(FurnitureViewModel::class.java)
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)

        var data = furnitureViewModel.furnitureLiveData.value
        if (data != null) {
            furnitures.addAll(data)
        }

        initViews()
    }

    private fun initViews() {
        adapter = FurnitureAdapter(context, furnitures, cartViewModel)
        main_rv.layoutManager = LinearLayoutManager(context)
        main_rv.adapter = adapter

        furnitureViewModel.furnitureLiveData.observe(this, Observer {

            if (it != null) {
                furnitures.clear()
                furnitures.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        cartViewModel.checkedFurniture.observe(this, Observer {
            if (it != null && it.size > 0) {
                main_cart_goods_num.visibility = View.VISIBLE
                main_cart_goods_num.setText(it.size.toString())
            }else{
                main_cart_goods_num.visibility = View.INVISIBLE
            }
        })

        main_cart.setOnClickListener {
            var goods = cartViewModel.checkedFurniture.value
            if ( goods!= null && goods.size > 0) {
                startActivity(Intent(context, CartActivity::class.java))
            }else{
                Utils.toast(activity, "there is no goods")
            }
        }
    }

}
