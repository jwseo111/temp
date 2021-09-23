package com.itsm.dranswer.apis.vpc;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.CreateVpcServerRequestDto;
import com.itsm.dranswer.apis.vpc.request.GetVpcServerDetailRequestDto;
import com.itsm.dranswer.apis.vpc.request.GetVpcServerListRequestDto;
import com.itsm.dranswer.apis.vpc.request.OperateVpcServersRequestDto;
import com.itsm.dranswer.apis.vpc.response.CreateVpcServerResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetVpcServerDetailResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetVpcServerListResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class VpcServerService extends ApiService {

    private final String openApiHost = apiServerHost;

    @Value("${ncp.laif.access-key}")
    private String laifAccessKey;

    @Value("${ncp.laif.secret-key}")
    private String laifSecretKey;

    public GetVpcServerListResponse getServerInstanceList(final GetVpcServerListRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_SERVER_INSTANCE_LIST, requestDto);

        return restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetVpcServerListResponse.class).getBody();
    }

    public GetVpcServerDetailResponseDto.ServerInstanceDto getServerInstanceDetail(final GetVpcServerDetailRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_SERVER_INSTANCE_DETAIL, requestDto);

        final ResponseEntity<GetVpcServerDetailResponseDto> response = restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetVpcServerDetailResponseDto.class);

        final GetVpcServerDetailResponseDto responseBody = response.getBody();

        if (ObjectUtils.isNotEmpty(responseBody.getGetServerInstanceDetailResponse())) {
            if (!CollectionUtils.isEmpty(responseBody.getGetServerInstanceDetailResponse().getServerInstanceList())) {
                return responseBody.getGetServerInstanceDetailResponse().getServerInstanceList().get(0);
            }
        }

        return null;
    }

    public CreateVpcServerResponseDto.ServerInstanceDto createServerInstances(CreateVpcServerRequestDto requestDto) {

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_VPC_SERVER_INSTANCES, requestDto);

        final ResponseEntity<CreateVpcServerResponseDto> responseEntity = restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), CreateVpcServerResponseDto.class);

        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            if(ObjectUtils.isNotEmpty(responseEntity.getBody()) && ObjectUtils.isNotEmpty(responseEntity.getBody().getResponseError())) {
                throw new RuntimeException(responseEntity.getBody().getResponseError().toString());
            }

            throw new RuntimeException("Failed create vpc server");
        }

        return responseEntity.getBody().getCreateServerInstancesResponse().getServerInstanceList().get(0);
    }

    public void stopServerInstances(final OperateVpcServersRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.STOP_VPC_SERVER_INSTANCES, requestDto);

        final ResponseEntity<String> response = restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed stop vpc servers");
        }
    }

    public void terminateServerInstances(final OperateVpcServersRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.TERMINATE_VPC_SERVER_INSTANCES, requestDto);

        final ResponseEntity<String> response = restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed terminate vpc servers");
        }
    }

    public void startServerInstances(final OperateVpcServersRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.START_VPC_SERVER_INSTANCES, requestDto);

        final ResponseEntity<String> response = restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed start vpc servers");
        }
    }

    public void rebootServerInstances(final OperateVpcServersRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REBOOT_VPC_SERVER_INSTANCES, requestDto);

        final ResponseEntity<String> response = restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed reboot vpc servers");
        }
    }
}
