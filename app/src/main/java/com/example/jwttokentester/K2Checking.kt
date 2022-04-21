package com.example.jwttokentester

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.jwttokentester.crypto.CryptoHelper
import java.io.InputStream
import java.security.PublicKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

class K2Checking(val context: Context) {

    fun decodeK2(input: String): String {
        Log.i(TAG, "decodeK2: $input")
        val cryptoHelper: CryptoHelper = CryptoHelper.getInstance(context)
        var clearKey: StringBuffer? = null
        var encKey: StringBuffer? = null
        if (input != null && input != "") {
            encKey = StringBuffer(input)
            clearKey = try {
                if (BuildConfig.DEBUG && (encKey.length == 24 || encKey.length == 32)) {
                    encKey
                } else cryptoHelper.decode(encKey)
            } catch (e: SecurityException) {
                throw SecurityException("Error decrypting K2", e)
            }
        }
        return clearKey.toString()
    }

    fun getSecretKeyFormCert(certificateName: String): PublicKey? {

        val certificate = getCertFromAssets(certificateName)
        val pk: PublicKey
        if (certificate == null) {
            return null
        } else {
            pk = certificate.publicKey

        }
        return pk
    }

    private fun getCertFromAssets(certName: String): X509Certificate? {
        val certFactory: CertificateFactory
        val inStream: InputStream
        var cer: X509Certificate? = null
        try {
            certFactory = CertificateFactory
                .getInstance("X.509")

            inStream = context.assets.open("certs/$certName")
            cer = certFactory.generateCertificate(inStream) as X509Certificate
            inStream.close()
            return cer
        } catch (e: Exception) {
            Log.e(TAG, "getCertFromAssets: error")
        }
        return cer
    }
}