package com.example.jwttokentester.crypto;

import android.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;

public class Asymmetric {

    public Asymmetric() {
    }

    public static StringBuffer encrypt(StringBuffer text, PublicKey encryptKey)
            throws Exception {
        ByteArrayOutputStream bao = null;
        DataOutputStream dao = null;
        try {
            StringBuffer stringbuffer;
            try {
                bao = new ByteArrayOutputStream();
                dao = new DataOutputStream(bao);
                encrypt(((InputStream) (new ByteArrayInputStream(text.toString().getBytes()))), dao, encryptKey, BUFFERSIZE_TEXT);
                stringbuffer = new StringBuffer(new String(Base64.encode(bao.toByteArray(), Base64.DEFAULT)));
            } catch (IOException ioe) {
                ioe.printStackTrace();
                throw new Exception(ioe.getMessage());
            }
            return stringbuffer;
        } finally {
            if (dao != null)
                try {
                    dao.close();
                } catch (IOException e) {
                }
        }
    }

    public static void encrypt(InputStream is, DataOutputStream daos, PublicKey encryptKey, int bufferlength)
            throws Exception, IOException {
        Cipher cipher = null;
        try {
            Security.insertProviderAt(new BouncyCastleProvider(), 1);
            cipher = Cipher.getInstance("RSA/ECB/OAEPPadding", "BC");
            cipher.init(1, encryptKey);
            byte buffer[] = new byte[bufferlength];
            for (int length = 0; (length = is.read(buffer)) != -1; )
                cipher.update(buffer, 0, length);

            byte result[] = cipher.doFinal();
            daos.write(result);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException(ioe.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public static StringBuffer decrypt(StringBuffer text, PrivateKey decryptKey)
            throws Exception {
        ByteArrayOutputStream bao = null;
        DataOutputStream dao = null;
        try {
            StringBuffer stringbuffer;
            try {
                bao = new ByteArrayOutputStream();
                dao = new DataOutputStream(bao);
                decrypt(((InputStream) (new ByteArrayInputStream(Base64.decode(text.toString(), Base64.DEFAULT)))), dao, decryptKey, BUFFERSIZE_TEXT);
                stringbuffer = new StringBuffer(new String(bao.toByteArray()));
            } catch (IOException ioe) {
                ioe.printStackTrace();
                throw new Exception(ioe.getMessage());
            }
            return stringbuffer;
        } finally {
            if (dao != null)
                try {
                    dao.close();
                } catch (IOException e) {
                }
        }
    }

    public static void decrypt(InputStream is, DataOutputStream daos, PrivateKey decryptKey, int bufferlength)
            throws Exception, IOException {
        Cipher cipher = null;
        try {
            Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 1);
            cipher = Cipher.getInstance("RSA/ECB/OAEPPadding", "BC");
            cipher.init(2, decryptKey);
            byte buffer[] = new byte[bufferlength];
            for (int length = 0; (length = is.read(buffer)) != -1; )
                cipher.update(buffer, 0, length);

            byte result[] = cipher.doFinal();
            daos.write(result);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException(ioe.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    private static int BUFFERSIZE_TEXT = 64;

}
