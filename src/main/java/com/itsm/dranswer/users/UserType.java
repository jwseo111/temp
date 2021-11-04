package com.itsm.dranswer.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum UserType {

    ADMIN("ADMIN", "관리자"),
    HOSP("HOSP", "병원"),
    COMP("COMP", "기업");

    private String name;
    private String desc;

    UserType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static UserType of(String name) {
        for (UserType obj : UserType.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (UserType obj : UserType.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("desc", obj.getDesc());

            codes.add(map);
        }

        return codes;
    }
}
