package com.itsm.dranswer.users;

/*
 * @package : com.itsm.dranswer.users
 * @name : JoinStat.java
 * @date : 2021-10-08 오후 2:47
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
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

    @JsonCreator
    public static JoinStat fromJson(JsonNode node) {
        return of(node.asText())==null?of(node.get("name").asText()):of(node.asText());
    }
}
