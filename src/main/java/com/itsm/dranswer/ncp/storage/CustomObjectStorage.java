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
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

@Component
public class CustomObjectStorage {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private AmazonS3 getS3(String endPoint, String regionName,
                           String accessKey, String secretKey){
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
        return s3;
    }
    
    /**
     * 
     * @methodName : makeBucket
     * @date : 2021-06-28 오후 1:44
     * @author : xeroman.k 
     * @param endPoint
     * @param regionName
     * @param accessKey
     * @param secretKey
     * @param bucketName
     * @return : void
     * @throws 
     * @modifyed :
     *
    **/
    public void makeBucket(String endPoint, String regionName,
                           String accessKey, String secretKey, String bucketName){

        final AmazonS3 s3 = getS3(endPoint, regionName, accessKey, secretKey);

        try {
            // create bucket if the bucket name does not exist
            if (s3.doesBucketExistV2(bucketName)) {
                throw new IllegalArgumentException("이미 사용중인 버킷명 입니다.");
            } else {
                s3.createBucket(bucketName);
            }
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            throw e;
        } catch(SdkClientException e) {
            e.printStackTrace();
            throw e;
        }
    }

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
        final AmazonS3 s3 = getS3(endPoint, regionName, accessKey, secretKey);

        try {
            List<Bucket> buckets = s3.listBuckets();
            log.debug("======================================================Bucket List: ===============================================================");
            for (Bucket bucket : buckets) {
                log.debug("    name=" + bucket.getName() + ", creation_date=" + bucket.getCreationDate() + ", owner=" + bucket.getOwner().getId());
            }

            return buckets;
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            throw e;
        } catch(SdkClientException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void uploadObject(String endPoint, String regionName,
                             String accessKey, String secretKey,
                             String bucketName, String folderName,
                             String objectName, File file) throws InterruptedException {

        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기

        final AmazonS3 s3 = getS3(endPoint, regionName, accessKey, secretKey);

        if(checkFileSize(file)){
            this.multipartUpload(s3, bucketName, folderName, objectName, file);
        }else {
            this.upload(s3, bucketName, folderName, objectName, file);
        }

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
        System.out.println("시간차이(m) : "+secDiffTime);
    }

    private boolean checkFileSize(File file){
        return file.length()/1024/1024 > 50;
    }

    private void makeDirectory(AmazonS3 s3, String bucketName, String folderName){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0L);
        objectMetadata.setContentType("application/x-directory");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);

        try {
            s3.putObject(putObjectRequest);
            System.out.format("Folder %s has been created.\n", folderName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            throw e;
        } catch(SdkClientException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void upload(AmazonS3 s3, String bucketName, String folderName, String objectName, File file){

        try {
            String keyName = folderName+objectName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, file);
            s3.putObject(putObjectRequest);
            System.out.format("Object %s has been created.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            throw e;
        } catch(SdkClientException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void multipartUpload(AmazonS3 s3, String bucketName, String folderName, String objectName, File file) throws InterruptedException {
        String keyName = folderName+objectName;

        TransferManager tm = TransferManagerBuilder.standard()
                .withS3Client(s3)
                .withMultipartUploadThreshold((long) (10 * 1024 * 1024))
                .withExecutorFactory(() -> Executors.newFixedThreadPool(10))
                .build();

        Upload upload = tm.upload(bucketName, keyName, file);
        upload.waitForCompletion();
    }

}
