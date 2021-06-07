package com.itsm.dranswer.utils;

import org.apache.commons.lang3.math.NumberUtils;

public class NumberUtil extends NumberUtils{
    
    public static boolean isBig(int value, int max){

        return value > max;
    }
}