package com.itsm.dranswer.ncp.storage;

/*
 * @package : com.itsm.ncp.storage
 * @name : CustomObjectStorage.java
 * @date : 2021-06-07 오후 4:00
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomObjectStorage {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 
     * @methodName : getBucketList
     * @date : 2021-06-07 오후 4:14
     * @author : xeroman.k
     * @param endPoint
     * @param regionName
     * @param accessKey
     * @param secretKey
     * @return : void
     * @throws 
     * @modifyed :
     *
    **/
    public List<Bucket> getBucketList(String endPoint, String regionName,
                          String accessKey, String secretKey){
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        try {
            List<Bucket> buckets = s3.listBuckets();
            log.debug("======================================================Bucket List: ===============================================================");
            for (Bucket bucket : buckets) {
                log.debug("    name=" + bucket.getName() + ", creation_date=" + bucket.getCreationDate() + ", owner=" + bucket.getOwner().getId());
            }

            return buckets;
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

        return null;
    }
}
