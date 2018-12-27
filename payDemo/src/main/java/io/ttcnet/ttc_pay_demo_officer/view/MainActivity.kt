package io.ttcnet.ttc_pay_demo_officer.view

import android.os.Bundle
import android.view.View
import io.ttcnet.ttc_pay_demo_officer.R
import io.ttcnet.ttc_pay_demo_officer.adapter.MainPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {


    private lateinit var adapter :MainPagerAdapter
    private lateinit var furnitureFragment :FurnitureFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

    }

    override fun onResume() {
        super.onResume()

    }

    private fun initViews(){

        furnitureFragment = FurnitureFragment.newInstance()
        adapter = MainPagerAdapter(supportFragmentManager)
        adapter.fragments = listOf(furnitureFragment)
        main_view_pager.adapter = adapter

        main_tab_layout.visibility = View.GONE
    }


}
