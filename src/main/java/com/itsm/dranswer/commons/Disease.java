package com.itsm.dranswer.commons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@ToString
public enum Disease {

    LIVER_CA    ("LIVER_CA", "간암",        "LIVER CANCER"),
    LIVER_DIS   ("LIVER_DIS", "간질환",      "LIVER DISEASE"),
    THYROID_CA  ("THYROID_CA", "갑상선암",    "THYROID CANCER"),
    HBP         ("HBP", "고혈압",      "HYPERTENSION, HIGH BLOOD PRESSURE "),
    CEREBRAL_IFT("CEREBRAL_IFT", "뇌경색",      "CEREBRAL INFARCTION"),
    DM          ("DM", "당뇨병",      "DIABETES MELLITUS"),
    MDD         ("MDD", "우울증",      "MAJOR DEPRESSIVE DISORDER"),
    STOMACH_CA  ("STOMACH_CA", "위암",       "STOMACH CANCER"),
    BPH         ("BPH", "전립선증식증", "BENIGN PROSTATIC HYPERTROPHY"),
    PN          ("PN", "폐렴",       "PNEUMONIA"),
    LUNG_CA     ("LUNG_CA", "폐암",       "LUNG CANCER"),
    SKIN_DIS    ("SKIN_DIS", "피부질환",    "SKIN DISEASE"),
    ;

    private String name;
    private String desc;
    private String descEng;

    Disease(String name, String desc, String descEng) {
        this.name = name;
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

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (Disease obj : Disease.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("desc", obj.getDesc());
            map.put("descEng", obj.getDescEng());

            codes.add(map);
        }

        return codes;
    }

    @JsonCreator
    public static Disease fromJson(JsonNode node) {
        return of(node.asText())==null?of(node.get("name").asText()):of(node.asText());
    }

}
