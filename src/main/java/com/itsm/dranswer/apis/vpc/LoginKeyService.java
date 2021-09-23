package com.itsm.dranswer.apis.vpc;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.CreateLoginKeyRequestDto;
import com.itsm.dranswer.apis.vpc.request.GetLoginKeyListRequestDto;
import com.itsm.dranswer.apis.vpc.response.CreateLoginKeyResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetLoginKeyListResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class LoginKeyService extends ApiService {

    private final String openApiHost = apiServerHost;

    @Value("${ncp.laif.access-key}")
    private String laifAccessKey;

    @Value("${ncp.laif.secret-key}")
    private String laifSecretKey;

    public CreateLoginKeyResponseDto.CreateLoginKeyRawResponseDto createLoginKey(CreateLoginKeyRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_LOGIN_KEY, requestDto);

        final CreateLoginKeyResponseDto responseDto = restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), CreateLoginKeyResponseDto.class).getBody();;

        return responseDto.getCreateLoginKeyResponse();
    }

    public GetLoginKeyListResponseDto getLoginKeyList(GetLoginKeyListRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_LOGIN_KEY, requestDto);

        final GetLoginKeyListResponseDto responseDto = restTemplate.exchange(
                openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetLoginKeyListResponseDto.class).getBody();;

        return responseDto;
    }
}
