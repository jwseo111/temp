package com.itsm.dranswer.apis.vpc.request;

/*
 * @package : com.itsm.dranswer.apis.vpc.request
 * @name : CreateVpcServerRequestDto.java
 * @date : 2021-10-08 오후 1:07
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.itsm.dranswer.instance.NCloudNetworkInterface;
import com.itsm.dranswer.instance.NCloudServerEnv;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateVpcServerRequestDto {
    private String regionCode;
    private String vpcNo;
    private String subnetNo;
    private String serverName;
    private String serverProductCode;
    private String serverImageProductCode;
    private String memberServerImageInstanceNo;
    private Integer serverCreateCount;
    private String serverDescription;
    private String loginKeyName;
    private String feeSystemTypeCode = "MTRAT";
    private Boolean associateWithPublicIp;
    private List<NetworkInterface> networkInterfaceList;

    public CreateVpcServerRequestDto(NCloudServerEnv nCloudServerEnv) {
        this.vpcNo = nCloudServerEnv.getVpcNo();
        this.subnetNo = nCloudServerEnv.getSubnetNo();
        this.serverImageProductCode = nCloudServerEnv.getOsImageType().getProductCode();
        this.serverProductCode = nCloudServerEnv.getProductCode();
        this.serverDescription = nCloudServerEnv.getServerDescription();
        this.loginKeyName = nCloudServerEnv.getLoginKeyName();
        this.associateWithPublicIp = nCloudServerEnv.getAssociateWithPublicIp();
        this.networkInterfaceList = nCloudServerEnv.getNetworkInterfaceList().stream().map(e -> new NetworkInterface(e)).collect(Collectors.toList());
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NetworkInterface {
        private Integer networkInterfaceOrder;
        private String networkInterfaceNo;
        private List<String> accessControlGroupNoList;

        public NetworkInterface(NCloudNetworkInterface nCloudNetworkInterface){
            this.networkInterfaceOrder = nCloudNetworkInterface.getNetworkInterfaceOrder();
            this.networkInterfaceNo = nCloudNetworkInterface.getNetworkInterfaceNo();
        }
    }
}
