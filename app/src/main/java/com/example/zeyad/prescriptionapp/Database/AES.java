package com.example.zeyad.prescriptionapp.Database;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Zeyad on 7/5/2018.
 *
 * This class is responsible to encrypt/decrypt user passwords before storing it in the db.
 * It is used as a layer of protection in case the db fall under some unwanted users.
 *
 * It is use AES and a symmetric key cryptography.
 *
 * It consists of 2 methods:
 * 1- encrypt: to encrypt the password.
 * 2- decrypt: to decrypt the password.
 */

public class AES {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    /**
     * The method is used to encrypt the password before saving it to the db
     * @param value
     * @return
     * @throws Exception
     */
    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AES.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }

    /**
     * The method is used to decrypt the password after getting it from the db.
     * @param value
     * @return
     * @throws Exception
     */
    public static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AES.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }

    /**
     * The method is used to generate a new key for encryption/decryption.
     * @return
     * @throws Exception
     */
    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(AES.KEY.getBytes(),AES.ALGORITHM);
        return key;
    }
}
