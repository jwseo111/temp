package com.itsm.dranswer.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum JoinStat {
    REQUEST("REQUEST", "신청"),
    ACCEPT("ACCEPT", "승인");

    private String name;
    private String desc;

    JoinStat(String name, String desc) {

        this.name = name;
        this.desc = desc;

    }

    public static JoinStat of(String name) {
        for (JoinStat obj : JoinStat.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (JoinStat obj : JoinStat.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("desc", obj.getDesc());

            codes.add(map);
        }

        return codes;
    }
}
