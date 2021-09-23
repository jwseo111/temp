package com.itsm.dranswer.instance;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum OsImageType {

    WND64("WND64", "SW.VSVR.OS.WND64.WND.SVR2016EN.B100", "Windows Server 2016 (64-bit) English Edition"),
    UBS64("UBS64", "SW.VSVR.OS.LNX64.UBNTU.SVR1604.B050", "Ubuntu Server 16.04 (64-bit)"),
    LNX64("LNX64", "SW.VSVR.OS.LNX64.CNTOS.0708.B050", "CentOS 7.8 (64-bit)");


    private String name;
    private String productCode;
    private String desc;

    OsImageType(String name, String productCode, String desc) {

        this.name = name;
        this.productCode = productCode;
        this.desc = desc;
    }

    public static OsImageType of(String name) {
        for (OsImageType obj : OsImageType.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (OsImageType obj : OsImageType.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("productCode", obj.getProductCode());
            map.put("desc", obj.getDesc());

            codes.add(map);
        }

        return codes;
    }
}
