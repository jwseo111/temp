package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : UseStorageStat.java
 * @date : 2021-10-08 오후 2:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum UseStorageStat {

    U_REQ("U_REQ", "사용신청"),
    U_CCL("U_CCL", "사용신청취소"),
    U_ACC("U_ACC", "사용신청승인"),
    U_REJ("U_REJ", "사용신청거절"),
    D_ACC("D_ACC", "삭제");

    private String name;
    private String desc;

    UseStorageStat(String name, String desc) {

        this.name = name;
        this.desc = desc;

    }

    public static UseStorageStat of(String name) {
        for (UseStorageStat obj : UseStorageStat.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (UseStorageStat obj : UseStorageStat.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("desc", obj.getDesc());

            codes.add(map);
        }

        return codes;
    }

    @JsonCreator
    public static UseStorageStat fromJson(@JsonProperty("name") String name) {
        return valueOf(name);
    }
}
