package com.itsm.dranswer.storage;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum OpenStorageStat {

    O_REQ("공개신청"),
    O_ACC("공개신청승인"),
    O_REJ("공개신청거절"),
    C_REQ("취소신청"),
    C_ACC("취소승인"),
    C_REJ("취소거절");

    private String desc;

    OpenStorageStat(String desc) {
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
