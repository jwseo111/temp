package com.itsm.dranswer.apis.vpc;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.*;
import com.itsm.dranswer.apis.vpc.response.*;
import com.itsm.dranswer.instance.CreateAcgAndAddRuleDto;
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

    public GetAccessControlGroupListResponseDto getAccessControlGroupList(GetAccessControlGroupListRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_ACG_LIST, requestDto);

        return restTemplate.exchange(openApiHost + uri, HttpMethod.GET,
                new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                GetAccessControlGroupListResponseDto.class).getBody();

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

    public GetAcgRuleListResponseDto getAcgRuleList(final GetAcgRuleListRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_ACG_RULE_LIST, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                GetAcgRuleListResponseDto.class).getBody();
    }

    public AddAcgInboundRuleResponseDto addAcgInboundRule(final AddAcgRuleRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_ACG_INBOUND_RULE, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                AddAcgInboundRuleResponseDto.class).getBody();
    }

    public AddAcgOutboundRuleResponseDto addAcgOutboundRule(final AddAcgRuleRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_ACG_OUTBOUND_RULE, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                AddAcgOutboundRuleResponseDto.class).getBody();
    }

    public RemoveAcgInboundRuleResponseDto removeAcgInboundRule(final RemoveAcgRuleRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_ACG_INBOUND_RULE, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                RemoveAcgInboundRuleResponseDto.class).getBody();
    }

    public RemoveAcgOutboundRuleResponseDto removeAcgOutboundRule(final RemoveAcgRuleRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_ACG_OUTBOUND_RULE, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                RemoveAcgOutboundRuleResponseDto.class).getBody();
    }

    public CreateAccessControlGroupResponseDto.AcgInstanceDto createAcgAndAddRule(CreateAcgAndAddRuleDto requestDto) {

        CreateAccessControlGroupRequestDto createAccessControlGroupRequestDto = requestDto.getCreateAccessControlGroupRequestDto();
        AddAcgRuleRequestDto addAcgInboundRuleRequestDto = requestDto.getAddAcgInboundRuleRequestDto();
        AddAcgRuleRequestDto addAcgOutboundRuleRequestDto = requestDto.getAddAcgOutboundRuleRequestDto();

        CreateAccessControlGroupResponseDto.AcgInstanceDto rtn = this.createAccessControlGroup(createAccessControlGroupRequestDto);

        addAcgInboundRuleRequestDto.setVpcNo(rtn.getVpcNo());
        addAcgInboundRuleRequestDto.setAccessControlGroupNo(rtn.getAccessControlGroupNo());
        addAcgOutboundRuleRequestDto.setVpcNo(rtn.getVpcNo());
        addAcgOutboundRuleRequestDto.setAccessControlGroupNo(rtn.getAccessControlGroupNo());

        this.addAcgInboundRule(addAcgInboundRuleRequestDto);
        this.addAcgOutboundRule(addAcgOutboundRuleRequestDto);

        return rtn;
    }
}
