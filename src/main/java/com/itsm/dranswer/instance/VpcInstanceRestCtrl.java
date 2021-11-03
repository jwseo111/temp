package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : VpcInstanceRestCtrl.java
 * @date : 2021-10-08 오후 1:53
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.apis.vpc.*;
import com.itsm.dranswer.apis.vpc.request.*;
import com.itsm.dranswer.apis.vpc.response.GetRootPasswordResponseDto;
import com.itsm.dranswer.config.LoginUser;
import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.users.NCloudKeyDto;
import com.itsm.dranswer.users.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class VpcInstanceRestCtrl {

    private final LoginKeyService loginKeyService;

    private final VpcCommonService vpcCommonService;

    private final VpcApiService vpcApiService;

    private final VpcNetworkInterfaceService vpcNetworkInterfaceService;

    private final AcgService acgService;

    private final EnvInstanceService envInstanceService;

    private final UserService userService;

    public VpcInstanceRestCtrl(LoginKeyService loginKeyService, VpcCommonService vpcCommonService, VpcApiService vpcApiService, VpcNetworkInterfaceService vpcNetworkInterfaceService, AcgService acgService, EnvInstanceService envInstanceService, UserService userService) {
        this.loginKeyService = loginKeyService;
        this.vpcCommonService = vpcCommonService;
        this.vpcApiService = vpcApiService;
        this.vpcNetworkInterfaceService = vpcNetworkInterfaceService;
        this.acgService = acgService;
        this.envInstanceService = envInstanceService;
        this.userService = userService;
    }

    private NCloudKeyDto getNCloudKey(LoginUserInfo loginUserInfo){
        if(loginUserInfo==null) throw new IllegalArgumentException("접속정보가 존재하지 않습니다.");
        return userService.getNCloudKey(loginUserInfo.getUserSeq());
    }

    /**
     * 리전 목록 조회
     * @methodName : getRegionList
     * @date : 2021-10-08 오후 1:53
     * @author : xeroman.k
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/instance/vpc/server/getRegionList")
    public ApiResult<?> getRegionList(
            @LoginUser LoginUserInfo loginUserInfo
            ){

        return success(vpcCommonService.getRegionList(getNCloudKey(loginUserInfo)));
    }

    /**
     * 존 목록 조회
     * @methodName : getZoneList
     * @date : 2021-10-08 오후 1:53
     * @author : xeroman.k
     * @param requestDto
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/instance/vpc/server/getZoneList")
    public ApiResult<?> getZoneList(GetZoneListRequestDto requestDto
                                    , @LoginUser LoginUserInfo loginUserInfo
                                      ){

        return success(vpcCommonService.getZoneList(requestDto, getNCloudKey(loginUserInfo)));
    }

    /**
     * 서버OS이미지목록 조회
     * @methodName : getServerImageProductList
     * @date : 2021-10-08 오후 1:53
     * @author : xeroman.k
     * @param requestDto
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/instance/vpc/server/getServerImageProductList")
    public ApiResult<?> getServerImageProductList(
            GetServerImageProductListRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(vpcCommonService.getServerImageProductList(requestDto, getNCloudKey(loginUserInfo)));
    }

    /**
     * 서버 상품 조회
     * @methodName : getServerProductList
     * @date : 2021-10-08 오후 1:54
     * @author : xeroman.k
     * @param requestDto
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/instance/vpc/server/getServerProductList")
    public ApiResult<?> getServerProductList(
            GetServerProductListRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(vpcCommonService.getServerProductList(requestDto, getNCloudKey(loginUserInfo)));
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
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/instance/vpc/getVpcList")
    public ApiResult<?> getVpcList(
            GetVpcListRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(vpcApiService.getVpcList(requestDto, getNCloudKey(loginUserInfo)));
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
    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/my/management/instance/vpc/createVpc")
    public ApiResult<?> createVpc(
            @RequestBody
                    CreateVpcRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(vpcApiService.createVpc(requestDto, getNCloudKey(loginUserInfo)));
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
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/instance/vpc/getSubnetList")
    public ApiResult<?> getSubnetList(
            GetSubnetListRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(vpcApiService.getSubnetList(requestDto, getNCloudKey(loginUserInfo)));
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
    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/my/management/instance/vpc/createSubnet")
    public ApiResult<?> createSubnet(
            @RequestBody
                    CreateSubnetRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(vpcApiService.createSubnet(requestDto, getNCloudKey(loginUserInfo)));
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
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/instance/vpc/getNetworkAclList")
    public ApiResult<?> getNetworkAclList(
            GetNetworkAclListRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(vpcNetworkInterfaceService.getNetworkAclList(requestDto, getNCloudKey(loginUserInfo)));
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
    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/my/management/instance/vpc/createNetworkAcl")
    public ApiResult<?> createNetworkAcl(
            @RequestBody
                    CreateNetworkAclRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(vpcNetworkInterfaceService.createNetworkAcl(requestDto, getNCloudKey(loginUserInfo)));
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
    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/my/management/instance/vpc/createNetworkAclAndAddRule")
    public ApiResult<?> createNetworkAclAndAddRule(
            @RequestBody
                    CreateNetworkAclAndAddRuleDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(vpcNetworkInterfaceService.createNetworkAclAndAddRule(requestDto, getNCloudKey(loginUserInfo)));
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
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/instance/vpc/getAcgList")
    public ApiResult<?> getAcgList(
            GetAccessControlGroupListRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(acgService.getAccessControlGroupList(requestDto, getNCloudKey(loginUserInfo)));
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
    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/my/management/instance/vpc/createAcgAndAddRule")
    public ApiResult<?> createAcgAndAddRule(
            @RequestBody
                    CreateAcgAndAddRuleDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(acgService.createAcgAndAddRule(requestDto, getNCloudKey(loginUserInfo)));
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
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/instance/vpc/getNetworkInterfaceList")
    public ApiResult<?> getNetworkInterfaceList(
            GetNetworkInterfaceListRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(vpcNetworkInterfaceService.getNetworkInterfaceList(requestDto, getNCloudKey(loginUserInfo)));
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
    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/my/management/instance/vpc/createNetworkInterface")
    public ApiResult<?> createNetworkAcl(
            @RequestBody
                    CreateNetworkInterfaceRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(vpcNetworkInterfaceService.createNetworkInterface(requestDto, getNCloudKey(loginUserInfo)));
    }

    /**
     * 학습환경 신청 목록 조회
     * @methodName : getEnvList
     * @date : 2021-09-28 오후 4:31
     * @author : xeroman.k
     * @param approveStatus
     * @param keyword
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.instance.ServerEnvDto>>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/env/instance/getList")
    public ApiResult<Page<ServerEnvDto>> getEnvList(
            @RequestParam(required = false) ApproveStatus approveStatus,
            @RequestParam(required = false) String keyword,
            Pageable pageable
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(envInstanceService.getEnvInstanceList(approveStatus, keyword, null, pageable));
    }

    /**
     * MyPage 학습환경신청목록 조회
     * @methodName : getMyEnvList
     * @date : 2021-10-08 오후 1:54
     * @author : xeroman.k
     * @param approveStatus
     * @param keyword
     * @param pageable
     * @param loginUserInfo
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.instance.ServerEnvDto>>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/env/instance/getList")
    public ApiResult<Page<ServerEnvDto>> getMyEnvList(
            @RequestParam(required = false) ApproveStatus approveStatus,
            @RequestParam(required = false) String keyword,
            Pageable pageable
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(envInstanceService.getEnvInstanceList(approveStatus, keyword, loginUserInfo.getUserSeq(), pageable));
    }

    /**
     * 학습환경 신청 상세 조회
     * @methodName : getEnvDetail
     * @date : 2021-09-28 오후 4:31
     * @author : xeroman.k
     * @param reqSeq
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "/env/instance/getDetail")
    public ApiResult<?> getEnvDetail(
            String reqSeq
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(envInstanceService.getEnvInstance(reqSeq));
    }

    /**
     * 학습환경 신청
     * @methodName : reqCreateEnvironment
     * @date : 2021-09-28 오후 4:32
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/env/instance/vpc/reqCreateEnvironment")
    public ApiResult<?> reqCreateEnvironment(
            @RequestBody
                    NCloudServerEnvDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(envInstanceService.reqCreateEnvironment(requestDto, loginUserInfo));
    }

    /**
     * 로그인키 목록 조회
     * @methodName : getLoginKeyList
     * @date : 2021-09-28 오후 4:54
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/instance/server/getLoginKeyList")
    public ApiResult<?> getLoginKeyList(
            GetLoginKeyListRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){

        return success(loginKeyService.getLoginKeyList(requestDto, getNCloudKey(loginUserInfo)));
    }

    /**
     * 로그인키 생성
     * @methodName : createLoginKey
     * @date : 2021-09-28 오후 4:54
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<?>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_USER"})
    @GetMapping(value = "/my/management/instance/server/createLoginKey")
    public ApiResult<?> createLoginKey(
            CreateLoginKeyRequestDto requestDto
            , @LoginUser LoginUserInfo loginUserInfo
    ){


        return success(envInstanceService.createLoginKeyAndSave(requestDto, getNCloudKey(loginUserInfo), loginUserInfo.getUserSeq()));
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/management/instance/environment/getRootPassword/{reqSeq:.+(?<!\\.js)$}")
    public ApiResult<GetRootPasswordResponseDto> getRootPassword(
            @PathVariable String reqSeq,
            @LoginUser LoginUserInfo loginUserInfo) {

        return success(envInstanceService.getRootPassword(reqSeq, loginUserInfo));
    }

    @Secured(value = {"ROLE_USER"})
    @PostMapping(value = "/management/instance/environment/getPrivateKey/{reqSeq:.+(?<!\\.js)$}")
    public ApiResult<String> getPrivateKey(
            @PathVariable String reqSeq,
            @LoginUser LoginUserInfo loginUserInfo) {

        return success(envInstanceService.getPrivateKey(reqSeq, loginUserInfo));
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/instance/environment/approve/{reqSeq:.+(?<!\\.js)$}")
    public ApiResult<NCloudServerEnvDto> approveEnvironment(
            @PathVariable String reqSeq) {

        return success(envInstanceService.approveEnvironment(reqSeq));
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/instance/environment/create/{reqSeq:.+(?<!\\.js)$}")
    public ApiResult<NCloudServerEnvDto> createEnvironment(
            @PathVariable String reqSeq) {

//        return success(envInstanceService.createEnvironment(reqSeq));
        return success(envInstanceService.createEnvironmentSimple(reqSeq));
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/instance/environment/end/{reqSeq:.+(?<!\\.js)$}")
    public ApiResult<NCloudServerEnvDto> endEnvironment(
            @PathVariable String reqSeq) {

        return success(envInstanceService.endEnvironment(reqSeq));
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/instance/environment/reject/{reqSeq:.+(?<!\\.js)$}")
    public ApiResult<NCloudServerEnvDto> rejectEnvironment(
            @PathVariable String reqSeq,
            @RequestBody
                    NCloudServerEnvDto requestDto) {

        return success(envInstanceService.rejectEnvironment(reqSeq, requestDto));
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/management/instance/environment/cancel/{reqSeq:.+(?<!\\.js)$}")
    public ApiResult<NCloudServerEnvDto> reqCancelEnvironment(
            @PathVariable String reqSeq,
            @RequestBody
                    NCloudServerEnvDto requestDto) {

        return success(envInstanceService.reqCancelEnvironment(reqSeq, requestDto));
    }

}
