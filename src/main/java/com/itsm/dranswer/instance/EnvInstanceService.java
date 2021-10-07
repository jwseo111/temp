package com.itsm.dranswer.instance;

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

    public Page<ServerEnvDto> getEnvInstanceList(ApproveStatus approveStatus, String keyword, Long userSeq, Pageable pageable) {

        return nCloudServerEnvRepoSupport.searchAll(approveStatus, keyword, userSeq, pageable);

    }

    public NCloudServerEnv getNCloudServerEnv(Long reqSeq){
        return nCloudServerEnvRepo.findById(reqSeq).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정보 입니다."));
    }

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

    public ServerEnvDto reqCreateEnvironment(NCloudServerEnvDto requestDto) {

        NCloudServerEnv nCloudServerEnv = new NCloudServerEnv(requestDto);

        return null;
    }
}
