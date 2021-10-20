package com.itsm.dranswer.apis.vpc;

/*
 * @package : com.itsm.dranswer.apis.vpc
 * @name : AcgService.java
 * @date : 2021-10-08 오전 11:07
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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

    /**
     * create vpc acg
     * @methodName : createAccessControlGroup
     * @date : 2021-10-08 오전 11:08
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.CreateAccessControlGroupResponseDto.AcgInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    public CreateAccessControlGroupResponseDto.AcgInstanceDto createAccessControlGroup(CreateAccessControlGroupRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_ACG, requestDto);

        final CreateAccessControlGroupResponseDto responseDto = restTemplate.exchange(
                OpenApiUtils.getOpenApiURI(apiServerHost, uri),
                HttpMethod.GET,
                new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), CreateAccessControlGroupResponseDto.class).getBody();

        if (ObjectUtils.isNotEmpty(responseDto.getCreateAccessControlGroupResponse())) {
            if (!CollectionUtils.isEmpty(responseDto.getCreateAccessControlGroupResponse().getAccessControlGroupList())) {
                return responseDto.getCreateAccessControlGroupResponse().getAccessControlGroupList().get(0);
            }
        }

        return null;
    }

    /**
     * get vpc acg list
     * @methodName : getAccessControlGroupList
     * @date : 2021-10-08 오전 11:08
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetAccessControlGroupListResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public GetAccessControlGroupListResponseDto getAccessControlGroupList(GetAccessControlGroupListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_ACG_LIST, requestDto);

        GetAccessControlGroupListResponseDto responseDto = restTemplate.exchange(
                OpenApiUtils.getOpenApiURI(apiServerHost, uri),
                HttpMethod.GET,
                new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                GetAccessControlGroupListResponseDto.class).getBody();

        responseDto.checkError();
        return responseDto;
    }

    /**
     * get vpc acg detail
     * @methodName : getAccessControlGroupDetail
     * @date : 2021-10-08 오전 11:08
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetAccessControlGroupDetailResponseDto.AcgInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    public GetAccessControlGroupDetailResponseDto.AcgInstanceDto getAccessControlGroupDetail(GetAccessControlGroupDetailRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_ACG_DETAIL, requestDto);

        final GetAccessControlGroupDetailResponseDto responseDto = restTemplate.exchange(
                OpenApiUtils.getOpenApiURI(apiServerHost, uri),
                HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetAccessControlGroupDetailResponseDto.class).getBody();

        if (ObjectUtils.isNotEmpty(responseDto.getGetAccessControlGroupDetailResponse())) {
            if (!CollectionUtils.isEmpty(responseDto.getGetAccessControlGroupDetailResponse().getAccessControlGroupList())) {
                return responseDto.getGetAccessControlGroupDetailResponse().getAccessControlGroupList().get(0);
            }
        }

        return null;
    }

    /**
     * get vpc acl rule list
     * @methodName : getAcgRuleList
     * @date : 2021-10-08 오전 11:08
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetAcgRuleListResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public GetAcgRuleListResponseDto getAcgRuleList(final GetAcgRuleListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_ACG_RULE_LIST, requestDto);

        return restTemplate.exchange(
                OpenApiUtils.getOpenApiURI(apiServerHost, uri),
                HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                GetAcgRuleListResponseDto.class).getBody();
    }

    /**
     * add vpc acg inbound rule
     * @methodName : addAcgInboundRule
     * @date : 2021-10-08 오전 11:09
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.AddAcgInboundRuleResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public AddAcgInboundRuleResponseDto addAcgInboundRule(final AddAcgRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_ACG_INBOUND_RULE, requestDto);

        AddAcgInboundRuleResponseDto responseDto = restTemplate.exchange(
                OpenApiUtils.getOpenApiURI(apiServerHost, uri),
                HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                AddAcgInboundRuleResponseDto.class).getBody();
        responseDto.checkError();
        return responseDto;
    }

    /**
     * add vpc acg outbound rule
     * @methodName : addAcgOutboundRule
     * @date : 2021-10-08 오전 11:09
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.AddAcgOutboundRuleResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public AddAcgOutboundRuleResponseDto addAcgOutboundRule(final AddAcgRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.ADD_ACG_OUTBOUND_RULE, requestDto);

        AddAcgOutboundRuleResponseDto responseDto = restTemplate.exchange(
                OpenApiUtils.getOpenApiURI(apiServerHost, uri),
                HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                AddAcgOutboundRuleResponseDto.class).getBody();
        responseDto.checkError();
        return responseDto;
    }

    /**
     * remove vpc acg inbound rule
     * @methodName : removeAcgInboundRule
     * @date : 2021-10-08 오전 11:09
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.RemoveAcgInboundRuleResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public RemoveAcgInboundRuleResponseDto removeAcgInboundRule(final RemoveAcgRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_ACG_INBOUND_RULE, requestDto);

        return restTemplate.exchange(
                OpenApiUtils.getOpenApiURI(apiServerHost, uri),
                HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                RemoveAcgInboundRuleResponseDto.class).getBody();
    }

    /**
     * remove vpc acg inbound rule
     * @methodName : removeAcgOutboundRule
     * @date : 2021-10-08 오전 11:09
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.RemoveAcgOutboundRuleResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public RemoveAcgOutboundRuleResponseDto removeAcgOutboundRule(final RemoveAcgRuleRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REMOVE_ACG_OUTBOUND_RULE, requestDto);

        return restTemplate.exchange(
                OpenApiUtils.getOpenApiURI(apiServerHost, uri),
                HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                RemoveAcgOutboundRuleResponseDto.class).getBody();
    }

    /**
     * create vpc acg & rule
     * @methodName : createAcgAndAddRule
     * @date : 2021-10-08 오전 11:09
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.CreateAccessControlGroupResponseDto.AcgInstanceDto
     * @throws
     * @modifyed :
     *
    **/
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
