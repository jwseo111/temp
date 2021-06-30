package com.itsm.dranswer.users;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum IsYn {

    Y("Y", true),
    N("N", false);

    private String strValue;
    private boolean bValue;

    IsYn(String strValue, boolean bValue) {
        this.strValue = strValue;
        this.bValue = bValue;
    }

    public static IsYn of(String name) {
        for (IsYn obj : IsYn.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (IsYn obj : IsYn.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("value", obj.strValue);

            codes.add(map);
        }

        return codes;
    }
}
