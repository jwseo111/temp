package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : MyInstanceRestCtrl.java
 * @date : 2021-10-08 오후 1:52
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.apis.classic.request.*;
import com.itsm.dranswer.apis.classic.ServerApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class MyInstanceRestCtrl {

    private final ServerApiService serverApiService;

    @Autowired
    public MyInstanceRestCtrl(ServerApiService serverApiService) {
        this.serverApiService = serverApiService;
    }

    @GetMapping(value = "/my/management/instance/classic/server/getServerImageProductList")
    public ApiResult<?> getServerImageProductList(
            ReqGetServerImageProductList reqGetServerImageProductList
    ){

        return success(serverApiService.getServerImageProductList(reqGetServerImageProductList));
    }

    @GetMapping(value = "/my/management/instance/classic/server/getServerProductList")
    public ApiResult<?> getServerProductList(
            ReqGetServerProductList reqGetServerProductList
    ){

        return success(serverApiService.getServerProductList(reqGetServerProductList));
    }

    @GetMapping(value = "/my/management/instance/classic/server/createServerInstances")
    public ApiResult<?> createServerInstances(
            ReqCreateServerInstances reqCreateServerInstances
    ){

        return success(serverApiService.createServerInstances(reqCreateServerInstances));
    }

    @GetMapping(value = "/my/management/instance/classic/server/getLoginKeyList")
    public ApiResult<?> getLoginKeyList(
            ReqGetLoginKeyList reqGetLoginKeyList
    ){

        return success(serverApiService.getLoginKeyList(reqGetLoginKeyList));
    }

    @GetMapping(value = "/my/management/instance/classic/server/createLoginKey")
    public ApiResult<?> createLoginKey(
            ReqCreateLoginKey reqCreateLoginKey
    ){

        return success(serverApiService.createLoginKey(reqCreateLoginKey));
    }

    @GetMapping(value = "/my/management/instance/classic/server/createPublicIpInstance")
    public ApiResult<?> createPublicIpInstance(
            ReqCreatePublicIpInstance reqCreatePublicIpInstance
    ){

        return success(serverApiService.createPublicIpInstance(reqCreatePublicIpInstance));
    }

    @GetMapping(value = "/my/management/instance/classic/server/associatePublicIpWithServerInstance")
    public ApiResult<?> associatePublicIpWithServerInstance(
            ReqAssociatePublicIpWithServerInstance reqAssociatePublicIpWithServerInstance
    ){

        return success(serverApiService.associatePublicIpWithServerInstance(reqAssociatePublicIpWithServerInstance));
    }



}
