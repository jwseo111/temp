package com.itsm.dranswer.instance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("envInstanceService")
public class EnvInstanceService {

    final private NCloudServerEnvRepo nCloudServerEnvRepo;

    final private NCloudServerEnvRepoSupport nCloudServerEnvRepoSupport;

    public EnvInstanceService(NCloudServerEnvRepo nCloudServerEnvRepo, NCloudServerEnvRepoSupport nCloudServerEnvRepoSupport) {
        this.nCloudServerEnvRepo = nCloudServerEnvRepo;
        this.nCloudServerEnvRepoSupport = nCloudServerEnvRepoSupport;
    }

    public Page<ServerEnvDto> getEnvInstanceList(ApproveStatus approveStatus, String keyword, Pageable pageable) {

        return nCloudServerEnvRepoSupport.searchAll(approveStatus, keyword, pageable);

    }

    public NCloudServerEnvDto getEnvInstance(Long reqSeq) {
        NCloudServerEnv nCloudServerEnv = nCloudServerEnvRepo.findById(reqSeq).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정보 입니다."));

        return nCloudServerEnv.convertDto();
    }

    public ServerEnvDto reqCreateEnvironment(NCloudServerEnvDto requestDto) {

        return null;
    }
}
