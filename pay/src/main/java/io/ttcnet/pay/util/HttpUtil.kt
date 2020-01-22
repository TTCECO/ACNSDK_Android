package io.ttcnet.pay.util

import android.content.Context
import android.util.Base64
import android.util.Log
import com.google.protobuf.nano.MessageNano
import java.io.IOException
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.URL
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * Created by lwq on 2018/12/4.
 */
object HttpUtil {
    private val TIMEOUT_MS = 30000  //30s


    fun post(context: Context, requestXXX: MessageNano, cmdId: Int): ByteArray? {

        val serverUrl = URL(Util.getServerUrl(context))
        var connection: HttpURLConnection = serverUrl.openConnection() as HttpURLConnection

        connection.connectTimeout = TIMEOUT_MS
        connection.readTimeout = TIMEOUT_MS
        connection.doOutput = true
        connection.doInput = true
        connection.setRequestProperty("Connection", "Keep-Alive")
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")

        try {
            connection.requestMethod = "POST"
        } catch (e: ProtocolException) {
            e.printStackTrace()
            return null
        }

        val content = MessageNano.toByteArray(requestXXX)
        val entries = createHeader(context, SPUtil.getAppId(context), content, cmdId)
        for (entry in entries) {
            connection.setRequestProperty(entry.key, entry.value)
        }
        try {
            val outputStream = connection.outputStream
            outputStream.write(content)
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        try {
            connection.connect()
            val responseCode = connection.responseCode
            if (responseCode == 200) {
                val inputStream = connection.inputStream
                return inputStream.readBytes()
            } else {
                val message = connection.responseMessage
                Log.d("", message)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    private fun createHeader(context: Context, appId: String, body: ByteArray, cmdId: Int): Map<String, String> {
        val header = LinkedHashMap<String, String>()
        header["AppID"] = appId
        header["Body"] = Base64.encodeToString(body, Base64.NO_WRAP)
        header["NonceStr"] = UUID.randomUUID().toString().replace("-", "")
        header["SignType"] = "MD5"
        header["Timestamp"] = System.currentTimeMillis().toString()
        header["secretKey"] = SPUtil.getSymmetricKey(context)

//        header.toSortedMap(Comparator { p0, p1 -> (p0[0] - p1[0]) })
        var sb = StringBuilder()
        for (map in header) {
            if (!sb.isEmpty()) {
                sb.append("&")
            }
            sb.append(map.key).append("=").append(map.value)
        }
        Log.d("headerSign", sb.toString())
        header["RequestSign"] = EncryptUtil.md5(sb.toString()).toUpperCase()

        header.remove("Body")
        header.remove("secretKey")
        header["cmdId"] = cmdId.toString()
        return header
    }


}