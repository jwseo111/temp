package com.itsm.dranswer.apis.vpc;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.GetServerImageProductListRequestDto;
import com.itsm.dranswer.apis.vpc.request.GetServerProductListRequestDto;
import com.itsm.dranswer.apis.vpc.request.GetZoneListRequestDto;
import com.itsm.dranswer.apis.vpc.response.GetRegionListResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetServerImageProductListResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetServerProductListResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetZoneListResponseDto;
import com.itsm.dranswer.users.NCloudKeyDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VpcCommonService extends ApiService {

    public GetRegionListResponseDto getRegionList(NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_REGION_LIST);

        GetRegionListResponseDto responseDto = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetRegionListResponseDto.class).getBody();

        responseDto.checkError();

        return responseDto;
    }

    public GetZoneListResponseDto getZoneList(final GetZoneListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_ZONE_LIST, requestDto);

        GetZoneListResponseDto responseDto = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetZoneListResponseDto.class).getBody();

        responseDto.checkError();

        return responseDto;
    }

    public GetServerImageProductListResponseDto getServerImageProductList(final GetServerImageProductListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_SERVER_IMAGE_PRODUCT_LIST, requestDto);

        GetServerImageProductListResponseDto getServerImageProductListResponseDto = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetServerImageProductListResponseDto.class).getBody();

        getServerImageProductListResponseDto.checkError();

        getServerImageProductListResponseDto.getGetServerImageProductListResponse().setProductList(
                getServerImageProductListResponseDto.getGetServerImageProductListResponse().getProductList().stream().filter( e -> e.getProductDescription().indexOf("with") < 0 ).collect(Collectors.toList()));

        return getServerImageProductListResponseDto;
    }

    public GetServerProductListResponseDto getServerProductList(final GetServerProductListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_SERVER_PRODUCT_LIST, requestDto);

        GetServerProductListResponseDto getServerProductListResponseDto = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetServerProductListResponseDto.class).getBody();

        getServerProductListResponseDto.checkError();

        List<GetServerProductListResponseDto.ProductDto> plist = getServerProductListResponseDto.getGetServerProductListResponse().getProductList();

        if(requestDto.getCpuCount() != null){
            plist = plist.stream().filter(e -> requestDto.getCpuCount().equals(String.valueOf(e.getCpuCount()))).collect(Collectors.toList());
        }

        if(requestDto.getMemorySize() != null){
            plist = plist.stream().filter(e -> requestDto.getMemorySize().equals(String.valueOf(e.getMemorySize()/1024/1024/1024))).collect(Collectors.toList());
        }

        if(requestDto.getBaseBlockStorageSize() != null){
            plist = plist.stream().filter(e -> requestDto.getBaseBlockStorageSize().equals(String.valueOf(e.getBaseBlockStorageSize()/1024/1024/1024))).collect(Collectors.toList());
        }

        if(requestDto.getProductType() != null){
            plist = plist.stream().filter(e -> requestDto.getProductType().equals(e.getProductType().getCode())).collect(Collectors.toList());
        }

        if(requestDto.getStorageType().equals("SSD")){
            plist = plist.stream().filter(e -> e.getProductCode().indexOf("SSD") > -1).collect(Collectors.toList());
        }else{
            plist = plist.stream().filter(e -> e.getProductCode().indexOf("SSD") < 0).collect(Collectors.toList());
        }

        getServerProductListResponseDto.getGetServerProductListResponse().setProductList(plist);

        return getServerProductListResponseDto;
    }
}
