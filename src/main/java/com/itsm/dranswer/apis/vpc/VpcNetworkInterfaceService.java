package com.itsm.dranswer.apis.vpc;

/*
 * @package : com.itsm.dranswer.apis.vpc
 * @name : VpcNetworkInterfaceService.java
 * @date : 2021-10-08 오전 10:24
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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

    /**
     *
     * @methodName : createNetworkInterface
     * @date : 2021-10-08 오전 10:27
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.CreateNetworkInterfaceResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public CreateNetworkInterfaceResponseDto createNetworkInterface(final CreateNetworkInterfaceRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_VPC_NETWORK_INTERFACE, requestDto);

        CreateNetworkInterfaceResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), CreateNetworkInterfaceResponseDto.class).getBody();

        responseDto.checkError();
        return responseDto;
    }

    /**
     * get vpc network interface list
     * @methodName : getNetworkInterfaceList
     * @date : 2021-10-08 오전 10:28
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetNetworkInterfaceListResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public GetNetworkInterfaceListResponseDto getNetworkInterfaceList(final GetNetworkInterfaceListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_NETWORK_INTERFACE_LIST, requestDto);

        GetNetworkInterfaceListResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetNetworkInterfaceListResponseDto.class).getBody();

        responseDto.checkError();
        return responseDto;
    }

    /**
     * get vpc network acl(access control list) list
     * @methodName : getNetworkAclList
     * @date : 2021-10-08 오전 10:28
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetNetworkAclListResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public GetNetworkAclListResponseDto getNetworkAclList(final GetNetworkAclListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_NETWORK_ACL_LIST, requestDto);

        GetNetworkAclListResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetNetworkAclListResponseDto.class).getBody();

        responseDto.checkError();
        return responseDto;
    }

    /**
     * create vpc network acl(access control list)
     * @methodName : createNetworkAcl
     * @date : 2021-10-08 오전 10:29
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.CreateNetworkAclResponseDto.NetworkAclInstanceDto
     * @throws
     * @modifyed :
     *
    **/
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

    /**
     * get vpc netwrok acl rule list
     * @methodName : getNetworkAclRuleList
     * @date : 2021-10-08 오전 10:29
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetNetworkAclRuleListResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public GetNetworkAclRuleListResponseDto getNetworkAclRuleList(final GetNetworkAclRuleListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_NETWORK_ACL_RULE_LIST, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                GetNetworkAclRuleListResponseDto.class).getBody();
    }

    /**
     * add vpc network acl inbound rule
     * @methodName : addNetworkAclInboundRule
     * @date : 2021-10-08 오전 10:29
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.AddNetworkAclInboundRuleResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public AddNetworkAclInboundRuleResponseDto addNetworkAclInboundRule(final AddNetworkAclRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_NETWORK_ACL_INBOUND_RULE, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                AddNetworkAclInboundRuleResponseDto.class).getBody();
    }

    /**
     * add vpc network acl outbound rule
     * @methodName : addNetworkAclOutboundRule
     * @date : 2021-10-08 오전 10:30
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.AddNetworkAclOutboundRuleResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public AddNetworkAclOutboundRuleResponseDto addNetworkAclOutboundRule(final AddNetworkAclRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_NETWORK_ACL_OUTBOUND_RULE, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                AddNetworkAclOutboundRuleResponseDto.class).getBody();
    }

    /**
     * remove vpc network acl inbound rule
     * @methodName : removeNetworkAclInboundRule
     * @date : 2021-10-08 오전 10:30
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.RemoveNetworkAclInboundRuleResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public RemoveNetworkAclInboundRuleResponseDto removeNetworkAclInboundRule(final RemoveNetworkAclRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_NETWORK_ACL_INBOUND_RULE, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                RemoveNetworkAclInboundRuleResponseDto.class).getBody();
    }

    /**
     * remove vpc network acl outbound rule
     * @methodName : removeNetworkAclOutboundRule
     * @date : 2021-10-08 오전 10:30
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.RemoveNetworkAclOutboundRuleResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public RemoveNetworkAclOutboundRuleResponseDto removeNetworkAclOutboundRule(final RemoveNetworkAclRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_NETWORK_ACL_OUTBOUND_RULE, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                RemoveNetworkAclOutboundRuleResponseDto.class).getBody();
    }

    /**
     * create vpc network acl & rule
     * @methodName : createNetworkAclAndAddRule
     * @date : 2021-10-08 오전 10:30
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.CreateNetworkAclResponseDto.NetworkAclInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    public CreateNetworkAclResponseDto.NetworkAclInstanceDto createNetworkAclAndAddRule(CreateNetworkAclAndAddRuleDto requestDto, NCloudKeyDto nCloudKeyDto) {

        CreateNetworkAclRequestDto createNetworkAclRequestDto = requestDto.getCreateNetworkAclRequestDto();
        AddNetworkAclRuleRequestDto addNetworkAclInboundRuleRequestDto = requestDto.getAddNetworkAclInboundRuleRequestDto();
        AddNetworkAclRuleRequestDto addNetworkAclOutboundRuleRequestDto = requestDto.getAddNetworkAclOutboundRuleRequestDto();

        CreateNetworkAclResponseDto.NetworkAclInstanceDto rtn = this.createNetworkAcl(createNetworkAclRequestDto, nCloudKeyDto);

        addNetworkAclInboundRuleRequestDto.setNetworkAclNo(rtn.getNetworkAclNo());
        addNetworkAclOutboundRuleRequestDto.setNetworkAclNo(rtn.getNetworkAclNo());

        AddNetworkAclInboundRuleResponseDto addNetworkAclInboundRuleResponseDto = this.addNetworkAclInboundRule(addNetworkAclInboundRuleRequestDto, nCloudKeyDto);
        AddNetworkAclOutboundRuleResponseDto addNetworkAclOutboundRuleResponseDto = this.addNetworkAclOutboundRule(addNetworkAclOutboundRuleRequestDto, nCloudKeyDto);

        if(addNetworkAclInboundRuleResponseDto.getResponseError() != null){
            rtn.setMakeInBoundError(true);
        }

        if(addNetworkAclOutboundRuleResponseDto.getResponseError() != null){
            rtn.setMakeInBoundError(true);
        }

        return rtn;
    }
}
