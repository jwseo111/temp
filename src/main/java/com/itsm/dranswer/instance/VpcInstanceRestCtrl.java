package com.itsm.dranswer.instance;

import com.itsm.dranswer.apis.vpc.VpcApiService;
import com.itsm.dranswer.apis.vpc.VpcCommonService;
import com.itsm.dranswer.apis.vpc.request.GetServerImageProductListRequestDto;
import com.itsm.dranswer.apis.vpc.request.GetServerProductListRequestDto;
import com.itsm.dranswer.apis.vpc.request.GetVpcListRequestDto;
import com.itsm.dranswer.apis.vpc.request.GetZoneListRequestDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class VpcInstanceRestCtrl {

    private final VpcCommonService vpcCommonService;

    private final VpcApiService vpcApiService;

    public VpcInstanceRestCtrl(VpcCommonService vpcCommonService, VpcApiService vpcApiService) {
        this.vpcCommonService = vpcCommonService;
        this.vpcApiService = vpcApiService;
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

    @GetMapping(value = "/my/management/instance/vpc/vpc/getVpcList")
    public ApiResult<?> createServerInstances(
            GetVpcListRequestDto requestDto
    ){

        return success(vpcApiService.getVpcList(requestDto));
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
