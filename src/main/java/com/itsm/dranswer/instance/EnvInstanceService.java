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
import com.itsm.dranswer.apis.vpc.request.GetServerProductListRequestDto;
import com.itsm.dranswer.apis.vpc.response.GetServerProductListResponseDto;
import com.itsm.dranswer.users.NCloudKeyDto;
import com.itsm.dranswer.users.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("envInstanceService")
public class EnvInstanceService {

    final private NCloudServerEnvRepo nCloudServerEnvRepo;

    final private NCloudServerEnvRepoSupport nCloudServerEnvRepoSupport;

    final private VpcCommonService vpcCommonService;

    final private UserService userService;

    public EnvInstanceService(NCloudServerEnvRepo nCloudServerEnvRepo, NCloudServerEnvRepoSupport nCloudServerEnvRepoSupport, VpcCommonService vpcCommonService, UserService userService) {
        this.nCloudServerEnvRepo = nCloudServerEnvRepo;
        this.nCloudServerEnvRepoSupport = nCloudServerEnvRepoSupport;
        this.vpcCommonService = vpcCommonService;
        this.userService = userService;
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
}
