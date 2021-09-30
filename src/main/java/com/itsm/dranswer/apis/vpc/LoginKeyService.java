package com.itsm.dranswer.apis.vpc;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.CreateLoginKeyRequestDto;
import com.itsm.dranswer.apis.vpc.request.GetLoginKeyListRequestDto;
import com.itsm.dranswer.apis.vpc.response.CreateLoginKeyResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetLoginKeyListResponseDto;
import com.itsm.dranswer.users.NCloudKeyDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class LoginKeyService extends ApiService {

    public CreateLoginKeyResponseDto.CreateLoginKeyRawResponseDto createLoginKey(CreateLoginKeyRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_LOGIN_KEY, requestDto);

        final CreateLoginKeyResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), CreateLoginKeyResponseDto.class).getBody();;

        return responseDto.getCreateLoginKeyResponse();
    }

    public GetLoginKeyListResponseDto getLoginKeyList(GetLoginKeyListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_LOGIN_KEY_LIST, requestDto);

        final GetLoginKeyListResponseDto responseDto = restTemplate.exchange(
                apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetLoginKeyListResponseDto.class).getBody();;

        return responseDto;
    }
}
