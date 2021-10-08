package com.itsm.dranswer.apis.vpc.request;

/*
 * @package : com.itsm.dranswer.apis.vpc.request
 * @name : GetServerImageProductListRequestDto.java
 * @date : 2021-10-08 오후 1:07
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetServerImageProductListRequestDto {
    private String regionCode = "KR";
    private String blockStorageSize;
    private String exclusionProductCode = "SW.VSVR.OS.LNX64.CNTOS.0703.B050";
    private String productCode;
    private List<String> platformTypeCodeList = Arrays.asList("LNX64", "WND64", "UBS64");


    // SW.VSVR.OS.LNX64.CNTOS.0708.B050 (CentOd 7.8)
    // SW.VSVR.OS.LNX64.UBNTU.SVR1604.B050 (Ubuntu-16.04-64-server)
    // SW.VSVR.OS.WND64.WND.SVR2016EN.B100 (Windows Server 2016 (64-bit) English Edition)
}
