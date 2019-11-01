package io.ttcnet.ttc_pay_demo_officer.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by lwq on 2018/12/17.
 */
class MainPagerAdapter(fm: androidx.fragment.app.FragmentManager) :
    androidx.fragment.app.FragmentPagerAdapter(fm) {
//    private lateinit var context:Context
     lateinit var fragments : List<androidx.fragment.app.Fragment>
//    private lateinit var tabIconResIds :List<Int>


    override fun getItem(index: Int): androidx.fragment.app.Fragment {

       return fragments[index]

    }

    override fun getCount(): Int {
        return fragments.size
    }


}