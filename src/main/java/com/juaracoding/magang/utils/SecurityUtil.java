package com.juaracoding.magang.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utilitas enkripsi dan dekripsi Base64 untuk keamanan kredensial.
 * Menghindari penyimpanan kata sandi dalam bentuk plaintext di dalam repositori publik GitHub.
 */
public class SecurityUtil {

    private SecurityUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Men-decode string Base64 kembali ke teks biasa (plaintext).
     *
     * @param base64String String terenkripsi Base64
     * @return Plaintext hasil decode
     */
    public static String decode(String base64String) {
        if (base64String == null || base64String.trim().isEmpty()) {
            return "";
        }
        byte[] decodedBytes = Base64.getDecoder().decode(base64String.trim());
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Meng-encode string plaintext ke dalam format Base64.
     *
     * @param plainText String plaintext yang akan di-encode
     * @return String ter-encode Base64
     */
    public static String encode(String plainText) {
        if (plainText == null) {
            return "";
        }
        return Base64.getEncoder().encodeToString(plainText.getBytes(StandardCharsets.UTF_8));
    }
}

