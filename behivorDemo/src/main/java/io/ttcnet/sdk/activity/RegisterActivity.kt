package io.ttcnet.sdk.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.acn.behavior.ACNAgent
import com.acn.behavior.IManager
import io.ttcnet.sdk.R
import io.ttcnet.sdk.utils.IntentKey
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private var userId = ""
    private var info = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        main_msg_tv.append("isProd:" + ACNAgent.isEnvProd());

        btn_register.setOnClickListener { register() }
        btn_like_comment.setOnClickListener { uploadLikeComment() }
        btn_ads.setOnClickListener { uploadAdsBehavior() }
    }

    fun register() {
        main_msg_tv.text = "registering..."

        var userIdTemp = main_user_id_et.getText().toString()
        ACNAgent.register(userIdTemp, object : IManager.UserInfoCallback {
            override fun success(map: Map<String, String>) {
                info = map.toString();
                main_msg_tv.text = "register successfully, " + info
                userId = userIdTemp

                var ts = map.get("addtime")
                if (ts != null) {
                    var date = SimpleDateFormat().format(Date((ts.toLong()) * 1000))
                    main_msg_tv.append(", addtime:" + date)
                }
            }

            override fun error(msg: String) {
                main_msg_tv.text = "register error, " + msg
                userId = ""
            }
        })
    }

    fun uploadLikeComment() {
        if (userId != "") {
            var intent = Intent(this, BehaviorActivity::class.java)
            intent.putExtra(IntentKey.CONTENT, info)
            startActivity(intent)
        }
    }

    fun uploadAdsBehavior() {
        var intent = Intent(this, AdsActivity::class.java)
        startActivity(intent)
    }

}
