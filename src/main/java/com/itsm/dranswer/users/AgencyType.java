package com.itsm.dranswer.users;

public enum AgencyType {

    HOSP("병원"),
    COMP("기업");

    private String desc;

    AgencyType(String desc) {

        this.desc = desc;

    }

    public static AgencyType of(String name) {
        for (AgencyType obj : AgencyType.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

}
