package com.itsm.dranswer.apis.vpc;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.*;
import com.itsm.dranswer.apis.vpc.response.*;
import com.itsm.dranswer.instance.CreateAcgAndAddRuleDto;
import com.itsm.dranswer.users.NCloudKeyDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class AcgService extends ApiService {

    public CreateAccessControlGroupResponseDto.AcgInstanceDto createAccessControlGroup(CreateAccessControlGroupRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_ACG, requestDto);

        final CreateAccessControlGroupResponseDto responseDto = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET,
                new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), CreateAccessControlGroupResponseDto.class).getBody();

        if (ObjectUtils.isNotEmpty(responseDto.getCreateAccessControlGroupResponse())) {
            if (CollectionUtils.isEmpty(responseDto.getCreateAccessControlGroupResponse().getAccessControlGroupList())) {
                return responseDto.getCreateAccessControlGroupResponse().getAccessControlGroupList().get(0);
            }
        }

        return null;
    }

    public GetAccessControlGroupListResponseDto getAccessControlGroupList(GetAccessControlGroupListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_ACG_LIST, requestDto);

        GetAccessControlGroupListResponseDto responseDto = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET,
                new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                GetAccessControlGroupListResponseDto.class).getBody();

        responseDto.checkError();
        return responseDto;
    }

    public GetAccessControlGroupDetailResponseDto.AcgInstanceDto getAccessControlGroupDetail(GetAccessControlGroupDetailRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_ACG_DETAIL, requestDto);

        final GetAccessControlGroupDetailResponseDto responseDto = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetAccessControlGroupDetailResponseDto.class).getBody();

        if (ObjectUtils.isNotEmpty(responseDto.getGetAccessControlGroupDetailResponse())) {
            if (!CollectionUtils.isEmpty(responseDto.getGetAccessControlGroupDetailResponse().getAccessControlGroupList())) {
                return responseDto.getGetAccessControlGroupDetailResponse().getAccessControlGroupList().get(0);
            }
        }

        return null;
    }

    public GetAcgRuleListResponseDto getAcgRuleList(final GetAcgRuleListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_ACG_RULE_LIST, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                GetAcgRuleListResponseDto.class).getBody();
    }

    public AddAcgInboundRuleResponseDto addAcgInboundRule(final AddAcgRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_ACG_INBOUND_RULE, requestDto);

        AddAcgInboundRuleResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                AddAcgInboundRuleResponseDto.class).getBody();
        responseDto.checkError();
        return responseDto;
    }

    public AddAcgOutboundRuleResponseDto addAcgOutboundRule(final AddAcgRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_ACG_OUTBOUND_RULE, requestDto);

        AddAcgOutboundRuleResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                AddAcgOutboundRuleResponseDto.class).getBody();
        responseDto.checkError();
        return responseDto;
    }

    public RemoveAcgInboundRuleResponseDto removeAcgInboundRule(final RemoveAcgRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_ACG_INBOUND_RULE, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                RemoveAcgInboundRuleResponseDto.class).getBody();
    }

    public RemoveAcgOutboundRuleResponseDto removeAcgOutboundRule(final RemoveAcgRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_ACG_OUTBOUND_RULE, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                RemoveAcgOutboundRuleResponseDto.class).getBody();
    }

    public CreateAccessControlGroupResponseDto.AcgInstanceDto createAcgAndAddRule(CreateAcgAndAddRuleDto requestDto, NCloudKeyDto nCloudKeyDto) {

        CreateAccessControlGroupRequestDto createAccessControlGroupRequestDto = requestDto.getCreateAccessControlGroupRequestDto();
        AddAcgRuleRequestDto addAcgInboundRuleRequestDto = requestDto.getAddAcgInboundRuleRequestDto();
        AddAcgRuleRequestDto addAcgOutboundRuleRequestDto = requestDto.getAddAcgOutboundRuleRequestDto();

        CreateAccessControlGroupResponseDto.AcgInstanceDto rtn = this.createAccessControlGroup(createAccessControlGroupRequestDto, nCloudKeyDto);

        addAcgInboundRuleRequestDto.setVpcNo(rtn.getVpcNo());
        addAcgInboundRuleRequestDto.setAccessControlGroupNo(rtn.getAccessControlGroupNo());
        addAcgOutboundRuleRequestDto.setVpcNo(rtn.getVpcNo());
        addAcgOutboundRuleRequestDto.setAccessControlGroupNo(rtn.getAccessControlGroupNo());

        this.addAcgInboundRule(addAcgInboundRuleRequestDto, nCloudKeyDto);
        this.addAcgOutboundRule(addAcgOutboundRuleRequestDto, nCloudKeyDto);

        return rtn;
    }
}
