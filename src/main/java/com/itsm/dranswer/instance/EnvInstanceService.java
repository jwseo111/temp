package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : EnvInstanceService.java
 * @date : 2021-10-08 오후 1:50
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.apis.vpc.*;
import com.itsm.dranswer.apis.vpc.request.*;
import com.itsm.dranswer.apis.vpc.response.*;
import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.users.NCloudKeyDto;
import com.itsm.dranswer.users.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Transactional
@Service("envInstanceService")
public class EnvInstanceService {

    final private NCloudServerEnvRepo nCloudServerEnvRepo;

    final private NCloudServerEnvRepoSupport nCloudServerEnvRepoSupport;

    final private VpcCommonService vpcCommonService;

    final private UserService userService;

    final private VpcApiService vpcApiService;

    final private VpcServerService vpcServerService;

    final private VpcNetworkInterfaceService vpcNetworkInterfaceService;

    final private AcgService acgService;

    final private LoginKeyService loginKeyService;

    private final NCloudVpcLoginKeyRepo nCloudVpcLoginKeyRepo;

    public EnvInstanceService(NCloudServerEnvRepo nCloudServerEnvRepo, NCloudServerEnvRepoSupport nCloudServerEnvRepoSupport, VpcCommonService vpcCommonService, UserService userService, VpcApiService vpcApiService, VpcServerService vpcServerService, VpcNetworkInterfaceService vpcNetworkInterfaceService, AcgService acgService, LoginKeyService loginKeyService, NCloudVpcLoginKeyRepo nCloudVpcLoginKeyRepo) {
        this.nCloudServerEnvRepo = nCloudServerEnvRepo;
        this.nCloudServerEnvRepoSupport = nCloudServerEnvRepoSupport;
        this.vpcCommonService = vpcCommonService;
        this.userService = userService;
        this.vpcApiService = vpcApiService;
        this.vpcServerService = vpcServerService;
        this.vpcNetworkInterfaceService = vpcNetworkInterfaceService;
        this.acgService = acgService;
        this.loginKeyService = loginKeyService;
        this.nCloudVpcLoginKeyRepo = nCloudVpcLoginKeyRepo;
    }

    /**
     * 학습환경신청 목록 조회
     * @methodName : getEnvInstanceList
     * @date : 2021-10-08 오후 1:50
     * @author : xeroman.k
     * @param approveStatus
     * @param keyword
     * @param userSeq
     * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.instance.ServerEnvDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<ServerEnvDto> getEnvInstanceList(ApproveStatus approveStatus, String keyword, Long userSeq, Pageable pageable) {

        Page<ServerEnvDto> pages = null;
        pages = nCloudServerEnvRepoSupport.searchAll(approveStatus, keyword, userSeq, pageable);

        if(userSeq != null){

            List<ServerEnvDto> list = pages.getContent();
            list.forEach(envDto->{
                String serverInstanceNo = envDto.getServerInstanceNo();
                if(serverInstanceNo != null){
                    NCloudKeyDto nCloudKeyDto = userService.getNCloudKey(envDto.getReqUserSeq());
                    GetVpcServerDetailRequestDto requestDto = new GetVpcServerDetailRequestDto();
                    requestDto.setServerInstanceNo(serverInstanceNo);
                    GetVpcServerDetailResponseDto.ServerInstanceDto serverInstanceDto = vpcServerService.getServerInstanceDetail(requestDto, nCloudKeyDto);
                    if(serverInstanceDto != null){
                        String publicIp = serverInstanceDto.getPublicIp();
                        envDto.setPublicIp(publicIp);
                    }

                }
            });

            pages = new PageImpl<ServerEnvDto>(list, pages.getPageable(), pages.getTotalElements());
        }

        return pages;

    }

    /**
     * 학습환경신청 조회
     * @methodName : getNCloudServerEnv
     * @date : 2021-10-08 오후 1:50
     * @author : xeroman.k
     * @param reqSeq
     * @return : com.itsm.dranswer.instance.NCloudServerEnv
     * @throws
     * @modifyed :
     *
    **/
    public NCloudServerEnv getNCloudServerEnv(String reqSeq){
        return nCloudServerEnvRepo.findById(reqSeq).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정보 입니다."));
    }

    /**
     * 학습환경신청정보 상세 조회
     * @methodName : getEnvInstance
     * @date : 2021-10-08 오후 1:51
     * @author : xeroman.k
     * @param reqSeq
     * @return : com.itsm.dranswer.instance.NCloudServerEnvDto
     * @throws
     * @modifyed :
     *
    **/
    public NCloudServerEnvDto getEnvInstance(String reqSeq) {

        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);

        NCloudServerEnvDto nCloudServerEnvDto = nCloudServerEnv.convertDto();
        nCloudServerEnvDto.addChildren(nCloudServerEnv);

        GetServerProductListRequestDto requestDto = new GetServerProductListRequestDto();
        requestDto.setServerImageProductCode(nCloudServerEnv.getOsImageType().getProductCode());
        NCloudKeyDto nCloudKeyDto = userService.getNCloudKey(nCloudServerEnv.getReqUserSeq());

        GetServerProductListResponseDto getServerProductListResponseDto = vpcCommonService.getServerProductList(requestDto, nCloudKeyDto);
        nCloudServerEnvDto.setProduct(getServerProductListResponseDto);

        GetVpcServerDetailRequestDto getVpcServerDetailRequestDto = new GetVpcServerDetailRequestDto();
        getVpcServerDetailRequestDto.setServerInstanceNo(nCloudServerEnvDto.getServerInstanceNo());
        GetVpcServerDetailResponseDto.ServerInstanceDto serverInstanceDto = vpcServerService.getServerInstanceDetail(getVpcServerDetailRequestDto, nCloudKeyDto);
        if(serverInstanceDto != null){
            String publicIp = serverInstanceDto.getPublicIp();
            nCloudServerEnvDto.setPublicIp(publicIp);
        }

        return nCloudServerEnvDto;
    }

    /**
     * 학습환경신청
     * @methodName : reqCreateEnvironment
     * @date : 2021-10-08 오후 1:51
     * @author : xeroman.k
     * @param requestDto
     * @param loginUserInfo
     * @return : com.itsm.dranswer.instance.ServerEnvDto
     * @throws
     * @modifyed :
     *
    **/
    public NCloudServerEnvDto reqCreateEnvironment(NCloudServerEnvDto requestDto, LoginUserInfo loginUserInfo) {

        requestDto.setReqUserSeq(loginUserInfo.getUserSeq());

        NCloudServerEnv nCloudServerEnv = new NCloudServerEnv(requestDto);

        nCloudServerEnv = nCloudServerEnvRepo.save(nCloudServerEnv);

        return nCloudServerEnv.convertDto();
    }

    /**
     *
     * @methodName : makeServer
     * @date : 2021-10-20 오후 1:53
     * @author : xeroman.k
     * @param nCloudServerEnv
     * @return : com.itsm.dranswer.instance.NCloudServerEnv
     * @throws
     * @modifyed :
     *
    **/
    private NCloudServerEnv makeServer(NCloudServerEnv nCloudServerEnv){

        NCloudKeyDto nCloudKeyDto = userService.getNCloudKey(nCloudServerEnv.getReqUserSeq());

        GetVpcListRequestDto getVpcListRequestDto = new GetVpcListRequestDto();

        GetZoneListRequestDto getZoneListRequestDto = new GetZoneListRequestDto();
        GetZoneListResponseDto getZoneListResponseDto = vpcCommonService.getZoneList(getZoneListRequestDto, nCloudKeyDto);

        String zoneCode = getZoneListResponseDto.getGetZoneListResponse().getZoneList().get(0).getZoneCode();

        GetVpcListResponseDto getVpcListResponseDto = vpcApiService.getVpcList(getVpcListRequestDto, nCloudKeyDto);
        List<GetVpcListResponseDto.VpcDto> vpcDtoList = getVpcListResponseDto.getGetVpcListResponse().getVpcList();

        // get or make vpc
        String vpcNo = null;
        String subnet = null;

        if(vpcDtoList.isEmpty()){
            CreateVpcRequestDto createVpcRequestDto = new CreateVpcRequestDto();
            CreateVpcResponseDto.VpcInstanceDto vpcInstanceDto = vpcApiService.createVpc(createVpcRequestDto, nCloudKeyDto);
            vpcNo = vpcInstanceDto.getVpcNo();
            subnet = createVpcRequestDto.getIpv4CidrBlock();
        }else{
            GetVpcListResponseDto.VpcDto vpcInstanceDto = vpcDtoList.get(0);
            vpcNo = vpcInstanceDto.getVpcNo();
            subnet = vpcInstanceDto.getIpv4CidrBlock();
        }

        GetSubnetListRequestDto getSubnetListRequestDto = new GetSubnetListRequestDto();
        getSubnetListRequestDto.setVpcNo(vpcNo);
        GetSubnetListResponseDto getSubnetListResponseDto = vpcApiService.getSubnetList(getSubnetListRequestDto, nCloudKeyDto);

        List<GetSubnetListResponseDto.SubnetDto> subnetDtoList = getSubnetListResponseDto.getGetSubnetListResponse().getSubnetList();

        // get or make subnet
        String subnetNo = null;

        if(subnetDtoList.isEmpty()){
            CreateSubnetRequestDto createSubnetRequestDto = new CreateSubnetRequestDto();
            createSubnetRequestDto.setZoneCode(zoneCode);
            createSubnetRequestDto.setVpcNo(vpcNo);
            createSubnetRequestDto.setSubnet(subnet);

            CreateNetworkAclRequestDto createNetworkAclRequestDto = new CreateNetworkAclRequestDto();
            createNetworkAclRequestDto.setVpcNo(vpcNo);
            CreateNetworkAclResponseDto.NetworkAclInstanceDto networkAclInstanceDto = vpcNetworkInterfaceService.createNetworkAcl(createNetworkAclRequestDto, nCloudKeyDto);
            createSubnetRequestDto.setNetworkAclNo(networkAclInstanceDto.getNetworkAclNo());

            CreateSubnetResponseDto.SubnetInstanceDto subnetInstanceDto = vpcApiService.createSubnet(createSubnetRequestDto, nCloudKeyDto);
            subnetNo = subnetInstanceDto.getSubnetNo();

        }else{
            subnetNo = subnetDtoList.get(0).getSubnetNo();
        }

        // make acg
        CreateAccessControlGroupRequestDto createAccessControlGroupRequestDto = new CreateAccessControlGroupRequestDto();
        createAccessControlGroupRequestDto.setVpcNo(vpcNo);
        CreateAccessControlGroupResponseDto.AcgInstanceDto acgInstanceDto = acgService.createAccessControlGroup(createAccessControlGroupRequestDto, nCloudKeyDto);
        String acgNo = acgInstanceDto.getAccessControlGroupNo();

        // make login key
        CreateLoginKeyRequestDto createLoginKeyRequestDto = new CreateLoginKeyRequestDto();
        CreateLoginKeyResponseDto.CreateLoginKeyRawResponseDto createLoginKeyResponseDto =
                this.createLoginKeyAndSave(createLoginKeyRequestDto, nCloudKeyDto, nCloudServerEnv.getReqUserSeq());

        String loginKeyName = createLoginKeyResponseDto.getKeyName();
        String loginPrivateKey = createLoginKeyResponseDto.getPrivateKey();

        // make server request
        CreateVpcServerRequestDto createVpcServerRequestDto = new CreateVpcServerRequestDto(nCloudServerEnv, true);
        createVpcServerRequestDto.setVpcNo(vpcNo);
        createVpcServerRequestDto.setSubnetNo(subnetNo);
        createVpcServerRequestDto.setNI(acgNo);
        createVpcServerRequestDto.setLoginKeyName(loginKeyName);

        CreateVpcServerResponseDto.ServerInstanceDto serverInstanceDto = vpcServerService.createServerInstances(createVpcServerRequestDto, nCloudKeyDto);

        nCloudServerEnv.update(serverInstanceDto.getServerInstanceNo(), vpcNo, subnetNo, acgNo, loginKeyName, loginPrivateKey, serverInstanceDto.getServerName());

        return nCloudServerEnv;
    }

    public CreateLoginKeyResponseDto.CreateLoginKeyRawResponseDto createLoginKeyAndSave(CreateLoginKeyRequestDto requestDto, NCloudKeyDto nCloudKeyDto,
                                      Long userSeq){

        CreateLoginKeyResponseDto.CreateLoginKeyRawResponseDto responseDto = loginKeyService.createLoginKey(requestDto, nCloudKeyDto);

        NCloudVpcLoginKey nCloudVpcLoginKey = this.saveNCloudVpcLoginKey(responseDto.getKeyName(), userSeq, responseDto.getPrivateKey());

        return responseDto;

    }

    /**
     *
     * @methodName : approveEnvironment
     * @date : 2021-10-12 오후 2:30
     * @author : xeroman.k
     * @param reqSeq
     * @return : com.itsm.dranswer.instance.NCloudServerEnvDto
     * @throws
     * @modifyed :
     *
    **/
    public NCloudServerEnvDto approveEnvironment(String reqSeq) {

        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);
        nCloudServerEnv.accept();

        return nCloudServerEnv.convertDto();
    }

    /**
     *
     * @methodName : createEnvironment
     * @date : 2021-10-12 오후 2:31
     * @author : xeroman.k
     * @param reqSeq
     * @return : com.itsm.dranswer.apis.vpc.response.CreateVpcServerResponseDto.ServerInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    public CreateVpcServerResponseDto.ServerInstanceDto createEnvironment(String reqSeq) {

        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);

        CreateVpcServerRequestDto createVpcServerRequestDto = new CreateVpcServerRequestDto(nCloudServerEnv, false);
        NCloudKeyDto nCloudKeyDto = userService.getNCloudKey(nCloudServerEnv.getReqUserSeq());
        CreateVpcServerResponseDto.ServerInstanceDto serverInstanceDto = vpcServerService.createServerInstances(createVpcServerRequestDto, nCloudKeyDto);

        nCloudServerEnv.created(serverInstanceDto.getServerInstanceNo());
        return serverInstanceDto;
    }


    /**
     *
     * @methodName : createEnvironmentSimple
     * @date : 2021-10-20 오후 2:47
     * @author : xeroman.k
     * @param reqSeq
     * @return : com.itsm.dranswer.instance.NCloudServerEnv
     * @throws
     * @modifyed :
     *
    **/
    public NCloudServerEnvDto createEnvironmentSimple(String reqSeq) {

        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);

        nCloudServerEnv.checkApproved();

        return this.makeServer(nCloudServerEnv).convertDto();

    }

    /**
     *
     * @methodName : endEnvironment
     * @date : 2021-10-12 오후 2:31
     * @author : xeroman.k
     * @param reqSeq
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public NCloudServerEnvDto endEnvironment(String reqSeq){
        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);

        // 서버 정지
        this.stopEnvironment(reqSeq);

        nCloudServerEnv.end();

        // 서버 반납
        this.terminateEnvironment(reqSeq);

        return nCloudServerEnv.convertDto();
    }

    public void stopEnvironment(String reqSeq){
        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);
        NCloudKeyDto nCloudKeyDto = userService.getNCloudKey(nCloudServerEnv.getReqUserSeq());

        vpcServerService.stopServerInstances(
                new OperateVpcServersRequestDto(null, Arrays.asList(nCloudServerEnv.getServerInstanceNo())),
                nCloudKeyDto);
    }

    /**
     *
     * @methodName : `terminateEnvironment`
     * @date : 2021-10-12 오후 5:06
     * @author : xeroman.k
 * @param reqSeq
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void terminateEnvironment(String reqSeq){

        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);
        NCloudKeyDto nCloudKeyDto = userService.getNCloudKey(nCloudServerEnv.getReqUserSeq());

        GetVpcServerDetailRequestDto requestDto = new GetVpcServerDetailRequestDto(null, nCloudServerEnv.getServerInstanceNo());

        GetVpcServerDetailResponseDto.ServerInstanceDto serverInstanceDto = vpcServerService.getServerInstanceDetail(requestDto, nCloudKeyDto);

        // 서버 정지를 기다림
        while(!"NSTOP".equals(serverInstanceDto.getServerInstanceStatus().getCode())){
            serverInstanceDto = vpcServerService.getServerInstanceDetail(requestDto, nCloudKeyDto);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String publicIpInstanceNo = serverInstanceDto.getPublicIpInstanceNo();

        if(!"".equals(publicIpInstanceNo) ){

            // 공인IP 삭제
            OperateVpcPublicIpRequestDto vpcPublicIpRequestDto = new OperateVpcPublicIpRequestDto(null, publicIpInstanceNo, null);
            vpcServerService.deletePublicIpInstance(
                    vpcPublicIpRequestDto,
                    nCloudKeyDto
            );
        }

        // 서버 반납
        vpcServerService.terminateServerInstances(
                new OperateVpcServersRequestDto(null, Arrays.asList(nCloudServerEnv.getServerInstanceNo())),
                nCloudKeyDto
            );
    }

    /**
     *
     * @methodName : rejectEnvironment
     * @date : 2021-10-12 오후 2:31
     * @author : xeroman.k
     * @param reqSeq
     * @param requestDto
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public NCloudServerEnvDto rejectEnvironment(String reqSeq, NCloudServerEnvDto requestDto){
        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);
        nCloudServerEnv.reject(requestDto.getRejectReason());

        return nCloudServerEnv.convertDto();
    }

    /**
     *
     * @methodName : saveNCloudVpcLoginKey
     * @date : 2021-10-19 오후 2:24
     * @author : xeroman.k
     * @param keyName
     * @param userSeq
     * @param privateKey
     * @return : com.itsm.dranswer.instance.NCloudVpcLoginKey
     * @throws
     * @modifyed :
     *
    **/
    public NCloudVpcLoginKey saveNCloudVpcLoginKey(String keyName, Long userSeq, String privateKey){

        NCloudVpcLoginKey nCloudVpcLoginKey = new NCloudVpcLoginKey(keyName, userSeq, privateKey);
        nCloudVpcLoginKey = nCloudVpcLoginKeyRepo.save(nCloudVpcLoginKey);

        return nCloudVpcLoginKey;
    }

    /**
     *
     * @methodName : getRootPassword
     * @date : 2021-10-21 오후 5:26
     * @author : xeroman.k
     * @param reqSeq
     * @param loginUserInfo
     * @return : com.itsm.dranswer.apis.vpc.response.GetRootPasswordResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public GetRootPasswordResponseDto getRootPassword(String reqSeq, LoginUserInfo loginUserInfo) {

        NCloudServerEnv nCloudServerEnv = getNCloudServerEnvAndCheck(reqSeq, loginUserInfo);

        NCloudKeyDto nCloudKeyDto = userService.getNCloudKey(nCloudServerEnv.getReqUserSeq());
        GetRootPasswordRequestDto getRootPasswordRequestDto =
                new GetRootPasswordRequestDto(null, nCloudServerEnv.getServerInstanceNo(), nCloudServerEnv.getLoginPrivateKey());
        return vpcServerService.getRootPassword(getRootPasswordRequestDto, nCloudKeyDto);

    }

    /**
     *
     * @methodName : getPrivateKey
     * @date : 2021-10-21 오후 5:39
     * @author : xeroman.k
     * @param reqSeq
     * @param loginUserInfo
     * @return : java.lang.String
     * @throws
     * @modifyed :
     *
    **/
    public String getPrivateKey(String reqSeq, LoginUserInfo loginUserInfo) {

        NCloudServerEnv nCloudServerEnv = getNCloudServerEnvAndCheck(reqSeq, loginUserInfo);
        return nCloudServerEnv.getLoginPrivateKey();
    }

    /**
     *
     * @methodName : getNCloudServerEnvAndCheck
     * @date : 2021-10-26 오후 5:40
     * @author : xeroman.k
 * @param reqSeq
 * @param loginUserInfo
     * @return : com.itsm.dranswer.instance.NCloudServerEnv
     * @throws
     * @modifyed :
     *
    **/
    private NCloudServerEnv getNCloudServerEnvAndCheck(String reqSeq, LoginUserInfo loginUserInfo){
        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);
        if(!nCloudServerEnv.checkUser(loginUserInfo.getUserSeq())){
            throw new IllegalArgumentException("잘못된 접근 입니다.");
        }

        return nCloudServerEnv;
    }

    /**
     *
     * @methodName : reqCancelEnvironment
     * @date : 2021-10-28 오전 10:32
     * @author : xeroman.k
     * @param reqSeq
     * @return : com.itsm.dranswer.instance.NCloudServerEnvDto
     * @throws
     * @modifyed :
     *
    **/
    public NCloudServerEnvDto reqCancelEnvironment(String reqSeq) {

        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);
        nCloudServerEnv.cancel();

        return nCloudServerEnv.convertDto();
    }
}
