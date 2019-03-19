package com.plusbueno.plusbueno.parser.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by LZMA on 2018/10/30.
 */
public class DoubleSHA1PasswordHasher implements PasswordHasher {

    @Override
    public String hash(String password) {
        MessageDigest messageDigest;
        try{
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            Log.e("DoubleSHA1PasswordHasher", e.toString());
            return null;
        }
        messageDigest.update(password.getBytes());
        messageDigest.update(messageDigest.digest());

        return convertToHex(messageDigest.digest());

    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfByte = (b >>> 4) & 0x0F;
            int twoHalves = 0;
            do {
                buf.append((0 <= halfByte) && (halfByte <= 9) ? (char) ('0' + halfByte) : (char) ('a' + (halfByte - 10)));
                halfByte = b & 0x0F;
            } while (twoHalves++ < 1);
        }
        return buf.toString();
    }
}
