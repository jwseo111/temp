package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : StorageService.java
 * @date : 2021-06-24 오후 2:34
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */


import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.itsm.dranswer.commons.Disease;
import com.itsm.dranswer.config.CustomMailSender;
import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.errors.NotFoundException;
import com.itsm.dranswer.etc.FileUploadResponse;
import com.itsm.dranswer.etc.FileUploadResponse.FileObject;
import com.itsm.dranswer.ncp.storage.CustomObjectStorage;
import com.itsm.dranswer.users.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StorageService {

    private final UserService userService;

    private final ReqStorageInfoRepo reqStorageInfoRepo;
    private final ReqStorageInfoRepoSupport reqStorageInfoRepoSupport;

    private final OpenStorageInfoRepo openStorageInfoRepo;
    private final OpenStorageInfoRepoSupport openStorageInfoRepoSupport;

    private final UseStorageInfoRepo useStorageInfoRepo;
    private final UseStorageInfoRepoSupport useStorageInfoRepoSupport;

    private final CustomObjectStorage customObjectStorage;
    private final BucketInfoRepo bucketInfoRepo;
    private final CustomMailSender customMailSender;

    @Value("${ncp.laif.end-point}")
    private String endPoint;
    @Value("${ncp.laif.region-name}")
    private String regionName;
    @Value("${ncp.laif.access-key}")
    private String laifAccessKey;
    @Value("${ncp.laif.secret-key}")
    private String laifSecretKey;
    @Value("${ncp.laif.server-access-key}")
    private String laifServerAccessKey;
    @Value("${ncp.laif.server-secret-key}")
    private String laifServerSecretKey;

    @Autowired
    public StorageService(UserService userService, ReqStorageInfoRepo reqStorageInfoRepo, OpenStorageInfoRepo openStorageInfoRepo, ReqStorageInfoRepoSupport reqStorageInfoRepoSupport, OpenStorageInfoRepoSupport openStorageInfoRepoSupport, CustomObjectStorage customObjectStorage, BucketInfoRepo bucketInfoRepo, CustomMailSender customMailSender, UseStorageInfoRepo useStorageInfoRepo, UseStorageInfoRepoSupport useStorageInfoRepoSupport) {
        this.userService = userService;
        this.reqStorageInfoRepo = reqStorageInfoRepo;
        this.openStorageInfoRepo = openStorageInfoRepo;
        this.reqStorageInfoRepoSupport = reqStorageInfoRepoSupport;
        this.openStorageInfoRepoSupport = openStorageInfoRepoSupport;
        this.customObjectStorage = customObjectStorage;
        this.bucketInfoRepo = bucketInfoRepo;
        this.customMailSender = customMailSender;
        this.useStorageInfoRepo = useStorageInfoRepo;
        this.useStorageInfoRepoSupport = useStorageInfoRepoSupport;
    }

    /**
     *
     * @methodName : reqStorage
     * @date : 2021-06-24 오후 2:33
     * @author : xeroman.k
     * @param reqStorageInfoDto
     * @return : com.itsm.dranswer.storage.ReqStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfoDto reqStorage(ReqStorageInfoDto reqStorageInfoDto) {

        ReqStorageInfo reqStorageInfo = new ReqStorageInfo(reqStorageInfoDto);
        return this.saveReqStorageInfo(reqStorageInfo).convertDto();

    }

    /**
     *
     * @methodName : saveReqStorageInfo
     * @date : 2021-06-25 오전 10:47
     * @author : xeroman.k
     * @param reqStorageInfo
     * @return : com.itsm.dranswer.storage.ReqStorageInfo
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfo saveReqStorageInfo(ReqStorageInfo reqStorageInfo) {
        return reqStorageInfoRepo.save(reqStorageInfo);
    }

    /**
     *
     * @methodName : getReqStorageList
     * @date : 2021-06-25 오후 1:49
     * @author : xeroman.k
     * @param reqStorageStatCode
     * @param dataName
     * @param userSeq
     * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<ReqStorageInfoDto> getReqStorageList(ReqStorageStat reqStorageStatCode,
                                                     String dataName, Disease diseaseCode,
                                                     String agencyName, Long userSeq, Pageable pageable) {

        Long inqUserSeq = null;

        if(userSeq != null){
            UserInfoDto userInfoDto = userService.getOriginUser(userSeq);
            inqUserSeq = userInfoDto.getUserSeq();
        }

        return reqStorageInfoRepoSupport.searchAll(reqStorageStatCode, dataName, diseaseCode, agencyName, inqUserSeq, pageable);
    }

    /**
     *
     * @methodName : getReqStorageInfo
     * @date : 2021-06-25 오후 1:59
     * @author : xeroman.k
     * @param reqStorageId
     * @return : com.itsm.dranswer.storage.ReqStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfoDto getReqStorage(String reqStorageId) {

        ReqStorageInfo reqStorageInfo = getReqStorageInfo(reqStorageId);
        UserInfo userInfo = reqStorageInfo.getDiseaseManagerUserInfo();//userService.findUserInfo(reqStorageInfo.getDiseaseManagerUserSeq());
        AgencyInfo agencyInfo = userInfo.getAgencyInfo();
        BucketInfo bucketInfo = reqStorageInfo.getBucketInfo();
        ReqUserDto reqUserDto = userService.getReqStorageUserInfo(reqStorageInfo.getCreatedBy());

        ReqStorageInfoDto reqStorageInfoDto = new ReqStorageInfoDto(reqStorageInfo, userInfo, agencyInfo, bucketInfo, reqUserDto);

        return reqStorageInfoDto;
    }

    /**
     *
     * @methodName : getReqStorageInfo
     * @date : 2021-06-25 오후 3:42
     * @author : xeroman.k
     * @param reqStorageId
     * @return : com.itsm.dranswer.storage.ReqStorageInfo
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfo getReqStorageInfo(String reqStorageId){
        return reqStorageInfoRepo
                .findById(reqStorageId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 저장정보 ID 입니다."));
    }

    /**
     *
     * @methodName : cancelReqStorageInfo
     * @date : 2021-06-25 오후 3:53
     * @author : xeroman.k
     * @param loginUserInfo
     * @param reqStorageId
     * @return : com.itsm.dranswer.storage.ReqStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfoDto cancelReqStorageInfo(LoginUserInfo loginUserInfo, String reqStorageId) {
        ReqStorageInfo reqStorageInfo = getReqStorageInfo(reqStorageId);
        checkMaker(loginUserInfo, reqStorageInfo.getCreatedBy());

        reqStorageInfo.reqCancel();

        return new ReqStorageInfoDto(reqStorageInfo);
    }

    /**
     *
     * @methodName : deleteReqStorageInfo
     * @date : 2021-06-29 오후 4:33
     * @author : xeroman.k
     * @param loginUserInfo
     * @param reqStorageId
     * @param reqStorageInfoDto
     * @return : com.itsm.dranswer.storage.ReqStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfoDto deleteReqStorageInfo(LoginUserInfo loginUserInfo, String reqStorageId, ReqStorageInfoDto reqStorageInfoDto) {
        ReqStorageInfo reqStorageInfo = getReqStorageInfo(reqStorageId);
        checkMaker(loginUserInfo, reqStorageInfo.getCreatedBy());

        reqStorageInfo.reqDelete(reqStorageInfoDto.getDeleteReason());

        return new ReqStorageInfoDto(reqStorageInfo);
    }

    /**
     *
     * @methodName : checkMaker
     * @date : 2021-07-06 오후 4:16
     * @author : xeroman.k
     * @param loginUserInfo
     * @param makerId
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void checkMaker(LoginUserInfo loginUserInfo, Long makerId){
        if(!loginUserInfo.checkCreateUser(makerId)){
            throw new IllegalArgumentException("타인의 정보 입니다.");
        }
    }

    /**
     *
     * @methodName : approveReqStorageInfo
     * @date : 2021-06-28 오후 3:40
     * @author : xeroman.k
     * @param reqStorageId
     * @param bucketInfoDto
     * @return : com.itsm.dranswer.storage.ReqStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfoDto approveReqStorageInfo(String reqStorageId, BucketInfoDto bucketInfoDto) throws MessagingException, IOException {

        ReqStorageInfo reqStorageInfo = getReqStorageInfo(reqStorageId);
        UserInfo managerInfo = reqStorageInfo.getDiseaseManagerUserInfo();
        BucketInfo bucketInfo = makeBucketInfo(reqStorageInfo, bucketInfoDto);
        makeBucket(bucketInfo.getBucketName());
        setBucketACL(bucketInfo.getBucketName(), managerInfo.getNCloudObjStorageId());

        reqStorageInfo.approve(bucketInfo);

        String email = managerInfo.getUserEmail();
        String mailsubject = "[닥터앤서]저장신청 승인 안내";
        String title = "저장신청";
        String userName = managerInfo.getUserName();
        String subject = reqStorageInfo.getDataName();
        customMailSender.sendAcceptMail(email, mailsubject, title, userName, subject);

        return reqStorageInfo.convertDto();
    }

    /**
     *
     * @methodName : deleteReqStorageInfo
     * @date : 2021-07-08 오전 10:46
     * @author : xeroman.k
 * @param reqStorageId
     * @return : com.itsm.dranswer.storage.ReqStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfoDto deleteReqStorageInfo(String reqStorageId) throws MessagingException, IOException {

        ReqStorageInfo reqStorageInfo = getReqStorageInfo(reqStorageId);
        reqStorageInfo.delete();

        BucketInfo bucketInfo = reqStorageInfo.getBucketInfo();
        deleteBucket(bucketInfo.getBucketName());

        UserInfo userInfo = reqStorageInfo.getDiseaseManagerUserInfo();

        String email = userInfo.getUserEmail();
        String mailsubject = "[닥터앤서]삭제신청 승인 안내";
        String title = "삭제신청";
        String userName = userInfo.getUserName();
        String subject = reqStorageInfo.getDataName();
        customMailSender.sendAcceptMail(email, mailsubject, title, userName, subject);

        return reqStorageInfo.convertDto();
    }

    /**
     *
     * @methodName : rejectReqStorageInfo
     * @date : 2021-10-08 오후 2:15
     * @author : xeroman.k
     * @param reqStorageId
     * @param reqStorageInfoDto
     * @return : com.itsm.dranswer.storage.ReqStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfoDto rejectReqStorageInfo(String reqStorageId, ReqStorageInfoDto reqStorageInfoDto) throws MessagingException, IOException {
        ReqStorageInfo reqStorageInfo = getReqStorageInfo(reqStorageId);
        reqStorageInfo.reject(reqStorageInfoDto.getRejectReason());

        UserInfo userInfo = reqStorageInfo.getDiseaseManagerUserInfo();
        if(reqStorageInfo.isRejected()){
            String email = userInfo.getUserEmail();
            String mailsubject = "[닥터앤서]저장신청 거절 안내";
            String title = "저장신청";
            String userName = userInfo.getUserName();
            String subject = reqStorageInfo.getDataName();
            String reject = reqStorageInfo.getRejectReason();
            customMailSender.sendRejectMail(email, mailsubject, title, userName, subject, reject);
        }

        if(reqStorageInfo.isDeleteRejected()){
            String email = userInfo.getUserEmail();
            String mailsubject = "[닥터앤서]삭제신청 거절 안내";
            String title = "삭제신청";
            String userName = userInfo.getUserName();
            String subject = reqStorageInfo.getDataName();
            String reject = reqStorageInfo.getRejectReason();
            customMailSender.sendRejectMail(email, mailsubject, title, userName, subject, reject);
            System.out.println("");
        }

        return reqStorageInfo.convertDto();
    }

    /**
     *
     * @methodName : makeBucketInfo
     * @date : 2021-10-08 오후 2:29
     * @author : xeroman.k
     * @param reqStorageInfo
     * @param bucketInfoDto
     * @return : com.itsm.dranswer.storage.BucketInfo
     * @throws
     * @modifyed :
     *
    **/
    public BucketInfo makeBucketInfo(ReqStorageInfo reqStorageInfo, BucketInfoDto bucketInfoDto){
        System.out.println("");
        return bucketInfoRepo.save(new BucketInfo(reqStorageInfo, bucketInfoDto));
    }

    /**
     *
     * @methodName : makeBucket
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param bucketName
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void makeBucket(String bucketName){
        customObjectStorage.makeBucket(endPoint, regionName, laifAccessKey, laifSecretKey, bucketName);
    }

    /**
     *
     * @methodName : setBucketACL
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param bucketName
     * @param ncpObjStorageId
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void setBucketACL(String bucketName, String ncpObjStorageId){
        customObjectStorage.setBucketACL(endPoint, regionName, laifAccessKey, laifSecretKey, bucketName, ncpObjStorageId);
    }

    /**
     *
     * @methodName : deleteBucket
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param bucketName
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void deleteBucket(String bucketName){
        customObjectStorage.deleteBucket(endPoint, regionName, laifAccessKey, laifSecretKey, bucketName);
    }

    /**
     *
     * @methodName : getOpenStorageList
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param openStorageStatCode
     * @param dataName
     * @param userSeq
     * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.storage.OpenStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<OpenStorageInfoDto> getOpenStorageList(OpenStorageStat openStorageStatCode, String dataName, Long userSeq, Pageable pageable) {

        Long inqUserSeq = null;

        if(userSeq != null){
            UserInfoDto userInfoDto = userService.getOriginUser(userSeq);
            inqUserSeq = userInfoDto.getUserSeq();
        }

        return openStorageInfoRepoSupport.searchAll(openStorageStatCode, dataName, inqUserSeq, pageable);
    }

    /**
     *
     * @methodName : getOpenStorageListForUse
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param disease
     * @param keyword
     * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.storage.OpenStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<OpenStorageInfoDto> getOpenStorageListForUse(Disease disease, String keyword, Pageable pageable) {
        return openStorageInfoRepoSupport.searchForUse(disease, keyword, pageable);
    }

    /**
     *
     * @methodName : openStorage
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param openStorageInfoDto
     * @return : com.itsm.dranswer.storage.OpenStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public OpenStorageInfoDto openStorage(OpenStorageInfoDto openStorageInfoDto) {
        OpenStorageInfo openStorageInfo = new OpenStorageInfo(openStorageInfoDto);
        return saveOpenStorageInfo(openStorageInfo).convertDto();
    }

    /**
     *
     * @methodName : saveOpenStorageInfo
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param openStorageInfo
     * @return : com.itsm.dranswer.storage.OpenStorageInfo
     * @throws
     * @modifyed :
     *
    **/
    public OpenStorageInfo saveOpenStorageInfo(OpenStorageInfo openStorageInfo){
        return openStorageInfoRepo.save(openStorageInfo);
    }

    /**
     *
     * @methodName : getOpenStorage
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param openStorageId
     * @return : com.itsm.dranswer.storage.OpenStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public OpenStorageInfoDto getOpenStorage(String openStorageId) {
        OpenStorageInfo openStorageInfo = getOpenStorageInfo(openStorageId);
        ReqStorageInfo reqStorageInfo = openStorageInfo.getReqStorageInfo();
        UserInfo userInfo = openStorageInfo.getDiseaseManagerUserInfo();
        AgencyInfo agencyInfo = userInfo.getAgencyInfo();
        ReqUserDto reqUserDto = userService.getReqStorageUserInfo(openStorageInfo.getCreatedBy());

        OpenStorageInfoDto openStorageInfoDto = new OpenStorageInfoDto(openStorageInfo, reqStorageInfo, userInfo, agencyInfo, reqUserDto);

        return openStorageInfoDto;
    }

    /**
     *
     * @methodName : getOpenStorageInfo
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param openStorageId
     * @return : com.itsm.dranswer.storage.OpenStorageInfo
     * @throws
     * @modifyed :
     *
    **/
    private OpenStorageInfo getOpenStorageInfo(String openStorageId) {
        return openStorageInfoRepo.findById(openStorageId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공개정보 ID 입니다."));
    }

    /**
     *
     * @methodName : cancelOpenStorageInfo
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param loginUserInfo
     * @param openStorageId
     * @param openStorageInfoDto
     * @return : com.itsm.dranswer.storage.OpenStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public OpenStorageInfoDto cancelOpenStorageInfo(LoginUserInfo loginUserInfo, String openStorageId, OpenStorageInfoDto openStorageInfoDto) {
        OpenStorageInfo openStorageInfo = getOpenStorageInfo(openStorageId);
        checkMaker(loginUserInfo, openStorageInfo.getCreatedBy());

        openStorageInfo.reqCancel(openStorageInfoDto.getCancelReason());

        return openStorageInfo.convertDto();
    }

    /**
     *
     * @methodName : approveOpenStorageInfo
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param openStorageId
     * @return : com.itsm.dranswer.storage.OpenStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public OpenStorageInfoDto approveOpenStorageInfo(String openStorageId) throws MessagingException, IOException {
        OpenStorageInfo openStorageInfo = getOpenStorageInfo(openStorageId);
        openStorageInfo.approve();

        if(openStorageInfo.isCanceled()){
            // bucket 권한 차단
        }

        if(openStorageInfo.isApproved()){
            UserInfo userInfo = openStorageInfo.getDiseaseManagerUserInfo();
            String email = userInfo.getUserEmail();
            String mailsubject = "[닥터앤서]공개신청 승인 안내";
            String title = "공개신청";
            String userName = userInfo.getUserName();
            String subject = openStorageInfo.getOpenDataName();
            customMailSender.sendAcceptMail(email, mailsubject, title, userName, subject);
        }

        if(openStorageInfo.isCanceled()){
            UserInfo userInfo = openStorageInfo.getDiseaseManagerUserInfo();
            String email = userInfo.getUserEmail();
            String mailsubject = "[닥터앤서]공개취소신청 승인 안내";
            String title = "공개취소신청";
            String userName = userInfo.getUserName();
            String subject = openStorageInfo.getOpenDataName();
            customMailSender.sendAcceptMail(email, mailsubject, title, userName, subject);
        }

        return openStorageInfo.convertDto();
    }

    /**
     *
     * @methodName : rejectOpenStorageInfo
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param openStorageId
     * @param openStorageInfoDto
     * @return : com.itsm.dranswer.storage.OpenStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public OpenStorageInfoDto rejectOpenStorageInfo(String openStorageId, OpenStorageInfoDto openStorageInfoDto) throws MessagingException, IOException {
        OpenStorageInfo openStorageInfo = getOpenStorageInfo(openStorageId);
        openStorageInfo.reject(openStorageInfoDto.getRejectReason());

        if(openStorageInfo.isRejected()){
            UserInfo userInfo = openStorageInfo.getDiseaseManagerUserInfo();
            String email = userInfo.getUserEmail();
            String mailsubject = "[닥터앤서]공개신청 거절 안내";
            String title = "공개신청";
            String userName = userInfo.getUserName();
            String subject = openStorageInfo.getOpenDataName();
            String reject = openStorageInfo.getRejectReason();
            customMailSender.sendRejectMail(email, mailsubject, title, userName, subject, reject);
        }

        if(openStorageInfo.isCancelRejected()){
            UserInfo userInfo = openStorageInfo.getDiseaseManagerUserInfo();
            String email = userInfo.getUserEmail();
            String mailsubject = "[닥터앤서]공개취소신청 거절 안내";
            String title = "공개취소신청";
            String userName = userInfo.getUserName();
            String subject = openStorageInfo.getOpenDataName();
            String reject = openStorageInfo.getRejectReason();
            customMailSender.sendRejectMail(email, mailsubject, title, userName, subject, reject);
        }

        return openStorageInfo.convertDto();
    }

    /**
     *
     * @methodName : getMyStorageBucketList
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param loginUserInfo
     * @return : java.util.List<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public List<ReqStorageInfoDto> getMyStorageBucketList(LoginUserInfo loginUserInfo) {

        UserInfoDto userInfoDto = userService.getOriginUser(loginUserInfo.getUserSeq());

        String accessKey = userInfoDto.getNCloudAccessKey();
        String secretKey = userInfoDto.getNCloudSecretKey();
        if(userInfoDto.getUserRole().equals(Role.ADMIN)){
            accessKey = laifAccessKey;
            secretKey = laifSecretKey;
        }

        List<ReqStorageInfo> reqStorageInfos = reqStorageInfoRepo.
                findByDiseaseManagerUserSeqAndReqStorageStatCode(userInfoDto.getUserSeq(), ReqStorageStat.S_ACC);

        return reqStorageInfos.stream().map(ReqStorageInfoDto::new).collect(Collectors.toList());
    }

    /**
     *
     * @methodName : getObjectList
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param loginUserInfo
     * @param bucketName
     * @param folderName
     * @return : java.util.List<com.itsm.dranswer.storage.S3ObjectDto>
     * @throws
     * @modifyed :
     *
    **/
    public List<S3ObjectDto> getObjectList(LoginUserInfo loginUserInfo, String bucketName, String folderName) {

        UserInfoDto userInfoDto = userService.getOriginUser(loginUserInfo.getUserSeq());

        String accessKey = userInfoDto.getNCloudAccessKey();
        String secretKey = userInfoDto.getNCloudSecretKey();
        if(userInfoDto.getUserRole().equals(Role.ADMIN)){
            accessKey = laifAccessKey;
            secretKey = laifSecretKey;
        }

        return customObjectStorage.getObjectList(endPoint, regionName, accessKey, secretKey, bucketName, folderName);
    }

    /**
     *
     * @methodName : uploadFile
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param bucketName
     * @param folderName
     * @param multipartFiles
     * @param loginUserInfo
     * @return : com.itsm.dranswer.etc.FileUploadResponse
     * @throws
     * @modifyed :
     *
    **/
    public FileUploadResponse uploadFile(String bucketName, String folderName, List<MultipartFile> multipartFiles, LoginUserInfo loginUserInfo) throws InterruptedException {
        int fileCnt = 0;
        long fileSize = 0L;

        UserInfoDto userInfoDto = userService.getOriginUser(loginUserInfo.getUserSeq());

        List<FileObject> listObject = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles){
            fileCnt++;
            fileSize += multipartFile.getSize();
            File targetFile = new File("/drAnswer/temp/" + multipartFile.getOriginalFilename());
            try {
                InputStream fileStream = multipartFile.getInputStream();
                FileUtils.copyInputStreamToFile(fileStream, targetFile);
            } catch (IOException e) {
                FileUtils.deleteQuietly(targetFile);
                e.printStackTrace();
            }

            String objectName = multipartFile.getOriginalFilename();

//        String accessKey = laifAccessKey;
//        String secretKey = laifSecretKey;
            String accessKey = userInfoDto.getNCloudAccessKey();
            String secretKey = userInfoDto.getNCloudSecretKey();

            String keyName = customObjectStorage.uploadObject(endPoint, regionName,
                    accessKey, secretKey, bucketName, folderName, objectName, targetFile, CannedAccessControlList.Private);

            FileObject fileObject = new FileObject();
            fileObject.setOrgFileName(multipartFile.getOriginalFilename());
            fileObject.setKeyName(keyName);

            listObject.add(fileObject);

            targetFile.delete();
        }

        return new FileUploadResponse(fileCnt, fileSize, listObject);
    }

    /**
     *
     * @methodName : deleteObject
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param bucketName
     * @param objectName
     * @param loginUserInfo
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void deleteObject(String bucketName, String objectName, LoginUserInfo loginUserInfo){

        UserInfoDto userInfoDto = userService.getOriginUser(loginUserInfo.getUserSeq());

//        String accessKey = laifAccessKey;
//        String secretKey = laifSecretKey;
        String accessKey = userInfoDto.getNCloudAccessKey();
        String secretKey = userInfoDto.getNCloudSecretKey();

        customObjectStorage.deleteObject(endPoint, regionName, accessKey, secretKey, bucketName, objectName);

    }

    /**
     *
     * @methodName : deleteObjects
     * @date : 2021-10-08 오후 2:30
     * @author : xeroman.k
     * @param requestObjectDtos
     * @param loginUserInfo
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void deleteObjects(List<RequestObjectDto> requestObjectDtos, LoginUserInfo loginUserInfo) {

        for(RequestObjectDto requestObjectDto : requestObjectDtos){
            deleteObject(requestObjectDto.getBucketName(), requestObjectDto.getObjectName(), loginUserInfo);
        }
    }

    /**
     *
     * @methodName : getReqStorageAuthList
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @param reqStorageId
     * @return : java.util.List<com.itsm.dranswer.users.UserInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public List<UserInfoDto> getReqStorageAuthList(String reqStorageId) {
        ReqStorageInfoDto reqStorageInfoDto = this.getReqStorage(reqStorageId);
        if(reqStorageInfoDto.getDiseaseManagerUserSeq() == null){
            return new ArrayList<>();
        }
        List<UserInfoDto> listUserInfoDto = userService.getOnlyMyUploader(reqStorageInfoDto.getDiseaseManagerUserSeq());
        listUserInfoDto.add(0, reqStorageInfoDto.getDiseaseManagerUserInfo());
        return listUserInfoDto;
    }

    /**
     *
     * @methodName : uploadBoardFile
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @param folderName
     * @param multipartFiles
     * @return : com.itsm.dranswer.etc.FileUploadResponse
     * @throws
     * @modifyed :
     *
    **/
    public FileUploadResponse uploadBoardFile(String folderName, List<MultipartFile> multipartFiles) throws InterruptedException {
        int fileCnt = 0;
        long fileSize = 0L;

        String bucketName = "dranswer-upload-files";
        List<FileObject> listObject = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles){
            fileCnt++;
            fileSize += multipartFile.getSize();
            File targetFile = new File("/drAnswer/temp/" + multipartFile.getOriginalFilename());
            try {
                InputStream fileStream = multipartFile.getInputStream();
                FileUtils.copyInputStreamToFile(fileStream, targetFile);
            } catch (IOException e) {
                FileUtils.deleteQuietly(targetFile);
                e.printStackTrace();
            }

            String objectName = new Date().getTime() + "_" + multipartFile.getOriginalFilename();

            String accessKey = laifServerAccessKey;
            String secretKey = laifServerSecretKey;

            String keyName = customObjectStorage.uploadObject(endPoint, regionName, accessKey,
                    secretKey, bucketName, folderName, objectName, targetFile, CannedAccessControlList.PublicRead);

            FileObject fileObject = new FileObject();
            fileObject.setOrgFileName(multipartFile.getOriginalFilename());
            fileObject.setKeyName(keyName);

            listObject.add(fileObject);

            targetFile.delete();
        }

        return new FileUploadResponse(fileCnt, fileSize, listObject);
    }

    /**
     *
     * @methodName : bucketSize
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @return : java.util.List<com.itsm.dranswer.storage.BucketInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public List<BucketInfoDto> bucketSize() {

        List<BucketInfo> buckets = bucketInfoRepo.findAll();

        for(BucketInfo bucketInfo : buckets){

            try {
                long size = customObjectStorage.getSize(endPoint, regionName, laifAccessKey, laifSecretKey, bucketInfo.getBucketName());
                bucketInfo.setBucketSize(size);
            }catch (AmazonS3Exception e){
                e.printStackTrace();
            }

        }

        return buckets.stream().map(BucketInfoDto::new).collect(Collectors.toList());
    }

    /**
     *
     * @methodName : getStorageUsedList
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @param agencySeq
     * @param diseaseCode
     * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.storage.ReqStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<ReqStorageInfoDto> getStorageUsedList(Integer agencySeq, Disease diseaseCode, Pageable pageable){

        return reqStorageInfoRepoSupport.getUsedSize(agencySeq, diseaseCode, pageable);

    }

    /**
     *
     * @methodName : getStorageUsedSummary
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @return : java.util.List<com.itsm.dranswer.storage.StorageSummaryDto>
     * @throws
     * @modifyed :
     *
    **/
    public List<StorageSummaryDto> getStorageUsedSummary() {

        List<StorageSummaryDto> list = reqStorageInfoRepoSupport.getUsedSummary();

        for(Disease c: Disease.values()){
            if(!list.stream().anyMatch(e->e.getDiseaseCode().equals(c))){
                list.add(new StorageSummaryDto(c, 0L));
            }
        }

        return list;
    }

    /**
     *
     * @methodName : reqUseStorage
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @param loginUserInfo
     * @param reqUseStorageInfoDto
     * @return : com.itsm.dranswer.storage.UseStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UseStorageInfoDto reqUseStorage(LoginUserInfo loginUserInfo, UseStorageInfoDto reqUseStorageInfoDto) {

        UseStorageInfo useStorageInfo = new UseStorageInfo(reqUseStorageInfoDto, loginUserInfo.getUserSeq());
        useStorageInfo = useStorageInfoRepo.save(useStorageInfo);

        return useStorageInfo.convertDto();

    }

    /**
     *
     * @methodName : getUseStorageList
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @param useStorageStat
     * @param dataName
     * @param userSeq
     * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.storage.UseStorageInfoDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<UseStorageInfoDto> getUseStorageList(UseStorageStat useStorageStat, String dataName, Long reqUserSeq, Long managerUserSeq, Pageable pageable) {

        return useStorageInfoRepoSupport.searchMyList(useStorageStat, dataName, reqUserSeq, managerUserSeq, pageable);
    }

    /**
     *
     * @methodName : getUseStorageInfo
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @param useStorageId
     * @return : com.itsm.dranswer.storage.UseStorageInfo
     * @throws
     * @modifyed :
     *
    **/
    private UseStorageInfo getUseStorageInfo(String useStorageId){
        return useStorageInfoRepo.findById(useStorageId).orElseThrow(() ->
                new NotFoundException("존재하지 않는 정보 입니다.")
        );
    }

    /**
     *
     * @methodName : getUseStorage
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @param useStorageId
     * @return : com.itsm.dranswer.storage.UseStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UseStorageInfoDto getUseStorage(String useStorageId) {

        UseStorageInfo useStorageInfo = getUseStorageInfo(useStorageId);

        UseStorageInfoDto useStorageInfoDto = new UseStorageInfoDto(useStorageInfo,
                useStorageInfo.getOpenStorageInfo(), useStorageInfo.getOpenStorageInfo().getDiseaseManagerUserInfo(),
                useStorageInfo.getOpenStorageInfo().getDiseaseManagerUserInfo().getAgencyInfo());

        return useStorageInfoDto;

    }

    /**
     *
     * @methodName : cancelUseStorage
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @param useStorageId
     * @param cancelMsg
     * @param loginUserInfo
     * @return : com.itsm.dranswer.storage.UseStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UseStorageInfoDto cancelUseStorage(String useStorageId, String cancelMsg, LoginUserInfo loginUserInfo) {
        UseStorageInfo useStorageInfo = getUseStorageInfo(useStorageId);
        useStorageInfo.checkUser(loginUserInfo.getUserSeq());
        useStorageInfo.reqCancel(cancelMsg);

        return useStorageInfo.convertDto();
    }

    /**
     *
     * @methodName : rejectUseStorage
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @param useStorageId
     * @param rejectMsg
     * @return : com.itsm.dranswer.storage.UseStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UseStorageInfoDto rejectUseStorage(String useStorageId, String rejectMsg) {
        UseStorageInfo useStorageInfo = getUseStorageInfo(useStorageId);

        useStorageInfo.reject(rejectMsg);

        return useStorageInfo.convertDto();
    }

    /**
     *
     * @methodName : approveUseStorage
     * @date : 2021-10-08 오후 2:31
     * @author : xeroman.k
     * @param useStorageId
     * @return : com.itsm.dranswer.storage.UseStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UseStorageInfoDto approveUseStorage(String useStorageId) {
        UseStorageInfo useStorageInfo = getUseStorageInfo(useStorageId);
        useStorageInfo.approve();

        return useStorageInfo.convertDto();
    }

    /**
     *
     * @methodName : deleteUseStorage
     * @date : 2021-10-08 오후 2:46
     * @author : xeroman.k
 * @param useStorageId
     * @return : com.itsm.dranswer.storage.UseStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public UseStorageInfoDto deleteUseStorage(String useStorageId) {
        UseStorageInfo useStorageInfo = getUseStorageInfo(useStorageId);
        useStorageInfo.delete();

        return useStorageInfo.convertDto();
    }
}
