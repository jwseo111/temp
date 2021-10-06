package com.itsm.dranswer.apis.vpc;

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

    public GetVpcListResponseDto getVpcList(final GetVpcListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_LIST, requestDto);

        GetVpcListResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetVpcListResponseDto.class).getBody();

        responseDto.checkError();

        return responseDto;
    }

    public GetVpcDetailResponseDto getVpcDetail(final GetVpcDetailRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_DETAIL, requestDto);

        return restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetVpcDetailResponseDto.class).getBody();
    }

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
