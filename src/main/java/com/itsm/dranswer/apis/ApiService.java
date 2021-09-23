package com.itsm.dranswer.apis;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.time.Instant;

public class ApiService {

    protected final String apiServerHost = "https://ncloud.apigw.ntruss.com";

    @Autowired
    protected RestTemplate restTemplate;

    private String makeSignature(String timestamp, String method, String url, String accessKey, String secretKey) throws Exception {
        String space = " ";					// one space
        String newLine = "\n";					// new line

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    protected HttpHeaders getNcloudUserApiHeader(HttpMethod method, String url, String accessKey, String secretKey) {
        try {
            MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
            //String timeStamp = String.valueOf(System.currentTimeMillis());
            String timeStamp = Instant.now().toEpochMilli()+"";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("x-ncp-apigw-timestamp", timeStamp);
            httpHeaders.add("x-ncp-iam-access-key", accessKey);
            httpHeaders.add("x-ncp-apigw-signature-v2", makeSignature(timeStamp, method.name(), url, accessKey, secretKey));
            httpHeaders.setContentType(mediaType);
            return httpHeaders;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
