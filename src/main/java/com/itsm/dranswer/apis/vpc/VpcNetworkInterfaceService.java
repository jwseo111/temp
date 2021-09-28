package com.itsm.dranswer.apis.vpc;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.*;
import com.itsm.dranswer.apis.vpc.response.*;
import com.itsm.dranswer.instance.CreateNetworkAclAndAddRuleDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class VpcNetworkInterfaceService extends ApiService {

    private final String openApiHost = apiServerHost;

    @Value("${ncp.laif.access-key}")
    private String laifAccessKey;

    @Value("${ncp.laif.secret-key}")
    private String laifSecretKey;

    public CreateNetworkInterfaceResponseDto createNetworkInterface(final CreateNetworkInterfaceRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_VPC_NETWORK_INTERFACE, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), CreateNetworkInterfaceResponseDto.class).getBody();
    }

    public GetNetworkInterfaceListResponseDto getNetworkInterfaceList(final GetNetworkInterfaceListRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_NETWORK_INTERFACE_LIST, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetNetworkInterfaceListResponseDto.class).getBody();
    }

    public GetNetworkAclListResponseDto getNetworkAclList(final GetNetworkAclListRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_NETWORK_ACL_LIST, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetNetworkAclListResponseDto.class).getBody();
    }

    public CreateNetworkAclResponseDto.NetworkAclInstanceDto createNetworkAcl(final CreateNetworkAclRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_NETWORK_ACL, requestDto);

        CreateNetworkAclResponseDto responseDto = restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), CreateNetworkAclResponseDto.class).getBody();

        if (ObjectUtils.isNotEmpty(responseDto.getCreateNetworkAclResponse())) {
            if (CollectionUtils.isEmpty(responseDto.getCreateNetworkAclResponse().getNetworkAclList())) {
                return responseDto.getCreateNetworkAclResponse().getNetworkAclList().get(0);
            }
        }

        return null;
    }

    public GetNetworkAclRuleListResponseDto getNetworkAclRuleList(final GetNetworkAclRuleListRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_NETWORK_ACL_RULE_LIST, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                GetNetworkAclRuleListResponseDto.class).getBody();
    }

    public AddNetworkAclInboundRuleResponseDto addNetworkAclInboundRule(final AddNetworkAclRuleRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_NETWORK_ACL_INBOUND_RULE, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                AddNetworkAclInboundRuleResponseDto.class).getBody();
    }

    public AddNetworkAclOutboundRuleResponseDto addNetworkAclOutboundRule(final AddNetworkAclRuleRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_NETWORK_ACL_OUTBOUND_RULE, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                AddNetworkAclOutboundRuleResponseDto.class).getBody();
    }

    public RemoveNetworkAclInboundRuleResponseDto removeNetworkAclInboundRule(final RemoveNetworkAclRuleRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_NETWORK_ACL_INBOUND_RULE, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                RemoveNetworkAclInboundRuleResponseDto.class).getBody();
    }

    public RemoveNetworkAclOutboundRuleResponseDto removeNetworkAclOutboundRule(final RemoveNetworkAclRuleRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_NETWORK_ACL_OUTBOUND_RULE, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                RemoveNetworkAclOutboundRuleResponseDto.class).getBody();
    }

    public CreateNetworkAclResponseDto.NetworkAclInstanceDto createNetworkAclAndAddRule(CreateNetworkAclAndAddRuleDto requestDto) {

        CreateNetworkAclRequestDto createNetworkAclRequestDto = requestDto.getCreateNetworkAclRequestDto();
        AddNetworkAclRuleRequestDto addNetworkAclInboundRuleRequestDto = requestDto.getAddNetworkAclInboundRuleRequestDto();
        AddNetworkAclRuleRequestDto addNetworkAclOutboundRuleRequestDto = requestDto.getAddNetworkAclOutboundRuleRequestDto();

        CreateNetworkAclResponseDto.NetworkAclInstanceDto rtn = this.createNetworkAcl(createNetworkAclRequestDto);

        addNetworkAclInboundRuleRequestDto.setNetworkAclNo(rtn.getNetworkAclNo());
        addNetworkAclOutboundRuleRequestDto.setNetworkAclNo(rtn.getNetworkAclNo());

        this.addNetworkAclInboundRule(addNetworkAclInboundRuleRequestDto);
        this.addNetworkAclOutboundRule(addNetworkAclOutboundRuleRequestDto);

        return rtn;
    }
}
