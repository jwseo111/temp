package com.itsm.dranswer.instance;

/*
 * @package : com.itsm.dranswer.instance
 * @name : EnvInstanceService.java
 * @date : 2021-10-08 오후 1:50
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.apis.vpc.VpcCommonService;
import com.itsm.dranswer.apis.vpc.VpcServerService;
import com.itsm.dranswer.apis.vpc.request.*;
import com.itsm.dranswer.apis.vpc.response.CreateVpcServerResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetServerProductListResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetVpcServerDetailResponseDto;
import com.itsm.dranswer.users.NCloudKeyDto;
import com.itsm.dranswer.users.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;


@Transactional
@Service("envInstanceService")
public class EnvInstanceService {

    final private NCloudServerEnvRepo nCloudServerEnvRepo;

    final private NCloudServerEnvRepoSupport nCloudServerEnvRepoSupport;

    final private VpcCommonService vpcCommonService;

    final private UserService userService;

    final private VpcServerService vpcServerService;

    public EnvInstanceService(NCloudServerEnvRepo nCloudServerEnvRepo, NCloudServerEnvRepoSupport nCloudServerEnvRepoSupport, VpcCommonService vpcCommonService, UserService userService, VpcServerService vpcServerService) {
        this.nCloudServerEnvRepo = nCloudServerEnvRepo;
        this.nCloudServerEnvRepoSupport = nCloudServerEnvRepoSupport;
        this.vpcCommonService = vpcCommonService;
        this.userService = userService;
        this.vpcServerService = vpcServerService;
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

        return nCloudServerEnvRepoSupport.searchAll(approveStatus, keyword, userSeq, pageable);

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
    public NCloudServerEnv getNCloudServerEnv(Long reqSeq){
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
    public NCloudServerEnvDto getEnvInstance(Long reqSeq) {

        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);

        NCloudServerEnvDto nCloudServerEnvDto = nCloudServerEnv.convertDto();
        if(nCloudServerEnv.getUseStorageId() != null){
            nCloudServerEnvDto.setUseStorageInfo(nCloudServerEnv.getUseStorageInfo().convertDto());
        }

        GetServerProductListRequestDto requestDto = new GetServerProductListRequestDto();
        requestDto.setServerImageProductCode(nCloudServerEnv.getOsImageType().getProductCode());
        NCloudKeyDto nCloudKeyDto = userService.getNCloudKey(nCloudServerEnv.getReqUserSeq());

        GetServerProductListResponseDto getServerProductListResponseDto = vpcCommonService.getServerProductList(requestDto, nCloudKeyDto);
        nCloudServerEnvDto.setProduct(getServerProductListResponseDto);

        return nCloudServerEnvDto;
    }

    /**
     * 학습환경신청
     * @methodName : reqCreateEnvironment
     * @date : 2021-10-08 오후 1:51
     * @author : xeroman.k
     * @param requestDto
     * @return : com.itsm.dranswer.instance.ServerEnvDto
     * @throws
     * @modifyed :
     *
    **/
    public NCloudServerEnvDto reqCreateEnvironment(NCloudServerEnvDto requestDto) {

        NCloudServerEnv nCloudServerEnv = new NCloudServerEnv(requestDto);

        nCloudServerEnv = nCloudServerEnvRepo.save(nCloudServerEnv);

        return nCloudServerEnv.convertDto();
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
    public NCloudServerEnvDto approveEnvironment(Long reqSeq) {

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
    public CreateVpcServerResponseDto.ServerInstanceDto createEnvironment(Long reqSeq) {

        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);

        CreateVpcServerRequestDto createVpcServerRequestDto = new CreateVpcServerRequestDto(nCloudServerEnv);
        NCloudKeyDto nCloudKeyDto = userService.getNCloudKey(nCloudServerEnv.getReqUserSeq());
        CreateVpcServerResponseDto.ServerInstanceDto serverInstanceDto = vpcServerService.createServerInstances(createVpcServerRequestDto, nCloudKeyDto);

        nCloudServerEnv.created(serverInstanceDto.getServerInstanceNo());
        return serverInstanceDto;
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
    public NCloudServerEnvDto endEnvironment(Long reqSeq){
        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);
        NCloudKeyDto nCloudKeyDto = userService.getNCloudKey(nCloudServerEnv.getReqUserSeq());

        vpcServerService.stopServerInstances(
                new OperateVpcServersRequestDto(null, Arrays.asList(nCloudServerEnv.getServerInstanceNo())),
                nCloudKeyDto);

        nCloudServerEnv.end();

        this.terminateEnvironment(reqSeq);

        return nCloudServerEnv.convertDto();
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
    public void terminateEnvironment(Long reqSeq){

        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);
        NCloudKeyDto nCloudKeyDto = userService.getNCloudKey(nCloudServerEnv.getReqUserSeq());

        GetVpcServerDetailRequestDto requestDto = new GetVpcServerDetailRequestDto(null, nCloudServerEnv.getServerInstanceNo());
        GetVpcServerDetailResponseDto.ServerInstanceDto serverInstanceDto = vpcServerService.getServerInstanceDetail(requestDto, nCloudKeyDto);

        String publicIpInstanceNo = serverInstanceDto.getPublicIpInstanceNo();

        OperateVpcPublicIpRequestDto vpcPublicIpRequestDto = new OperateVpcPublicIpRequestDto(null, publicIpInstanceNo, null);

        vpcServerService.disassociatePublicIpFromServerInstance(
                vpcPublicIpRequestDto,
                nCloudKeyDto
        );

        vpcServerService.deletePublicIpInstance(
                vpcPublicIpRequestDto,
                nCloudKeyDto
        );

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
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public NCloudServerEnvDto rejectEnvironment(Long reqSeq){
        NCloudServerEnv nCloudServerEnv = getNCloudServerEnv(reqSeq);
        nCloudServerEnv.reject();

        return nCloudServerEnv.convertDto();
    }
}
