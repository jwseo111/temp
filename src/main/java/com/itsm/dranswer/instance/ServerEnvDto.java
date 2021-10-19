package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : ServerEnvDto.java
 * @date : 2021-10-08 오후 1:53
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.commons.BaseEntity;
import com.itsm.dranswer.users.UserInfo;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServerEnvDto extends BaseEntity {

    private String reqSeq;
    private String serverDescription;
    private OsImageType osImageType;
    private ProductType productType;
    private String storageType;
    private String userName;
    private ApproveStatus approveStatus;

    /**
     *
     * @methodName : ServerEnvDto
     * @date : 2021-09-23 오후 5:57
     * @author : xeroman.k
     * @param nCloudServerEnv
     * @param userInfo
     * @return :
     * @throws
     * @modifyed :
     * using for repo support
    **/
    public ServerEnvDto(NCloudServerEnv nCloudServerEnv, UserInfo userInfo){
        this.reqSeq = nCloudServerEnv.getReqSeq();
        this.serverDescription = nCloudServerEnv.getServerDescription();
        this.osImageType = nCloudServerEnv.getOsImageType();
        this.productType = nCloudServerEnv.getProductType();
        this.storageType = nCloudServerEnv.getStorageType();
        this.userName = userInfo.getUserName();
        this.approveStatus = nCloudServerEnv.getApproveStatus();
        this.createdDate = nCloudServerEnv.getCreatedDate();
    }
}
