package io.ttcnet.sdk

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ttc.behavior.IManager
import com.ttc.behavior.TTCAgent
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        TTCAgent.setEnvProd(false)

    }

    fun register(v: View) {
        main_msg_tv.text = "registering..."

        var userIdTemp = main_user_id_et.getText().toString()
        TTCAgent.register(userIdTemp, object : IManager.UserInfoCallback {
            override fun success(map: Map<String, String>) {
                main_msg_tv.text = "register successfully, " + map.toString()
                userId = userIdTemp
            }

            override fun error(msg: String) {
                main_msg_tv.text = "register error, " + msg
                userId = ""
            }
        })
    }

    fun uploadLikeComment(v: View) {
        if (userId != "") {
            var intent = Intent(this, BehaviorActivity::class.java)
            startActivity(intent)
        }
    }

    fun uploadAdsBehavior(v: View) {
        var intent = Intent(this, AdsActivity::class.java)
        startActivity(intent)
    }

}
