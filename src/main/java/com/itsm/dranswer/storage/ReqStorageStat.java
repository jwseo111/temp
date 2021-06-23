package com.itsm.dranswer.storage;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum ReqStorageStat {

    S_REQ("저장신청"),
    S_CCL("저장신청취소"),
    S_ACC("저장신청승인"),
    S_REJ("저장신청거절"),
    D_REQ("삭제신청"),
    D_ACC("삭제신청승인"),
    D_REJ("삭제신청거절");

    private String desc;

    ReqStorageStat(String desc) {

        this.desc = desc;

    }

    public static ReqStorageStat of(String name) {
        for (ReqStorageStat obj : ReqStorageStat.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (ReqStorageStat obj : ReqStorageStat.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("desc", obj.getDesc());

            codes.add(map);
        }

        return codes;
    }

}
