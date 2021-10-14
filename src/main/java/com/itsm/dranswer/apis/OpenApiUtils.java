package com.itsm.dranswer.apis;

/*
 * @package : com.itsm.dranswer.apis
 * @name : OpenApiUtils.java
 * @date : 2021-10-08 오후 1:19
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenApiUtils {

    /**
     * not exists parameter object
     * @methodName : getOpenApiUrl
     * @date : 2021-10-08 오후 1:20
     * @author : xeroman.k
     * @param uri
     * @return : java.lang.String
     * @throws
     * @modifyed :
     *
    **/
    public static String getOpenApiUrl(String uri) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri);

        return uriBuilder.toUriString() + "?responseFormatType=json";
    }

    /**
     * exists parameter object
     * @methodName : getOpenApiUrl
     * @date : 2021-10-08 오후 1:20
     * @author : xeroman.k
     * @param uri
     * @param requestDto
     * @return : java.lang.String
     * @throws
     * @modifyed :
     *
    **/
    public static String getOpenApiUrl(String uri, Object requestDto) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri);

        UriComponentsBuilder builder = uriAndParamMerge(uriBuilder, "", requestDto);
        builder.replaceQueryParam("responseFormatType", "json");

        String convert = builder.toUriString();

        return convert;
    }

    /**
     * 
     * @methodName : getOpenApiURI
     * @date : 2021-10-14 오후 2:40
     * @author : xeroman.k 
     * @param serverHost
     * @param uri
     * @return : java.net.URI
     * @throws 
     * @modifyed :
     * getOpenApiUrl 에서 uri를 자동으로 encoding을 하고, 그걸 바탕으로 signature를 생성
     * restTeample의 sxchange는 uri string을 던지면 기본적으로 encoding을 진행하기에
     * 인코딩된 uri를 또 다시 인코딩하는 현상이 발생하여
     * 재 인코딩을 방지하고자 URI로 exchange를 시도
    **/
    public static URI getOpenApiURI(String serverHost, String uri){
        return UriComponentsBuilder.fromUriString(serverHost + uri).build(true).toUri();
    }

    private static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        objectMapper.setDateFormat(df);

        return objectMapper;
    }

    /**
     * uri와 parameter object를 query string 형태로 변환하기 위한 작업
     * @methodName : uriAndParamMerge
     * @date : 2021-10-08 오후 1:21
     * @author : xeroman.k
     * @param uriBuilder
     * @param keyPrefix
     * @param getParameters
     * @return : org.springframework.web.util.UriComponentsBuilder
     * @throws
     * @modifyed :
     *
    **/
    private static UriComponentsBuilder uriAndParamMerge(UriComponentsBuilder uriBuilder, String keyPrefix, final Object getParameters) {
        final Map<String, Object> map = objectMapper().convertValue(getParameters, new TypeReference<HashMap>() {
        });
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            final Object value = entry.getValue();
            if (value instanceof List) {
                final List<Map> list = objectMapper().convertValue(value, new TypeReference<List>() {
                });
                for (int i=0; i < list.size(); i++) {
                    if (list.get(i) instanceof Map) {
                        uriBuilder = uriAndParamMerge(uriBuilder, keyPrefix + entry.getKey() + "." + (i+1) + ".", list.get(i));
                    } else {
                        uriBuilder = uriBuilder.replaceQueryParam(keyPrefix + entry.getKey() + "." + (i+1), list.get(i));
                    }
                }
            } else {
                uriBuilder = uriBuilder.replaceQueryParam(keyPrefix + entry.getKey(), value);
            }
        }
        return uriBuilder;
    }

}
