package com.example.jwttokentester.crypto;

import java.math.BigInteger;

/**
 * Created by danielm on 08/02/2018.
 */

public class Hex {

    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final String HEXES = "0123456789ABCDEF";

    public static String encodeHexString(String data) {
        return new String(encodeHex(data.getBytes()));
    }

    protected static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_UPPER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_UPPER[0x0F & data[i]];
        }
        return out;
    }

    public static String decodeHexString(String data) {
        return new String(decodeHex(data.getBytes()));
    }

    public static byte[] toIntBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)(((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16)) & 0xff);
        }

        return data;
    }

    public static byte[] toIntBytes(String s, int length){
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte)(((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16)) & 0xff);
        }

        return data;
    }

    public static int[] toInt(String s){
        int len = s.length();
        int[] data = new int[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16)));
        }

        return data;
    }

    public static byte[] hexToByte(String HexInput, int size) {
        try {
            if (HexInput.length() > 0) {
                int ndx = 1;
                int i = 0;
                int ByteCnt = 0;
                // Dim Buffer(size) As Byte

                ByteCnt = HexInput.length() / 2;
                byte[] ByteVar = new byte[size];
                for (i = 0; i < size; i++) {
                    if (i >= ByteCnt) {
                        continue;
                    }
                    BigInteger bi = new BigInteger(HexInput.substring(ndx - 1, ndx - 1 + 2), 16);
                    ByteVar[i] = bi.byteValue();
                    ndx = ndx + 2;
                }

                return ByteVar;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static byte[] decodeHex(byte[] data) {
        int len = data.length;

        if ((len & 0x01) != 0) {
            return null;
        }

        byte[] out = new byte[len >> 1];

        try {
            for (int i = 0, j = 0; j < len; i++) {

                int f = toDigit(data[j], j) << 4;
                j++;
                f = f | toDigit(data[j], j);
                j++;
                out[i] = (byte) (f & 0xFF);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return out;
    }

    protected static int toDigit(byte data, int index) throws Exception {
        int digit = Character.digit(data, 16);
        if (digit == -1) {
            throw new Exception("Illegal hexadecimal character " + data + " at index " + index);
        }
        return digit;
    }

    public static String getHex(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }
}