package com.itsm.dranswer.instance;

import com.itsm.dranswer.commons.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NCloudServerEnvDto extends BaseEntity {

    private Long reqSeq;

    private Long reqUserSeq;

    private OsImageType osImageType;

    private ProductType productType;

    private String storageType;

    private String productCode;

    private Boolean associateWithPublicIp;

    private String serverDescription;

    private ApproveStatus approveStatus;

    private String loginKeyName;

    private Integer usingDays;

    private List<NCloudNetworkInterfaceDto> networkInterfaceList = new ArrayList<>();

    public NCloudServerEnvDto(NCloudServerEnv source) {
        copyProperties(source, this);
    }
}
