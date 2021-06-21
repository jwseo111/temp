package com.itsm.dranswer.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils{
    public static String random(int len){
        int random = 0;
        String randomStr = "";

        for(int i=0; i<len; i++){
            random = (int) (Math.random() * 10);
            randomStr += String.valueOf(random);
        }

        return randomStr;
    }
}