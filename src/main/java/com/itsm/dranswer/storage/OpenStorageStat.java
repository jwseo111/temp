package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : OpenStorageStat.java
 * @date : 2021-10-08 오후 2:14
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
public enum OpenStorageStat {

    O_REQ("O_REQ", "공개신청"),
    O_ACC("O_ACC", "신청승인"),
    O_REJ("O_REJ", "신청거절"),
    C_REQ("C_REQ", "취소신청"),
    C_ACC("C_ACC", "취소승인"),
    C_REJ("C_REJ", "취소거절");

    private String name;
    private String desc;

    OpenStorageStat(String name, String desc) {

        this.name = name;
        this.desc = desc;
    }

    public static OpenStorageStat of(String name) {
        for (OpenStorageStat obj : OpenStorageStat.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (OpenStorageStat obj : OpenStorageStat.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("desc", obj.getDesc());

            codes.add(map);
        }

        return codes;
    }

}
