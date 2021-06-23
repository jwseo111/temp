package com.itsm.dranswer.users;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum AgencyType {

    HOSP("병원"),
    COMP("기업");

    private String desc;

    AgencyType(String desc) {

        this.desc = desc;

    }

    public static AgencyType of(String name) {
        for (AgencyType obj : AgencyType.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (AgencyType obj : AgencyType.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("desc", obj.getDesc());

            codes.add(map);
        }

        return codes;
    }
}
