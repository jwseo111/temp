package com.itsm.dranswer.apis.vpc;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.CreateAccessControlGroupRequestDto;
import com.itsm.dranswer.apis.vpc.request.GetAccessControlGroupDetailRequestDto;
import com.itsm.dranswer.apis.vpc.response.CreateAccessControlGroupResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetAccessControlGroupDetailResponseDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class AcgService extends ApiService {

    private final String openApiHost = apiServerHost;

    @Value("${ncp.laif.access-key}")
    private String laifAccessKey;

    @Value("${ncp.laif.secret-key}")
    private String laifSecretKey;

    public CreateAccessControlGroupResponseDto.AcgInstanceDto createAccessControlGroup(CreateAccessControlGroupRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_ACG, requestDto);

        final CreateAccessControlGroupResponseDto responseDto = restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), CreateAccessControlGroupResponseDto.class).getBody();

        if (ObjectUtils.isNotEmpty(responseDto.getCreateAccessControlGroupResponse())) {
            if (CollectionUtils.isEmpty(responseDto.getCreateAccessControlGroupResponse().getAccessControlGroupList())) {
                return responseDto.getCreateAccessControlGroupResponse().getAccessControlGroupList().get(0);
            }
        }

        return null;
    }

    public GetAccessControlGroupDetailResponseDto.AcgInstanceDto getAccessControlGroupDetail(GetAccessControlGroupDetailRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_ACG_DETAIL, requestDto);

        final GetAccessControlGroupDetailResponseDto responseDto = restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetAccessControlGroupDetailResponseDto.class).getBody();

        if (ObjectUtils.isNotEmpty(responseDto.getGetAccessControlGroupDetailResponse())) {
            if (!CollectionUtils.isEmpty(responseDto.getGetAccessControlGroupDetailResponse().getAccessControlGroupList())) {
                return responseDto.getGetAccessControlGroupDetailResponse().getAccessControlGroupList().get(0);
            }
        }

        return null;
    }
}
