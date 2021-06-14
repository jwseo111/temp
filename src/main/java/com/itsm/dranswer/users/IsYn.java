package com.itsm.dranswer.users;

import lombok.Getter;

@Getter
public enum IsYn {

    Y("Y", true),
        N("N", false);

    private String strValue;
    private boolean bValue;

    IsYn(String strValue, boolean bValue) {
        this.strValue = strValue;
        this.bValue = bValue;
    }

    public static IsYn of(String name) {
        for (IsYn obj : IsYn.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }
}
