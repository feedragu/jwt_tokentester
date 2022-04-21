package com.example.jwttokentester.crypto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;


public class CryptoHelper {

    private static final String VALUE_P = "P";
    private boolean initialized = false;
    private X509Certificate cert = null;
    private PrivateKey decryptKey = null;
    private String p12 = null;
    private String alias = null;
    private String pwd = null;
    private KeyStore ks = null;
    private static CryptoHelper instance = null;
    private final Context context;
    private static final String enc = "hPVPDpq5xlpnYu2ZURYnlnbg2TbR6TRh";


    private CryptoHelper(Context context) {
        this.context = context;
        init();
    }

    public static CryptoHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CryptoHelper(context);
        }

        return instance;
    }

    /**
     * Initializes AMMS P12 keystore and certificate settings getting values from command line system properties and weblogic UserConfig security files
     */
    @SuppressLint("CheckResult")
    private void init() {

        if (Security.getProvider("BC") == null)
            Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 1);


    }

    /**
     * Retrieves the AMMS certificate from the P12 key store
     *
     * @return certificate
     * @throws SecurityException
     */
    public X509Certificate getCertificate() throws SecurityException {

        try {
            if (cert == null) {
                //String alias = ks.aliases().nextElement();
                cert = (X509Certificate) ks.getCertificate(alias);
            }
        } catch (KeyStoreException ex) {
            throw new SecurityException("Unable to get certificate from keystore", ex);
        }

        return cert;
    }

    /**
     * Retrieves private key from AMMS certificate in P12 keystore
     *
     * @return
     * @throws SecurityException
     */
    public PrivateKey getPrivate() throws SecurityException {
        if (decryptKey == null) decryptKey = getPrivateKeyFromP12(p12, alias, pwd);
        return decryptKey;
    }

    /**
     * Encodes a string using AMMS certificate from P12 keystore
     *
     * @param input string to encode
     * @return encoded string
     * @throws SecurityException
     */
    public StringBuffer encode(StringBuffer input) throws SecurityException {
        if (!initialized) init();

        PublicKey encryptKey = null;
        StringBuffer output = null;

        try {
            cert = getCertificate();
            encryptKey = cert.getPublicKey();
            output = Asymmetric.encrypt(input, encryptKey);

        } catch (Exception ex) {
            throw new SecurityException("Unable to encrypt data (file: " + p12 + " entry: " + alias + " pwd: " + pwd + ")", ex);
        }

        return output;
    }


    /**
     * Decodes a string using AMMS certificate from P12 keystore
     *
     * @param input string to decode
     * @return decoded string
     * @throws SecurityException
     */
    public StringBuffer decode(StringBuffer input) throws SecurityException {
        File file = getFileAssets(context);

        p12 = file.getPath();
        Log.i("TAG", "init: " + file.getPath());

        try {
//            alias = AESCrypt.decrypt(enc, "f1siHtD2Olc6qYnVj7I1e/V9/Sl0ypFl84bnFoF3odw=");
//            pwd = AESCrypt.decrypt(enc, "dPq2lzD/thSYWF+mrxQeeA==");
            alias = "WFMPrivKey";
            pwd = "vl4d1m1rh0R0w1tZ";
            Log.i("TAG", "decode: "+alias +"\n"+pwd);
            InputStream inStream = new FileInputStream(p12);
            ks = KeyStore.getInstance("PKCS12");
            Log.i("TAG", "ks var: " + ks.getType());

            ks.load(inStream, pwd.toCharArray());
            inStream.close();
        } catch (FileNotFoundException e) {
            throw new SecurityException("FileNotFoundException: Unable to load keystore from file: " + p12 + ", entry: " + alias, e);
        } catch (KeyStoreException e) {
            throw new SecurityException("KeyStoreException: Unable to load keystore from file: " + p12 + ", entry: " + alias, e);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException("NoSuchAlgorithmException: Unable to load keystore from file: " + p12 + ", entry: " + alias, e);
        } catch (GeneralSecurityException e) {
            throw new SecurityException("CertificateException: Unable to load keystore from file: " + p12 + ", entry: " + alias, e);
        } catch (IOException e) {
            throw new SecurityException("IOException: Unable to load keystore from file: " + p12 + ", entry: " + alias, e);
        }
        initialized = true;

        PrivateKey decKey = null;
        StringBuffer output = null;

        try {
            decKey = getPrivate();
            Log.i("test", "decode: " + decKey);
            output = Asymmetric.decrypt(input, decKey);
        } catch (Exception ex) {
            throw new SecurityException("Unable to decrypt data (file: " + p12 + " entry: " + alias + " pwd: " + pwd + ")", ex);
        }

        return output;
    }

    private PrivateKey getPrivateKeyFromP12(String p12file, String alias, String password) throws SecurityException {
        try {
            return (PrivateKey) ks.getKey(alias, password.toCharArray());
        } catch (KeyStoreException ex) {
            throw new SecurityException("KeyStoreException: Unable to get private key from keystore", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new SecurityException("NoSuchAlgorithmException: Cryptographic algorithm not found", ex);
        } catch (UnrecoverableKeyException ex) {
            throw new SecurityException("UnrecoverableKeyException: Unable to recover private key", ex);
        }
    }

    private static File getFileAssets(Context context) {
//        String company = BuildConfig.FLAVOR.replace("prod", "");
        File f = new File(context.getCacheDir() + "/TwoBeatTestKeystore");
        if (f.exists()) {
//            LegoLogHelper.info("Clearing old ks...");
            f.delete();
        }
        try {

//            LegoLogHelper.info("Loading keystore for " + company + (ARETI_FLAVOR.equals(BuildConfig.FLAVOR) ? (", env: " + BuildConfig.ENV) : ""));

            InputStream is;

            is = context.getAssets().open("certs/" + KEYSTORE_NAME);


            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();


            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();

//            LegoLogHelper.info("Succesfully loaded keystore for " + company);
            return f;
        } catch (Exception e) {
//            LegoLogHelper.error("Error loading keystore for " + company, e);
            throw new RuntimeException(e);
        }
    }
//
//    private static String getKSNameWithEnv() {
//        return BuildConfig.KEYSTORE_NAME + "_" + BuildConfig.ENV + ".p12";
//    }

    public static final String KEYSTORE_NAME = "ks_ireti_Q.p12";
}


