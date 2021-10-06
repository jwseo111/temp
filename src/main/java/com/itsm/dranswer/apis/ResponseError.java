package com.itsm.dranswer.apis;

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

    public void checkError (){

        if(responseError != null){
            String msg = "["+responseError.get("returnCode")+"]"+responseError.get("returnMessage");
            throw new IllegalArgumentException(msg);
        }
    }
}
