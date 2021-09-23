package com.itsm.dranswer.apis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenApiUtils {

    public static String getOpenApiUrl(String uri) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri);

        return uriBuilder.toUriString() + "?responseFormatType=json";
    }

    public static String getOpenApiUrl(String uri, Object requestDto) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri);

        String convert = uriAndParamMerge(uriBuilder, "", requestDto).toUriString();

        if(convert.equals(uri)){
            return convert + "?responseFormatType=json";
        }

        return uriAndParamMerge(uriBuilder, "", requestDto).toUriString() + "&responseFormatType=json";
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
