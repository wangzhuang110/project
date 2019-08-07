package com.glch.spectrum.util;

import java.util.UUID;

public class IDUtil {

    //获取指定位数数字ID
    public static String getID(int len) {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();    //e3f6eec7-d9f4-4b78-b242-749851a2d922
        id = id.replace("-", "");
        int num = id.hashCode();        //取字符串哈希值
        num = num < 0 ? -num : num;
        id = String.valueOf(num);       //318887523
        //通过Math.random()获取[0.0, 1.0)的随机数，再乘以需要的位数。
        //用乘9，再加1，而没有用乘10的方式，是为了防止生成的随机数比较小，导致乘以位数后小于指定位数
        long random = (long) ((Math.random() * 9 + 1) * Math.pow(10, len - id.length() - 1));
        String rd = String.valueOf(random);
        return id + rd;
    }

    //获取UUID生成字符串的hashcode(8位、9位或者10位数字)
    public static String getHashcode() {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();    //e3f6eec7-d9f4-4b78-b242-749851a2d922
        id = id.replace("-", "");
        int num = id.hashCode();        //取字符串哈希值
        id = String.valueOf(num);       //318887523
        return id;
    }
}
