package com.itsm.dranswer.apis.classic.request;

import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReqGetServerImageProductList {
    private String exclusionProductCode;
    private String productCode;
    private String blockStorageSize;
    private String regionNo = "1";
    private String infraResourceDetailTypeCode;
    private List<String> platformTypeCodeList = Arrays.asList("LNX64","WND64","UBS64");
}
