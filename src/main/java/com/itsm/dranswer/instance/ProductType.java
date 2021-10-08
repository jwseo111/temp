package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : ProductType.java
 * @date : 2021-10-08 오후 1:53
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ProductType {

    STAND("STAND", "Standard"),
    CPU("CPU", "CPU Intensive"),
    HIMEM("HIMEM", "High Memory"),
    HICPU("HICPU", "High CPU"),
    GPU("GPU", "GPU");

    private String name;
    private String desc;

    ProductType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static ProductType of(String name) {
        for (ProductType obj : ProductType.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (ProductType obj : ProductType.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("desc", obj.getDesc());

            codes.add(map);
        }

        return codes;
    }
}
