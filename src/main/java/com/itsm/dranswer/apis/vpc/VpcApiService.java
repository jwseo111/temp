package com.itsm.dranswer.apis.vpc;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.ResponseError;
import com.itsm.dranswer.apis.vpc.request.*;
import com.itsm.dranswer.apis.vpc.response.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Service
public class VpcApiService extends ApiService {

    private final String openApiHost = apiServerHost;

    @Value("${ncp.laif.access-key}")
    private String laifAccessKey;

    @Value("${ncp.laif.secret-key}")
    private String laifSecretKey;

    public CreateVpcResponseDto.VpcInstanceDto createVpc(final CreateVpcRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_VPC, requestDto);

        final ResponseEntity<CreateVpcResponseDto> response = restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), CreateVpcResponseDto.class);

        final CreateVpcResponseDto responseBody = response.getBody();

        checkError(responseBody);

        if (ObjectUtils.isNotEmpty(responseBody.getCreateVpcResponse())) {
            if (!CollectionUtils.isEmpty(responseBody.getCreateVpcResponse().getVpcList())) {
                return responseBody.getCreateVpcResponse().getVpcList().get(0);
            }
        }

        return null;
    }

    public static void checkError (ResponseError responseError){

        if(responseError.getResponseError() != null){
            Map error = responseError.getResponseError();
            String msg = "["+error.get("returnCode")+"]"+error.get("returnMessage");
            throw new IllegalArgumentException(msg);
        }
    }

    public GetVpcListResponseDto getVpcList(final GetVpcListRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_LIST, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetVpcListResponseDto.class).getBody();
    }

    public GetVpcDetailResponseDto getVpcDetail(final GetVpcDetailRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_DETAIL, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetVpcDetailResponseDto.class).getBody();
    }

    public CreateSubnetResponseDto.SubnetInstanceDto createSubnet(final CreateSubnetRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_VPC_SUBNET, requestDto);

        final ResponseEntity<CreateSubnetResponseDto> response = restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), CreateSubnetResponseDto.class);

        final CreateSubnetResponseDto responseBody = response.getBody();

        if (ObjectUtils.isNotEmpty(responseBody.getCreateSubnetResponse())) {
            if (!CollectionUtils.isEmpty(responseBody.getCreateSubnetResponse().getSubnetList())) {
                return responseBody.getCreateSubnetResponse().getSubnetList().get(0);
            }
        }

        return null;
    }

    public GetSubnetListResponseDto getSubnetList(GetSubnetListRequestDto requestDto){

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_SUBNET_LIST, requestDto);

        return restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)),
                GetSubnetListResponseDto.class).getBody();

    }

    public GetSubnetDetailResponseDto.SubnetInstanceDto getSubnetDetail(final GetSubnetDetailRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_SUBNET_DETAIL, requestDto);

        final ResponseEntity<GetSubnetDetailResponseDto> response = restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetSubnetDetailResponseDto.class);

        final GetSubnetDetailResponseDto responseBody = response.getBody();

        if (ObjectUtils.isNotEmpty(responseBody.getGetSubnetDetailResponse())) {
            if (!CollectionUtils.isEmpty(responseBody.getGetSubnetDetailResponse().getSubnetList())) {
                return responseBody.getGetSubnetDetailResponse().getSubnetList().get(0);
            }
        }

        return null;
    }
}
