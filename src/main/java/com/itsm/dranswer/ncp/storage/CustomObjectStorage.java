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
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.itsm.dranswer.storage.S3ObjectDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
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

    public void putBucketCORS(String endPoint, String regionName,
                              String accessKey, String secretKey, String bucketName){

        final AmazonS3 s3 = getS3(endPoint, regionName, accessKey, secretKey);

        BucketCrossOriginConfiguration bucketCrossOriginConfiguration = new BucketCrossOriginConfiguration();

        CORSRule corsRule = new CORSRule();
        corsRule.setAllowedOrigins("*");
        corsRule.setAllowedHeaders("*");
        corsRule.setExposedHeaders("ETag");
        corsRule.setMaxAgeSeconds(3000);
        corsRule.setAllowedMethods(CORSRule.AllowedMethods.GET, CORSRule.AllowedMethods.PUT,
                CORSRule.AllowedMethods.POST, CORSRule.AllowedMethods.HEAD);

        bucketCrossOriginConfiguration.withRules(corsRule);

        s3.setBucketCrossOriginConfiguration(bucketName, bucketCrossOriginConfiguration);

    }

    public void getBucketCORS(String endPoint, String regionName,
                              String accessKey, String secretKey, String bucketName){

        final AmazonS3 s3 = getS3(endPoint, regionName, accessKey, secretKey);

        BucketCrossOriginConfiguration b = s3.getBucketCrossOriginConfiguration(bucketName);
        for(CORSRule corsRule : b.getRules()){
            corsRule.getAllowedOrigins().forEach(e->{
//                System.out.println(e);
            });
        }

        CannedAccessControlList cannedAccessControlList = CannedAccessControlList.PublicRead;
        s3.setBucketAcl(bucketName, cannedAccessControlList);

        AccessControlList accessControlList =  s3.getBucketAcl(bucketName);
        for(Grant grant : accessControlList.getGrantsAsList()){
            System.out.println("dpName = "+accessControlList.getOwner().getDisplayName());
            System.out.println("getIdentifier = "+grant.getGrantee().getIdentifier());
            System.out.println("permName = "+grant.getPermission().name());

        }
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
     * @methodName : deleteBucket
     * @date : 2021-07-08 오후 1:26
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
    public void deleteBucket(String endPoint, String regionName, String accessKey, String secretKey, String bucketName) {
        final AmazonS3 s3 = getS3(endPoint, regionName, accessKey, secretKey);

        try {

            /* Get list of objects in a given bucket */
            ObjectListing objects = s3.listObjects(bucketName);

            /* Recursively delete all the objects inside given bucket */
            if(objects != null && objects.getObjectSummaries() != null) {
                while (true) {
                    for(S3ObjectSummary summary : objects.getObjectSummaries()) {
                        s3.deleteObject(bucketName, summary.getKey());
                    }

                    if (objects.isTruncated()) {
                        objects = s3.listNextBatchOfObjects(objects);
                    } else {
                        break;
                    }
                }
            }

            /* Get list of versions in a given bucket */
            VersionListing versions = s3.listVersions(new ListVersionsRequest().withBucketName(bucketName));

            /* Recursively delete all the versions inside given bucket */
            if(versions != null && versions.getVersionSummaries() != null) {
                while (true) {
                    for(S3VersionSummary summary : versions.getVersionSummaries()) {
                        s3.deleteObject(bucketName, summary.getKey());
                    }

                    if (versions.isTruncated()) {
                        versions = s3.listNextBatchOfVersions(versions);
                    } else {
                        break;
                    }
                }
            }

            /* Send Delete Bucket Request */
            s3.deleteBucket(bucketName);
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

    public List<S3ObjectDto> getObjectList(String endPoint, String regionName,
                                           String accessKey, String secretKey,
                                           String bucketName, String folderName){

        List<S3ObjectDto> list = new ArrayList<>();

        final AmazonS3 s3 = getS3(endPoint, regionName, accessKey, secretKey);

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withDelimiter("/")
                .withPrefix(folderName)
                .withMaxKeys(300);

        ObjectListing objectListing = s3.listObjects(listObjectsRequest);

        for(String folder:objectListing.getCommonPrefixes()){
            list.add(new S3ObjectDto(folder, folderName));
        }

        while (true) {
            for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
                if(!folderName.equals(os.getKey())){
                    list.add(new S3ObjectDto(os, folderName));
                }
            }

            if (objectListing.isTruncated()) {
                objectListing = s3.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }

//        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(300)
//                .withStartAfter("/")
//                ;
//
//        ListObjectsV2Result result;
//
//        do {
//            result = s3.listObjectsV2(req);
//
//            for (S3ObjectSummary os : result.getObjectSummaries()) {
//                list.add(new S3ObjectDto(os));
//            }
//            // If there are more than maxKeys keys in the bucket, get a continuation token
//            // and list the next objects.
//            String token = result.getNextContinuationToken();
//            req.setContinuationToken(token);
//        } while (result.isTruncated());

        return list;
    }

    public void setBucketACL(String endPoint, String regionName,
                             String accessKey, String secretKey,
                             String bucketName, String ncpId){

        final AmazonS3 s3 = getS3(endPoint, regionName, accessKey, secretKey);

        AccessControlList accessControlList = s3.getBucketAcl(bucketName);
        accessControlList.grantPermission(new CanonicalGrantee(ncpId), Permission.FullControl);
        s3.setBucketAcl(bucketName, accessControlList);
    }

    public String uploadObject(String endPoint, String regionName,
                             String accessKey, String secretKey,
                             String bucketName, String folderName,
                             String objectName, File file,
                               CannedAccessControlList cannedAccessControlList) throws InterruptedException {

        final AmazonS3 s3 = getS3(endPoint, regionName, accessKey, secretKey);

        String keyName = folderName+objectName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, file).withCannedAcl(cannedAccessControlList);

        if(checkFileSize(file)){
            this.multipartUpload(s3, putObjectRequest);
        }else {
            this.upload(s3, putObjectRequest);
        }

        return keyName;
    }

    public void deleteObject(String endPoint, String regionName,
                             String accessKey, String secretKey,
                             String bucketName, String objectName){


        final AmazonS3 s3 = getS3(endPoint, regionName, accessKey, secretKey);

        s3.deleteObject(bucketName, objectName);
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

    private void upload(AmazonS3 s3, PutObjectRequest putObjectRequest){

        try {
            s3.putObject(putObjectRequest);

        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            throw e;
        } catch(SdkClientException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void multipartUpload(AmazonS3 s3, PutObjectRequest putObjectRequest) throws InterruptedException {

        TransferManager tm = TransferManagerBuilder.standard()
                .withS3Client(s3)
                .withMultipartUploadThreshold((long) (10 * 1024 * 1024))
                .withExecutorFactory(() -> Executors.newFixedThreadPool(10))
                .build();
        Upload upload = tm.upload(putObjectRequest);
        upload.waitForCompletion();

    }
}
