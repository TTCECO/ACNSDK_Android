package io.ttcnet.ttc_pay_demo_officer.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by lwq on 2018/12/17.
 */
class MainPagerAdapter(fm: FragmentManager) :FragmentPagerAdapter(fm) {
//    private lateinit var context:Context
     lateinit var fragments : List<Fragment>
//    private lateinit var tabIconResIds :List<Int>


    override fun getItem(index: Int): Fragment {

       return fragments[index]

    }

    override fun getCount(): Int {
        return fragments.size
    }


}