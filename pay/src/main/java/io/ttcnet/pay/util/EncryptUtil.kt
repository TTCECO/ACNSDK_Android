package io.ttcnet.pay.util

import android.content.Context
import android.util.Base64
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

/**
 * Created by lwq on 2018/12/5.
 */
object EncryptUtil {
    val MD5 = "md5"
    val RSA = "RSA"
    val SHA1_WITH_RSA = "SHA1withRSA"

    fun md5(content: String): String {
        var md5 = MessageDigest.getInstance(MD5)
        val digestByteArray = md5.digest(content.toByteArray(Charsets.UTF_8))

        return bytes2String(digestByteArray)
    }

    fun bytes2String(bytes: ByteArray): String {
        var sb = StringBuilder()
        for (byte in bytes) {
            val v = byte.toInt() and 0xFF
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                sb.append(0)
            }
            sb.append(hv)
        }
        return sb.toString()
    }

    fun rsaVerifySha1(context: Context, content: String, sign: String): Boolean {
        var keyFactory = KeyFactory.getInstance(RSA);
        var encodedKey = Base64.decode(SPUtil.getTTCPublicKey(context), Base64.DEFAULT);
        var pubKey = keyFactory.generatePublic(X509EncodedKeySpec(encodedKey));

        var signature = Signature.getInstance(SHA1_WITH_RSA);
        signature.initVerify(pubKey);
        signature.update(content.toByteArray());

        var isRight = signature.verify(Base64.decode(sign, Base64.NO_WRAP));
        return isRight;
    }
}