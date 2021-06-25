package com.itsm.dranswer.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum AgencyType {


    MNHP("주관병원"),
    SBHP("참여병원"),
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
