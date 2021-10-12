package com.itsm.dranswer.apis.vpc;

/*
 * @package : com.itsm.dranswer.apis.vpc
 * @name : VpcServerService.java
 * @date : 2021-10-08 오전 11:24
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.apis.ApiService;
import com.itsm.dranswer.apis.OpenApiUrls;
import com.itsm.dranswer.apis.OpenApiUtils;
import com.itsm.dranswer.apis.vpc.request.*;
import com.itsm.dranswer.apis.vpc.response.CreateVpcServerResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetVpcServerDetailResponseDto;
import com.itsm.dranswer.apis.vpc.response.GetVpcServerListResponse;
import com.itsm.dranswer.apis.vpc.response.VpcPublicIpInstanceResponseDto;
import com.itsm.dranswer.users.NCloudKeyDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class VpcServerService extends ApiService {

    /**
     * get vpc server list
     * @methodName : getServerInstanceList
     * @date : 2021-10-08 오전 10:22
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetVpcServerListResponse
     * @throws
     * @modifyed :
     *
    **/
    public GetVpcServerListResponse getServerInstanceList(final GetVpcServerListRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_SERVER_INSTANCE_LIST, requestDto);

        return restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetVpcServerListResponse.class).getBody();
    }

    /**
     * get vpc server detail info
     * @methodName : getServerInstanceDetail
     * @date : 2021-10-08 오전 10:22
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.GetVpcServerDetailResponseDto.ServerInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    public GetVpcServerDetailResponseDto.ServerInstanceDto getServerInstanceDetail(final GetVpcServerDetailRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.GET_SERVER_INSTANCE_DETAIL, requestDto);

        final ResponseEntity<GetVpcServerDetailResponseDto> response = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), GetVpcServerDetailResponseDto.class);

        final GetVpcServerDetailResponseDto responseBody = response.getBody();

        if (ObjectUtils.isNotEmpty(responseBody.getGetServerInstanceDetailResponse())) {
            if (!CollectionUtils.isEmpty(responseBody.getGetServerInstanceDetailResponse().getServerInstanceList())) {
                return responseBody.getGetServerInstanceDetailResponse().getServerInstanceList().get(0);
            }
        }

        return null;
    }

    /**
     * create vpc server
     * @methodName : createServerInstances
     * @date : 2021-10-08 오전 10:23
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.CreateVpcServerResponseDto.ServerInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    public CreateVpcServerResponseDto.ServerInstanceDto createServerInstances(CreateVpcServerRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();


        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.CREATE_VPC_SERVER_INSTANCES, requestDto);

        final ResponseEntity<CreateVpcServerResponseDto> responseEntity = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), CreateVpcServerResponseDto.class);

        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            if(ObjectUtils.isNotEmpty(responseEntity.getBody()) && ObjectUtils.isNotEmpty(responseEntity.getBody().getResponseError())) {
                throw new RuntimeException(responseEntity.getBody().getResponseError().toString());
            }

            throw new RuntimeException("Failed create vpc server");
        }

        return responseEntity.getBody().getCreateServerInstancesResponse().getServerInstanceList().get(0);
    }

    /**
     * stop vpc server
     * @methodName : stopServerInstances
     * @date : 2021-10-08 오전 10:23
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void stopServerInstances(final OperateVpcServersRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.STOP_VPC_SERVER_INSTANCES, requestDto);

        final ResponseEntity<String> response = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed stop vpc servers");
        }
    }

    /**
     * terminate vpc server
     * @methodName : terminateServerInstances
     * @date : 2021-10-08 오전 10:23
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void terminateServerInstances(final OperateVpcServersRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.TERMINATE_VPC_SERVER_INSTANCES, requestDto);

        final ResponseEntity<String> response = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed terminate vpc servers");
        }
    }

    /**
     * start vpc server
     * @methodName : startServerInstances
     * @date : 2021-10-08 오전 10:23
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void startServerInstances(final OperateVpcServersRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.START_VPC_SERVER_INSTANCES, requestDto);

        final ResponseEntity<String> response = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed start vpc servers");
        }
    }

    /**
     * reboot vpc server
     * @methodName : rebootServerInstances
     * @date : 2021-10-08 오전 10:23
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void rebootServerInstances(final OperateVpcServersRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(OpenApiUrls.REBOOT_VPC_SERVER_INSTANCES, requestDto);

        final ResponseEntity<String> response = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed reboot vpc servers");
        }
    }

    /**
     * 공인 IP 해제(From Server)
     * @methodName : disassociatePublicIpFromServerInstance
     * @date : 2021-10-12 오후 4:27
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.VpcPublicIpInstanceResponseDto.PublicIpInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    public VpcPublicIpInstanceResponseDto.PublicIpInstanceDto disassociatePublicIpFromServerInstance(final OperateVpcPublicIpRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        return requestPublicIpInstance(OpenApiUrls.DISASSOCIATE_PUBLIC_IP_INSTANCE, requestDto, nCloudKeyDto);

    }

    /**
     * 공인 IP 삭제
     * @methodName : deletePublicIpInstance
     * @date : 2021-10-12 오후 4:27
     * @author : xeroman.k
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.VpcPublicIpInstanceResponseDto.PublicIpInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    public VpcPublicIpInstanceResponseDto.PublicIpInstanceDto deletePublicIpInstance(final OperateVpcPublicIpRequestDto requestDto, NCloudKeyDto nCloudKeyDto) {

        return requestPublicIpInstance(OpenApiUrls.DELETE_PUBLIC_IP_INSTANCE, requestDto, nCloudKeyDto);

    }

    /**
     *
     * @methodName : requestPublicIpInstance
     * @date : 2021-10-12 오후 4:27
     * @author : xeroman.k
     * @param reqUri
     * @param requestDto
     * @param nCloudKeyDto
     * @return : com.itsm.dranswer.apis.vpc.response.VpcPublicIpInstanceResponseDto.PublicIpInstanceDto
     * @throws
     * @modifyed :
     *
    **/
    private VpcPublicIpInstanceResponseDto.PublicIpInstanceDto requestPublicIpInstance(String reqUri, final OperateVpcPublicIpRequestDto requestDto, NCloudKeyDto nCloudKeyDto){
        String nCloudAccessKey = nCloudKeyDto.getNCloudAccessKey();
        String nCloudSecretKey = nCloudKeyDto.getNCloudSecretKey();

        final String uri = OpenApiUtils.getOpenApiUrl(reqUri, requestDto);

        final ResponseEntity<VpcPublicIpInstanceResponseDto> response = restTemplate.exchange(apiServerHost + uri, HttpMethod.GET, new HttpEntity(getNcloudUserApiHeader(HttpMethod.GET, uri, nCloudAccessKey, nCloudSecretKey)), VpcPublicIpInstanceResponseDto.class);

        VpcPublicIpInstanceResponseDto vpcPublicIpInstanceResponseDto = response.getBody();
        vpcPublicIpInstanceResponseDto.checkError();

        return vpcPublicIpInstanceResponseDto.getDisassociatePublicIpFromServerInstanceResponse().getPublicIpInstanceList().get(0);
    }
}
