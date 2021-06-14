package com.itsm.dranswer.commons;

import lombok.Getter;

@Getter
public enum Disease {

    LIVER_CA    ("간암",        "LIVER CANCER"),
    LIVER_DIS   ("간질환",      "LIVER DISEASE"),
    THYROID_CA  ("갑상선암",    "THYROID CANCER"),
    HBP         ("고혈압",      "HYPERTENSION, HIGH BLOOD PRESSURE "),
    CEREBRAL_IFT("뇌경색",      "CEREBRAL INFARCTION"),
    DM          ("당뇨병",      "DIABETES MELLITUS"),
    MDD         ("우울증",      "MAJOR DEPRESSIVE DISORDER"),
    STOMACH_CA  ("위암",       "STOMACH CANCER"),
    BPH         ("전립선증식증", "BENIGN PROSTATIC HYPERTROPHY"),
    PN          ("폐렴",       "PNEUMONIA"),
    LUNG_CA     ("폐암",       "LUNG CANCER"),
    SKIN_DIS    ("피부질환",    "SKIN DISEASE"),
    ;

    private String desc;
    private String descEng;

    Disease(String desc, String descEng) {
        this.desc = desc;
        this.descEng = descEng;
    }

    public static Disease of(String name) {
        for (Disease obj : Disease.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }
}
