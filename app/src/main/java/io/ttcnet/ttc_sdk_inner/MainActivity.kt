package io.ttcnet.ttc_sdk_inner

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_behavior.setOnClickListener { startActivity(Intent(this, BehaviorActivity::class.java)) }

        main_pay.setOnClickListener { startActivity(Intent(this, OrderActivity::class.java)) }
    }
}
