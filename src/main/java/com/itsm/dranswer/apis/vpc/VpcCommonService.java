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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VpcCommonService extends ApiService {

    private final String openApiHost = apiServerHost;

    @Value("${ncp.laif.access-key}")
    private String laifAccessKey;

    @Value("${ncp.laif.secret-key}")
    private String laifSecretKey;

    public GetRegionListResponseDto getRegionList() {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_REGION_LIST);

        return restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetRegionListResponseDto.class).getBody();
    }

    public GetZoneListResponseDto getZoneList(final GetZoneListRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_ZONE_LIST, requestDto);

        return restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetZoneListResponseDto.class).getBody();
    }

    public GetServerImageProductListResponseDto getServerImageProductList(final GetServerImageProductListRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_SERVER_IMAGE_PRODUCT_LIST, requestDto);

        GetServerImageProductListResponseDto getServerImageProductListResponseDto = restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetServerImageProductListResponseDto.class).getBody();

        getServerImageProductListResponseDto.getGetServerImageProductListResponse().setProductList(
                getServerImageProductListResponseDto.getGetServerImageProductListResponse().getProductList().stream().filter( e -> e.getProductDescription().indexOf("with") < 0 ).collect(Collectors.toList()));

        return getServerImageProductListResponseDto;
    }

    public GetServerProductListResponseDto getServerProductList(final GetServerProductListRequestDto requestDto) {
        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_VPC_SERVER_PRODUCT_LIST, requestDto);

        GetServerProductListResponseDto getServerProductListResponseDto = restTemplate.exchange(openApiHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey)), GetServerProductListResponseDto.class).getBody();

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
