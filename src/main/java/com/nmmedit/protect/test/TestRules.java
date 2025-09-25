package com.nmmedit.protect.test;

import com.nmmedit.apkprotect.dex2c.filters.SimpleRules;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class TestRules {

    public static void main(String[] args) {

        System.out.println("hello");

        final TestSimpleRules simpleRules = new TestSimpleRules();


//        try {
//            simpleRules.parse(new InputStreamReader(new FileInputStream("G:\\androidDemo2\\nmmp-master\\nmmp-master\\nmm-protect\\rules222.txt"), StandardCharsets.UTF_8));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        boolean flag = test("!com.kwad,com.*");
//        System.out.println("返回结果是"+flag);

        // newStr=com.kwad---typeRegexLcom/kwad;-----Lcom/kwad/sdk/utils/f$c$1;
        String classType = "Lcom/kwad/sdk/utils/f$c$1;";
        String typeRegex = "Lcom/kwad/.*;";
        if(classType.matches(typeRegex)) {
            System.out.println("匹配到了，");
        } else {
            System.out.println("匹配失败");
        }
        String nmmpStr = decrypt("eddrgi");
        System.out.println("nmmpStr = " + nmmpStr);

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

    private static boolean test(String str) {
        for (int i=0;i<10;i++) {
            if(str.contains(",")) {
                String[] split = str.split(",");
                for (int j=0;j<split.length;j++) {
                    if(split[j].startsWith("!")) {
                        String replaceStr = split[j].replace("!","");
                        System.out.println("split[j]=" + replaceStr);
                        if("com.kwad".matches(replaceStr)) {
                            return true;
                        }
                    }
                }
            }
        }
        System.out.println("还会走到这里来吗");
        return false;
    }



}
