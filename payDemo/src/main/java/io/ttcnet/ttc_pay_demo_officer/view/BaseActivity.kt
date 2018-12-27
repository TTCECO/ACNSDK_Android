package io.ttcnet.ttc_pay_demo_officer.view

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    protected var activity = this
    var activitys = ArrayList<Activity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitys.add(activity)
    }

//    fun finishActivity(clazz: Class<Any>){
//        for (activity in activitys) {
//            if (activity.javaClass == clazz){
//                activity.finish()
//                break
//            }
//        }
//    }
}
