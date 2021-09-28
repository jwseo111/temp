package com.itsm.dranswer.instance;

import com.itsm.dranswer.apis.vpc.AcgService;
import com.itsm.dranswer.apis.vpc.VpcApiService;
import com.itsm.dranswer.apis.vpc.VpcCommonService;
import com.itsm.dranswer.apis.vpc.VpcNetworkInterfaceService;
import com.itsm.dranswer.apis.vpc.request.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class VpcInstanceRestCtrl {

    private final VpcCommonService vpcCommonService;

    private final VpcApiService vpcApiService;

    private final VpcNetworkInterfaceService vpcNetworkInterfaceService;

    private final AcgService acgService;

    private final EnvInstanceService envInstanceService;

    public VpcInstanceRestCtrl(VpcCommonService vpcCommonService, VpcApiService vpcApiService, VpcNetworkInterfaceService vpcNetworkInterfaceService, AcgService acgService, EnvInstanceService envInstanceService) {
        this.vpcCommonService = vpcCommonService;
        this.vpcApiService = vpcApiService;
        this.vpcNetworkInterfaceService = vpcNetworkInterfaceService;
        this.acgService = acgService;
        this.envInstanceService = envInstanceService;
    }

    @GetMapping(value = "/my/management/instance/vpc/server/getRegionList")
    public ApiResult<?> getRegionList(
    ){

        return success(vpcCommonService.getRegionList());
    }

    @GetMapping(value = "/my/management/instance/vpc/server/getZoneList")
    public ApiResult<?> getZoneList(GetZoneListRequestDto requestDto
                                      ){

        return success(vpcCommonService.getZoneList(requestDto));
    }

    @GetMapping(value = "/my/management/instance/vpc/server/getServerImageProductList")
    public ApiResult<?> getServerImageProductList(
            GetServerImageProductListRequestDto requestDto
    ){

        return success(vpcCommonService.getServerImageProductList(requestDto));
    }

    @GetMapping(value = "/my/management/instance/vpc/server/getServerProductList")
    public ApiResult<?> getServerProductList(
            GetServerProductListRequestDto requestDto
    ){

        return success(vpcCommonService.getServerProductList(requestDto));
    }

    /**
     * VPC 목록 조회
     * @methodName : getVpcList
     * @date : 2021-09-28 오후 2:42
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/my/management/instance/vpc/getVpcList")
    public ApiResult<?> getVpcList(
            GetVpcListRequestDto requestDto
    ){

        return success(vpcApiService.getVpcList(requestDto));
    }

    /**
     * VPC 생성
     * @methodName : createVpc
     * @date : 2021-09-28 오후 2:42
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/my/management/instance/vpc/createVpc")
    public ApiResult<?> createVpc(
            @RequestBody
                    CreateVpcRequestDto requestDto
    ){

        return success(vpcApiService.createVpc(requestDto));
    }

    /**
     * 서브넷 목록 조회
     * @methodName : getSubnetList
     * @date : 2021-09-28 오후 2:42
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/my/management/instance/vpc/getSubnetList")
    public ApiResult<?> getSubnetList(
            GetSubnetListRequestDto requestDto
    ){

        return success(vpcApiService.getSubnetList(requestDto));
    }

    /**
     * 서브넷 생성
     * @methodName : createSubnet
     * @date : 2021-09-28 오후 2:42
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/my/management/instance/vpc/createSubnet")
    public ApiResult<?> createSubnet(
            @RequestBody
                    CreateSubnetRequestDto requestDto
    ){

        return success(vpcApiService.createSubnet(requestDto));
    }

    /**
     * ACL 목록 조회
     * @methodName : getNetworkAclList
     * @date : 2021-09-28 오후 2:41
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/my/management/instance/vpc/getNetworkAclList")
    public ApiResult<?> getNetworkAclList(
            GetNetworkAclListRequestDto requestDto
    ){

        return success(vpcNetworkInterfaceService.getNetworkAclList(requestDto));
    }

    /**
     * ACL만 생성하기(팝업은 이 아래 함수 호출)
     * @methodName : createNetworkAcl
     * @date : 2021-09-28 오후 2:41
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/my/management/instance/vpc/createNetworkAcl")
    public ApiResult<?> createNetworkAcl(
            @RequestBody
                    CreateNetworkAclRequestDto requestDto
    ){

        return success(vpcNetworkInterfaceService.createNetworkAcl(requestDto));
    }

    /**
     * ACL 생성 및 룰 적용
     * @methodName : createNetworkAclAndAddRule
     * @date : 2021-09-28 오후 2:40
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/my/management/instance/vpc/createNetworkAclAndAddRule")
    public ApiResult<?> createNetworkAclAndAddRule(
            @RequestBody
                    CreateNetworkAclAndAddRuleDto requestDto
    ){

        return success(vpcNetworkInterfaceService.createNetworkAclAndAddRule(requestDto));
    }

    /**
     * Acg 목록 조회
     * @methodName : getAcgList
     * @date : 2021-09-28 오후 4:30
     * @author : xeroman.k 
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws 
     * @modifyed :
     *
    **/
    @GetMapping(value = "/my/management/instance/vpc/getAcgList")
    public ApiResult<?> getAcgList(
            GetAccessControlGroupListRequestDto requestDto
    ){

        return success(acgService.getAccessControlGroupList(requestDto));
    }
    
    /**
     * ACG 생성 및 룰 추가
     * @methodName : createAcgAndAddRule
     * @date : 2021-09-28 오후 4:31
     * @author : xeroman.k 
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws 
     * @modifyed :
     *
    **/
    @PostMapping(value = "/my/management/instance/vpc/createAcgAndAddRule")
    public ApiResult<?> createAcgAndAddRule(
            @RequestBody
                    CreateAcgAndAddRuleDto requestDto
    ){

        return success(acgService.createAcgAndAddRule(requestDto));
    }

    /**
     * Network Interface 목록 조회
     * @methodName : getNetworkInterfaceList
     * @date : 2021-09-28 오후 3:04
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/my/management/instance/vpc/getNetworkInterfaceList")
    public ApiResult<?> getNetworkInterfaceList(
            GetNetworkInterfaceListRequestDto requestDto
    ){

        return success(vpcNetworkInterfaceService.getNetworkInterfaceList(requestDto));
    }

    /**
     * Network Interface 생성
     * @methodName : createNetworkAcl
     * @date : 2021-09-28 오후 3:05
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/my/management/instance/vpc/createNetworkInterface")
    public ApiResult<?> createNetworkAcl(
            @RequestBody
                    CreateNetworkInterfaceRequestDto requestDto
    ){

        return success(vpcNetworkInterfaceService.createNetworkInterface(requestDto));
    }

    @GetMapping(value = "/env/instance/getList")
    public ApiResult<Page<ServerEnvDto>> getEnvList(
            @RequestParam(required = false) ApproveStatus approveStatus,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ){

        return success(envInstanceService.getEnvInstanceList(approveStatus, keyword, pageable));
    }

    @GetMapping(value = "/env/instance/getDetail")
    public ApiResult<?> getEnvDetail(
            Long reqSeq
    ){

        return success(envInstanceService.getEnvInstance(reqSeq));
    }

    @PostMapping(value = "/env/instance/vpc/reqCreateEnvironment")
    public ApiResult<?> reqCreateEnvironment(
            @RequestBody
                    NCloudServerEnvDto requestDto
    ){

        return success(vpcApiService.reqCreateEnvironment(requestDto));
    }
//
//    @GetMapping(value = "/my/management/instance/classic/server/getLoginKeyList")
//    public ApiResult<?> getLoginKeyList(
//            ReqGetLoginKeyList reqGetLoginKeyList
//    ){
//
//        return success(serverApiService.getLoginKeyList(reqGetLoginKeyList));
//    }
//
//    @GetMapping(value = "/my/management/instance/classic/server/createLoginKey")
//    public ApiResult<?> createLoginKey(
//            ReqCreateLoginKey reqCreateLoginKey
//    ){
//
//        return success(serverApiService.createLoginKey(reqCreateLoginKey));
//    }
//
//    @GetMapping(value = "/my/management/instance/classic/server/createPublicIpInstance")
//    public ApiResult<?> createPublicIpInstance(
//            ReqCreatePublicIpInstance reqCreatePublicIpInstance
//    ){
//
//        return success(serverApiService.createPublicIpInstance(reqCreatePublicIpInstance));
//    }
//
//    @GetMapping(value = "/my/management/instance/classic/server/associatePublicIpWithServerInstance")
//    public ApiResult<?> associatePublicIpWithServerInstance(
//            ReqAssociatePublicIpWithServerInstance reqAssociatePublicIpWithServerInstance
//    ){
//
//        return success(serverApiService.associatePublicIpWithServerInstance(reqAssociatePublicIpWithServerInstance));
//    }



}
