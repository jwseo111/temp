package com.itsm.dranswer.apis.vpc;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.*;
import com.itsm.dranswer.apis.vpc.response.*;
import com.itsm.dranswer.instance.CreateNetworkAclAndAddRuleDto;
import com.itsm.dranswer.users.NCloudKeyDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class VpcNetworkInterfaceService extends ApiService {

    public CreateNetworkInterfaceResponseDto createNetworkInterface(final CreateNetworkInterfaceRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_VPC_NETWORK_INTERFACE, requestDto);

        CreateNetworkInterfaceResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), CreateNetworkInterfaceResponseDto.class).getBody();

        responseDto.checkError();
        return responseDto;
    }

    public GetNetworkInterfaceListResponseDto getNetworkInterfaceList(final GetNetworkInterfaceListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_NETWORK_INTERFACE_LIST, requestDto);

        GetNetworkInterfaceListResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetNetworkInterfaceListResponseDto.class).getBody();

        responseDto.checkError();
        return responseDto;
    }

    public GetNetworkAclListResponseDto getNetworkAclList(final GetNetworkAclListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_NETWORK_ACL_LIST, requestDto);

        GetNetworkAclListResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetNetworkAclListResponseDto.class).getBody();

        responseDto.checkError();
        return responseDto;
    }

    public CreateNetworkAclResponseDto.NetworkAclInstanceDto createNetworkAcl(final CreateNetworkAclRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_NETWORK_ACL, requestDto);

        CreateNetworkAclResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), CreateNetworkAclResponseDto.class).getBody();

        responseDto.checkError();

        if (ObjectUtils.isNotEmpty(responseDto.getCreateNetworkAclResponse())) {
            if (CollectionUtils.isEmpty(responseDto.getCreateNetworkAclResponse().getNetworkAclList())) {
                return responseDto.getCreateNetworkAclResponse().getNetworkAclList().get(0);
            }
        }

        return null;
    }

    public GetNetworkAclRuleListResponseDto getNetworkAclRuleList(final GetNetworkAclRuleListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_NETWORK_ACL_RULE_LIST, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                GetNetworkAclRuleListResponseDto.class).getBody();
    }

    public AddNetworkAclInboundRuleResponseDto addNetworkAclInboundRule(final AddNetworkAclRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_NETWORK_ACL_INBOUND_RULE, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                AddNetworkAclInboundRuleResponseDto.class).getBody();
    }

    public AddNetworkAclOutboundRuleResponseDto addNetworkAclOutboundRule(final AddNetworkAclRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_NETWORK_ACL_OUTBOUND_RULE, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                AddNetworkAclOutboundRuleResponseDto.class).getBody();
    }

    public RemoveNetworkAclInboundRuleResponseDto removeNetworkAclInboundRule(final RemoveNetworkAclRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_NETWORK_ACL_INBOUND_RULE, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                RemoveNetworkAclInboundRuleResponseDto.class).getBody();
    }

    public RemoveNetworkAclOutboundRuleResponseDto removeNetworkAclOutboundRule(final RemoveNetworkAclRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_NETWORK_ACL_OUTBOUND_RULE, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                RemoveNetworkAclOutboundRuleResponseDto.class).getBody();
    }

    public CreateNetworkAclResponseDto.NetworkAclInstanceDto createNetworkAclAndAddRule(CreateNetworkAclAndAddRuleDto requestDto, NCloudKeyDto nCloudKeyDto) {

        CreateNetworkAclRequestDto createNetworkAclRequestDto = requestDto.getCreateNetworkAclRequestDto();
        AddNetworkAclRuleRequestDto addNetworkAclInboundRuleRequestDto = requestDto.getAddNetworkAclInboundRuleRequestDto();
        AddNetworkAclRuleRequestDto addNetworkAclOutboundRuleRequestDto = requestDto.getAddNetworkAclOutboundRuleRequestDto();

        CreateNetworkAclResponseDto.NetworkAclInstanceDto rtn = this.createNetworkAcl(createNetworkAclRequestDto, nCloudKeyDto);

        addNetworkAclInboundRuleRequestDto.setNetworkAclNo(rtn.getNetworkAclNo());
        addNetworkAclOutboundRuleRequestDto.setNetworkAclNo(rtn.getNetworkAclNo());

        this.addNetworkAclInboundRule(addNetworkAclInboundRuleRequestDto, nCloudKeyDto);
        this.addNetworkAclOutboundRule(addNetworkAclOutboundRuleRequestDto, nCloudKeyDto);

        return rtn;
    }
}
