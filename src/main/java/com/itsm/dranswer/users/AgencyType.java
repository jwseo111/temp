package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : AgencyType.java
 * @date : 2021-10-08 오후 2:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum AgencyType {
    MNHP("MNHP", "질병주관병원"),
    SBHP("SBHP", "질병참여병원"),
    COMP("COMP", "기업");

    private String desc;
    private String name;

    AgencyType(String name, String desc) {

        this.name = name;
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
