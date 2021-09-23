package com.itsm.dranswer.apis.vpc;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.CreateNetworkInterfaceRequestDto;
import com.itsm.dranswer.apis.vpc.request.GetNetworkInterfaceListRequestDto;
import com.itsm.dranswer.apis.vpc.response.CreateNetworkInterfaceResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetNetworkInterfaceListResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

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
}
