package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : NCloudServerEnvDto.java
 * @date : 2021-10-08 오후 1:53
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.apis.vpc.response.GetServerProductListResponseDto;
import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.storage.UseStorageInfoDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NCloudServerEnvDto extends BaseEntity {

    private String reqSeq;

    private Long reqUserSeq;

    private String serverInstanceNo;

    private String serverName;

    private String publicIp;

    private String vpcNo;

    private String subnetNo;

    private OsImageType osImageType;

    private ProductType productType;

    private String storageType;

    private String productCode;

    private GetServerProductListResponseDto product;

    private Boolean associateWithPublicIp = true;

    private String serverDescription;

    private ApproveStatus approveStatus;

    private String loginKeyName;

    private String rejectReason;

    private String cancelReason;

    private Integer usingDays;

    private List<UseStorageInfoDto> useStorageInfoList = new ArrayList<>();

    private List<NCloudNetworkInterfaceDto> networkInterfaceList = new ArrayList<>();

    public NCloudServerEnvDto(NCloudServerEnv source, Boolean addChildren) {
        copyProperties(source, this);

        if(addChildren){
            addChildren(source);
        }
    }

    public void addChildren(NCloudServerEnv nCloudServerEnv) {
        this.useStorageInfoList = nCloudServerEnv.getUseStorageInfoList().stream().map(e -> new UseStorageInfoDto(e)).collect(Collectors.toList());
        this.networkInterfaceList = nCloudServerEnv.getNetworkInterfaceList().stream().map(e -> new NCloudNetworkInterfaceDto(e)).collect(Collectors.toList());
    }
}
