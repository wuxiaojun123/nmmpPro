package com.nmmedit.apkprotect.util;

import java.util.Random;

public class RandomUtils {

    /***
     * 加密算法
     * @param ciphertext 原始字符串
     * @return
     */
    public static String encrypt(String ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        ciphertext = ciphertext.toLowerCase();

        char pass = genPrefixNameChar();
        int shift = pass-96;

        System.out.println("加密字符是" + pass+" 随机数是" + shift);

        for (char c : ciphertext.toCharArray()) {
            if (Character.isLowerCase(c)) {
                char decryptedChar = (char) ((c - 'a' - shift + 26) % 26 + 'a');
                plaintext.append(decryptedChar);
            } else {
                plaintext.append(c);
            }
        }

        plaintext.append(pass);

        return plaintext.toString();
    }

    /***
     * 解密算法
     * @param plaintext
     * @return
     */
    public static String decrypt(String plaintext) {
        StringBuilder ciphertext = new StringBuilder();
        plaintext = plaintext.toLowerCase();

        char[] chars = plaintext.toCharArray();
        // 生成随机数
        int shift = chars[plaintext.length()-1]-96;
        System.out.println("解密，密文的最后一个字符是" + chars[plaintext.length()-1] + " 解密出来的随机数是 " + shift);

        for (int i=0; i < (chars.length-1); i++) {
            char c = chars[i];
            if (Character.isLowerCase(c)) {
                char encryptedChar = (char) ((c - 'a' + shift) % 26 + 'a');
                ciphertext.append(encryptedChar);
            } else {
                ciphertext.append(c);
            }
        }

        return ciphertext.toString();
    }

    /***
     * a = 97
     * y=121
     * @return
     */
    private static char genPrefixNameChar() {
        Random random = new Random();
        String str = "abcdefghijklmnopqrstuvwxy";
        int number = random.nextInt(25);
        return str.charAt(number);
    }

    /***
     * 随机生成一个字符串
     * @return
     */
    public static String generateRandomClaName() {
        Random random = new Random();
        String str = "abcdefghijklmnopqrstuvwxyz";

        String cName = null;

        for (int i = 0; i < 1; i++) {
            StringBuilder sb = new StringBuilder();
            int ranInt = random.nextInt(10);
            if(ranInt <= 3) {
                ranInt = ranInt+4;
            }
            for (int j = 0; j < ranInt; j++) {
                int number = random.nextInt(26);
                boolean canj = (ranInt%2==0);

                if(j == 0) {
                    sb.append(Character.toUpperCase(str.charAt(number)));
                } else if(canj && j == 1) {
                    sb.append(Character.toUpperCase(str.charAt(number)));
                } else {
                    sb.append(str.charAt(number));
                }
            }
            cName = sb.toString();
        }
        return cName;
    }

    public static String generateRandomMethodName() {
        Random random = new Random();
        String str = "abcdefghijklmnopqrstuvwxyz";

        String cName = null;

        for (int i = 0; i < 1; i++) {
            StringBuilder sb = new StringBuilder();
            int ranInt = random.nextInt(10);
            if(ranInt <= 3) {
                ranInt = ranInt+4;
            }
            for (int j = 0; j < ranInt; j++) {
                int number = random.nextInt(26);
                boolean canj = (ranInt%2==0);

                if(j == 0) {
                    sb.append(str.charAt(number));
                } else if(canj && j == 1) {
                    sb.append(str.charAt(number));
                } else {
                    sb.append(str.charAt(number));
                }
            }
            cName = sb.toString();
        }
        return cName;
    }

}
