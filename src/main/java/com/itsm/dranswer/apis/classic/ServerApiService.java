package com.itsm.dranswer.apis.classic;

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.classic.dto.CommonCode;
import com.itsm.dranswer.apis.classic.dto.Product;
import com.itsm.dranswer.apis.classic.dto.ProductList;
import com.itsm.dranswer.apis.classic.dto.PublicIpInstance;
import com.itsm.dranswer.apis.classic.request.*;
import com.itsm.dranswer.apis.classic.response.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServerApiService extends ApiService {

    private final String CLASSIC_SERVER = "/server/v2";

    @Value("${ncp.laif.access-key}")
    private String laifAccessKey;

    @Value("${ncp.laif.secret-key}")
    private String laifSecretKey;

    private String makeUrl(String uri){
        return apiServerHost + uri;
    }

    private HttpEntity makeHttpEntity(String uri){
        HttpHeaders headers = getNcloudUserApiHeader(HttpMethod.GET, uri, laifAccessKey, laifSecretKey);
        HttpEntity httpEntity = new HttpEntity(headers);

        return httpEntity;
    }

    public List<Product> getServerImageProductList(ReqGetServerImageProductList reqGetServerImageProductList){

        final String uri = OpenApiUtils.getOpenApiUrl(CLASSIC_SERVER+"/getServerImageProductList", reqGetServerImageProductList);

        ResponseEntity<GetServerImageProductListResponse> responseEntity = restTemplate.exchange(makeUrl(uri), HttpMethod.GET,
                makeHttpEntity(uri), GetServerImageProductListResponse.class);

        ProductList productList = responseEntity.getBody().getGetServerImageProductListResponse();

        List<Product> plist = productList.getProductList();

        plist = plist.stream().filter( e -> e.getProductDescription().indexOf("with") < 0).collect(Collectors.toList());

        return plist;
    }

    public List<Product> getServerProductList(ReqGetServerProductList reqGetServerProductList){

        final String uri = OpenApiUtils.getOpenApiUrl(CLASSIC_SERVER+"/getServerProductList", reqGetServerProductList);

        ResponseEntity<GetServerProductListResponse> responseEntity = restTemplate.exchange(makeUrl(uri), HttpMethod.GET,
                makeHttpEntity(uri), GetServerProductListResponse.class);

        ProductList productList = responseEntity.getBody().getGetServerProductListResponse();

        List<Product> plist = productList.getProductList();

        Set<CommonCode> codes = new LinkedHashSet<>();
        for(Product product : plist){
            codes.add(product.getProductType());
        }

        String s = codes.toString();

        if(reqGetServerProductList.getCpuCount() != null){
            plist = plist.stream().filter(e -> reqGetServerProductList.getCpuCount().equals(String.valueOf(e.getCpuCount()))).collect(Collectors.toList());
        }

        if(reqGetServerProductList.getMemorySize() != null){
            plist = plist.stream().filter(e -> reqGetServerProductList.getMemorySize().equals(String.valueOf(e.getMemorySize()/1024/1024/1024))).collect(Collectors.toList());
        }

        if(reqGetServerProductList.getBaseBlockStorageSize() != null){
            plist = plist.stream().filter(e -> reqGetServerProductList.getBaseBlockStorageSize().equals(String.valueOf(e.getBaseBlockStorageSize()/1024/1024/1024))).collect(Collectors.toList());
        }

        if(reqGetServerProductList.getProductType() != null){
            plist = plist.stream().filter(e -> reqGetServerProductList.getProductType().equals(e.getProductType().getCode())).collect(Collectors.toList());
        }

        if(reqGetServerProductList.getStorageType().equals("SSD")){
            plist = plist.stream().filter(e -> e.getProductCode().indexOf("SSD") > -1).collect(Collectors.toList());
        }else{
            plist = plist.stream().filter(e -> e.getProductCode().indexOf("SSD") < 0).collect(Collectors.toList());
        }

        return plist;
    }

    public CreateServerResponse.ServerInstance createServerInstances(ReqCreateServerInstances reqCreateServerInstances) {

        final String uri = OpenApiUtils.getOpenApiUrl(CLASSIC_SERVER+"/createServerInstances", reqCreateServerInstances);

        ResponseEntity<CreateServerResponse> responseEntity = restTemplate.exchange(makeUrl(uri), HttpMethod.GET,
                makeHttpEntity(uri), CreateServerResponse.class);

        CreateServerResponse createServerResponse = responseEntity.getBody();

        return createServerResponse.getCreateServerInstancesResponse().getServerInstanceList().get(0);
    }

    public List<GetLoginKeyListResponse.LoginKey> getLoginKeyList(ReqGetLoginKeyList reqGetLoginKeyList){
        final String uri = OpenApiUtils.getOpenApiUrl(CLASSIC_SERVER+"/getLoginKeyList", reqGetLoginKeyList);

        ResponseEntity<GetLoginKeyListResponse> responseEntity = restTemplate.exchange(makeUrl(uri), HttpMethod.GET,
                makeHttpEntity(uri), GetLoginKeyListResponse.class);

        GetLoginKeyListResponse getLoginKeyListResponse = responseEntity.getBody();

        return getLoginKeyListResponse.getGetLoginKeyListResponse().getLoginKeyList();

    }

    public CreateLoginKeyResponse.CreateLoginKeyRawResponse createLoginKey(ReqCreateLoginKey reqCreateLoginKey){
        final String uri = OpenApiUtils.getOpenApiUrl(CLASSIC_SERVER+"/createLoginKey", reqCreateLoginKey);

        ResponseEntity<CreateLoginKeyResponse> responseEntity = restTemplate.exchange(makeUrl(uri), HttpMethod.GET,
                makeHttpEntity(uri), CreateLoginKeyResponse.class);

        CreateLoginKeyResponse r = responseEntity.getBody();

        return r.getCreateLoginKeyResponse();

    }

    public PublicIpInstance createPublicIpInstance(ReqCreatePublicIpInstance reqCreatePublicIpInstance){
        final String uri = OpenApiUtils.getOpenApiUrl(CLASSIC_SERVER+"/createPublicIpInstance", reqCreatePublicIpInstance);

        ResponseEntity<CreatePublicIpInstanceResponse> responseEntity = restTemplate.exchange(makeUrl(uri), HttpMethod.GET,
                makeHttpEntity(uri), CreatePublicIpInstanceResponse.class);

        CreatePublicIpInstanceResponse r = responseEntity.getBody();


        return r.getCreatePublicIpInstanceResponse().getPublicIpInstanceList().get(0);
    }

    public PublicIpInstance associatePublicIpWithServerInstance(ReqAssociatePublicIpWithServerInstance reqAssociatePublicIpWithServerInstance){
        final String uri = OpenApiUtils.getOpenApiUrl(CLASSIC_SERVER+"/associatePublicIpWithServerInstance", reqAssociatePublicIpWithServerInstance);

        ResponseEntity<AssociatePublicIpWithServerInstanceResponse> responseEntity = restTemplate.exchange(makeUrl(uri), HttpMethod.GET,
                makeHttpEntity(uri), AssociatePublicIpWithServerInstanceResponse.class);

        AssociatePublicIpWithServerInstanceResponse r = responseEntity.getBody();

        return r.getAssociatePublicIpWithServerInstanceResponse().getPublicIpInstanceList().get(0);
    }

    public void testModule(ReqCreatePublicIpInstance reqCreatePublicIpInstance){
        final String uri = OpenApiUtils.getOpenApiUrl(CLASSIC_SERVER+"/createPublicIpInstance", reqCreatePublicIpInstance);

        ResponseEntity<String> responseEntity = restTemplate.exchange(makeUrl(uri), HttpMethod.GET,
                makeHttpEntity(uri), String.class);

        String r = responseEntity.getBody();


        System.out.println(r);
    }


}
