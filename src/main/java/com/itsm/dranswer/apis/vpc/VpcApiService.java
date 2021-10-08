package com.itsm.dranswer.apis.vpc;

/*
 * @package : com.itsm.dranswer.apis.vpc
 * @name : VpcApiService.java
 * @date : 2021-10-08 오전 10:55
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.*;
import com.itsm.dranswer.apis.vpc.response.*;
import com.itsm.dranswer.users.NCloudKeyDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class VpcApiService extends ApiService {

    /**
     *
     * @methodName : createVpc
     * @date : 2021-10-08 오전 10:57
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.CreateVpcResponseDto.VpcInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    public CreateVpcResponseDto.VpcInstanceDto createVpc(final CreateVpcRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_VPC, requestDto);

        final ResponseEntity<CreateVpcResponseDto> response = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), CreateVpcResponseDto.class);

        final CreateVpcResponseDto responseBody = response.getBody();

        responseBody.checkError();

        if (ObjectUtils.isNotEmpty(responseBody.getCreateVpcResponse())) {
            if (!CollectionUtils.isEmpty(responseBody.getCreateVpcResponse().getVpcList())) {
                return responseBody.getCreateVpcResponse().getVpcList().get(0);
            }
        }

        return null;
    }

    /**
     *
     * @methodName : getVpcList
     * @date : 2021-10-08 오전 11:04
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetVpcListResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public GetVpcListResponseDto getVpcList(final GetVpcListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_LIST, requestDto);

        GetVpcListResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetVpcListResponseDto.class).getBody();

        responseDto.checkError();

        return responseDto;
    }

    /**
     *
     * @methodName : getVpcDetail
     * @date : 2021-10-08 오전 11:04
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetVpcDetailResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public GetVpcDetailResponseDto getVpcDetail(final GetVpcDetailRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_DETAIL, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetVpcDetailResponseDto.class).getBody();
    }

    /**
     *
     * @methodName : createSubnet
     * @date : 2021-10-08 오전 11:04
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.CreateSubnetResponseDto.SubnetInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    public CreateSubnetResponseDto.SubnetInstanceDto createSubnet(final CreateSubnetRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_VPC_SUBNET, requestDto);

        final ResponseEntity<CreateSubnetResponseDto> response = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), CreateSubnetResponseDto.class);

        final CreateSubnetResponseDto responseBody = response.getBody();

        responseBody.checkError();

        if (ObjectUtils.isNotEmpty(responseBody.getCreateSubnetResponse())) {
            if (!CollectionUtils.isEmpty(responseBody.getCreateSubnetResponse().getSubnetList())) {
                return responseBody.getCreateSubnetResponse().getSubnetList().get(0);
            }
        }

        return null;
    }

    /**
     *
     * @methodName : getSubnetList
     * @date : 2021-10-08 오전 11:04
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetSubnetListResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public GetSubnetListResponseDto getSubnetList(GetSubnetListRequestDto requestDto, NCloudKeyDto nCloudKeyDto){

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();


        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_SUBNET_LIST, requestDto);

        GetSubnetListResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)),
                GetSubnetListResponseDto.class).getBody();

        responseDto.checkError();
        return responseDto;

    }

    /**
     *
     * @methodName : getSubnetDetail
     * @date : 2021-10-08 오전 11:10
     * @author : xeroman.k
 * @param requestDto
 * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetSubnetDetailResponseDto.SubnetInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    public GetSubnetDetailResponseDto.SubnetInstanceDto getSubnetDetail(final GetSubnetDetailRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_SUBNET_DETAIL, requestDto);

        final ResponseEntity<GetSubnetDetailResponseDto> response = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetSubnetDetailResponseDto.class);

        final GetSubnetDetailResponseDto responseBody = response.getBody();

        if (ObjectUtils.isNotEmpty(responseBody.getGetSubnetDetailResponse())) {
            if (!CollectionUtils.isEmpty(responseBody.getGetSubnetDetailResponse().getSubnetList())) {
                return responseBody.getGetSubnetDetailResponse().getSubnetList().get(0);
            }
        }

        return null;
    }
}
