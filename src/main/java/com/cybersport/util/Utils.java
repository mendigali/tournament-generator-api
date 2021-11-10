package com.cybersport.util;

public class Utils {
    public static boolean isPowerOfTwo(Integer number) {
        while (number % 2 == 0) {
            number = number / 2;
        }
        return number == 1;
    }

    public static Integer log2(Integer N) {
        int result = (int)(Math.log(N) / Math.log(2));
        return (Integer) result;
    }
}
