package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : QuestionType.java
 * @date : 2021-10-08 오후 1:47
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
public enum QuestionType {

    JOIN("JOIN", "회원가입"),
    REQUEST("REQUEST", "저장신청"),
    OPEN("OPEN", "공개신청"),
    LEANING("LEANING", "학습데이터"),
    ENVIRONMENT("ENVIRONMENT", "학습환경"),
    ETC("ETC", "기타");

    private String name;
    private String desc;

    QuestionType(String name, String desc) {

        this.name = name;
        this.desc = desc;
    }

    public static QuestionType of(String name) {
        for (QuestionType obj : QuestionType.values()) {
            if (obj.name().equalsIgnoreCase(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, String>> codes(){

        List<Map<String, String>> codes = new ArrayList<>();

        for (QuestionType obj : QuestionType.values()) {
            Map<String, String> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("desc", obj.getDesc());

            codes.add(map);
        }

        return codes;
    }
}
