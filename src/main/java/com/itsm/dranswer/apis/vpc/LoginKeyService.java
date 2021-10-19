package com.itsm.dranswer.apis.vpc;

/*
 * @package : com.itsm.dranswer.apis.vpc
 * @name : LoginKeyService.java
 * @date : 2021-10-08 오전 11:07
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

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


    /**
     *
     * @methodName : createLoginKey
     * @date : 2021-10-08 오전 11:04
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.CreateLoginKeyResponseDto.CreateLoginKeyRawResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public CreateLoginKeyResponseDto.CreateLoginKeyRawResponseDto createLoginKey(CreateLoginKeyRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {
        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_LOGIN_KEY, requestDto);

        final CreateLoginKeyResponseDto responseDto = restTemplate.exchange(
                OpenApiUtils.getOpenApiURI(apiServerHost, uri),
                HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), CreateLoginKeyResponseDto.class).getBody();;


        responseDto.checkError();

        CreateLoginKeyResponseDto.CreateLoginKeyRawResponseDto createLoginKeyResponseDto = responseDto.getCreateLoginKeyResponse();

        return createLoginKeyResponseDto;
    }

    /**
     *
     * @methodName : getLoginKeyList
     * @date : 2021-10-08 오전 11:04
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetLoginKeyListResponseDto
     * @throws
     * @modifyed :
     *
    **/
    public GetLoginKeyListResponseDto getLoginKeyList(GetLoginKeyListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_LOGIN_KEY_LIST, requestDto);

        final GetLoginKeyListResponseDto responseDto = restTemplate.exchange(
                OpenApiUtils.getOpenApiURI(apiServerHost, uri),
                HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetLoginKeyListResponseDto.class).getBody();;

        return responseDto;
    }
}
