package com.itsm.dranswer.users;

import lombok.Getter;

@Getter
public enum JoinStat {
    REQUEST("신청"),
    ACCEPT("승인");

    private String desc;

    JoinStat(String desc) {

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
}
