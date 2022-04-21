package com.example.jwttokentester

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import java.io.InputStream
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.security.PublicKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

class TokenChecking(val context: Context) {

    fun checkToken(token: String, certificateName: String): Boolean {
        Log.i("EnelTemp", "getSecretKeyFormCert: $token")
//        SecretKeySpec secret_key;
//        secret_key = new SecretKeySpec("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes(), "HmacSHA256"); //TODO per test hs256
        return try {
            if (getSecretKeyFormCert(certificateName) != null) {
//                Jwts.parserBuilder().setSigningKey(getSecretKeyFormCert(certificateName))
                Jwts.parser().setSigningKey(getSecretKeyFormCert(certificateName))
                    .parseClaimsJws(token);
            } else {

                Log.i("JwtException", "No  certificate installed")
                return false
            }
            Log.i("EnelTemp", "getSecretKeyFormCert: $token")
            Log.i(
                "Preghiamo assieme",
                "getSecretKeyFormCert:  ${getSecretKeyFormCert(certificateName)}"
            )
            true
            // we can safely trust the JWT
        } catch (e: JwtException) {
            if (e is ExpiredJwtException) {
                Log.i("JwtException", "checkToken:  dio santo")
            } else {
                Log.i("JwtException", "checkToken:  dio santo " + e.javaClass)
            }

            Toast.makeText(context, "Token non valido", Toast.LENGTH_LONG).show()
            // we *cannot* use the JWT as intended by its creator
            false
        } catch (e: IllegalArgumentException) {
            Toast.makeText(context, "Token non presente", Toast.LENGTH_LONG).show()
            false
        } catch (e: Exception) {
            Toast.makeText(context, "Token non valido - Errore generico", Toast.LENGTH_LONG).show()
            // we *cannot* use the JWT as intended by its creator
            false
        }
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