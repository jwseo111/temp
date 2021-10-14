package com.itsm.dranswer.apis;

/*
 * @package : com.itsm.dranswer.apis
 * @name : ResponseError.java
 * @date : 2021-10-08 오후 1:22
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 * 모든 response 객체에 대해 error를 담는 prop 용도의 super class
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseError {
    protected Map responseError;
    protected Map error;

    public void checkError (){

        if(responseError != null){
            String msg = "["+responseError.get("returnCode")+"]"+responseError.get("returnMessage");
            throw new IllegalArgumentException(msg);
        }

        if(error != null){
            String msg = "["+error.get("message")+"]"+error.get("details");
            throw new IllegalArgumentException(msg);
        }
    }
}
