package com.itsm.dranswer.storage;

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

}
