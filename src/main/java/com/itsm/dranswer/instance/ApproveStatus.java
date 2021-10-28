package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : ApproveStatus.java
 * @date : 2021-10-08 오후 1:49
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
public enum ApproveStatus {

    REQUEST("REQUEST", "요청"),
    ACCEPT("ACCEPT", "승인"),
    CREATED("CREATED", "생성됨"),
    END("END", "종료"),
    REJECT("REJECT", "거절"),
    CANCEL("CANCEL", "취소");

    private String name;
    private String desc;

    ApproveStatus(String name, String desc) {

        this.name = name;
        this.desc = desc;
    }

    public static ApproveStatus of(String name) {
        for (ApproveStatus obj : ApproveStatus.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (ApproveStatus obj : ApproveStatus.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("desc", obj.getDesc());

            codes.add(map);
        }

        return codes;
    }
}
