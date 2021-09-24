package com.itsm.dranswer.instance;

import com.itsm.dranswer.apis.vpc.VpcApiService;
import com.itsm.dranswer.apis.vpc.VpcCommonService;
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

    private final EnvInstanceService envInstanceService;

    public VpcInstanceRestCtrl(VpcCommonService vpcCommonService, VpcApiService vpcApiService, EnvInstanceService envInstanceService) {
        this.vpcCommonService = vpcCommonService;
        this.vpcApiService = vpcApiService;
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

    @GetMapping(value = "/my/management/instance/vpc/getVpcList")
    public ApiResult<?> getVpcList(
            GetVpcListRequestDto requestDto
    ){

        return success(vpcApiService.getVpcList(requestDto));
    }

    @PostMapping(value = "/my/management/instance/vpc/createVpc")
    public ApiResult<?> createVpc(
            @RequestBody
                    CreateVpcRequestDto requestDto
    ){

        return success(vpcApiService.createVpc(requestDto));
    }

    @GetMapping(value = "/my/management/instance/vpc/getSubnetList")
    public ApiResult<?> getSubnetList(
            GetSubnetListRequestDto requestDto
    ){

        return success(vpcApiService.getSubnetList(requestDto));
    }

    @PostMapping(value = "/my/management/instance/vpc/createSubnet")
    public ApiResult<?> createSubnet(
            @RequestBody
                    CreateSubnetRequestDto requestDto
    ){

        return success(vpcApiService.createSubnet(requestDto));
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
