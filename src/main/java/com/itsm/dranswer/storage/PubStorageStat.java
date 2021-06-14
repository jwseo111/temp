package com.itsm.dranswer.storage;

public enum PubStorageStat {

    P_REQ("공개신청"),
    P_ACC("공개신청승인"),
    P_REJ("공개신청거절"),
    C_REQ("취소신청"),
    C_ACC("취소승인"),
    C_REJ("취소거절");

    private String desc;

    PubStorageStat(String desc) {
        this.desc = desc;
    }

    public static PubStorageStat of(String name) {
        for (PubStorageStat obj : PubStorageStat.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

}
