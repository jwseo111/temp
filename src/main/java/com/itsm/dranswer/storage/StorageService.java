package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : StorageService.java
 * @date : 2021-06-24 오후 2:34
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */


import com.itsm.dranswer.commons.Disease;
import com.itsm.dranswer.config.CustomMailSender;
import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.etc.FileUploadResponse;
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
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StorageService {

    private final UserService userService;

    private final ReqStorageInfoRepo reqStorageInfoRepo;

    private final OpenStorageInfoRepo openStorageInfoRepo;

    private final ReqStorageInfoRepoSupport reqStorageInfoRepoSupport;

    private final OpenStorageInfoRepoSupport openStorageInfoRepoSupport;

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

    @Autowired
    public StorageService(UserService userService, ReqStorageInfoRepo reqStorageInfoRepo, OpenStorageInfoRepo openStorageInfoRepo, ReqStorageInfoRepoSupport reqStorageInfoRepoSupport, OpenStorageInfoRepoSupport openStorageInfoRepoSupport, CustomObjectStorage customObjectStorage, BucketInfoRepo bucketInfoRepo, CustomMailSender customMailSender) {
        this.userService = userService;
        this.reqStorageInfoRepo = reqStorageInfoRepo;
        this.openStorageInfoRepo = openStorageInfoRepo;
        this.reqStorageInfoRepoSupport = reqStorageInfoRepoSupport;
        this.openStorageInfoRepoSupport = openStorageInfoRepoSupport;
        this.customObjectStorage = customObjectStorage;
        this.bucketInfoRepo = bucketInfoRepo;
        this.customMailSender = customMailSender;
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
        UserInfoDto userInfoDto = userService.getOriginUser(userSeq);

        return reqStorageInfoRepoSupport.searchAll(reqStorageStatCode, dataName, diseaseCode, agencyName, userInfoDto.getUserSeq(), pageable);
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
        }

        return reqStorageInfo.convertDto();
    }

    public BucketInfo makeBucketInfo(ReqStorageInfo reqStorageInfo, BucketInfoDto bucketInfoDto){
        return bucketInfoRepo.save(new BucketInfo(reqStorageInfo, bucketInfoDto));
    }

    public void makeBucket(String bucketName){
        customObjectStorage.makeBucket(endPoint, regionName, laifAccessKey, laifSecretKey, bucketName);
    }

    public void setBucketACL(String bucketName, String ncpObjStorageId){
        customObjectStorage.setBucketACL(endPoint, regionName, laifAccessKey, laifSecretKey, bucketName, ncpObjStorageId);
    }

    public void deleteBucket(String bucketName){
        customObjectStorage.deleteBucket(endPoint, regionName, laifAccessKey, laifSecretKey, bucketName);
    }

    public Page<OpenStorageInfoDto> getOpenStorageList(OpenStorageStat openStorageStatCode, String dataName, Long userSeq, Pageable pageable) {
        UserInfoDto userInfoDto = userService.getOriginUser(userSeq);

        return openStorageInfoRepoSupport.searchAll(openStorageStatCode, dataName, userInfoDto.getUserSeq(), pageable);
    }

    public OpenStorageInfoDto openStorage(OpenStorageInfoDto openStorageInfoDto) {
        OpenStorageInfo openStorageInfo = new OpenStorageInfo(openStorageInfoDto);
        return saveOpenStorageInfo(openStorageInfo).convertDto();
    }

    public OpenStorageInfo saveOpenStorageInfo(OpenStorageInfo openStorageInfo){
        return openStorageInfoRepo.save(openStorageInfo);
    }

    public OpenStorageInfoDto getOpenStorage(String openStorageId) {
        OpenStorageInfo openStorageInfo = getOpenStorageInfo(openStorageId);
        ReqStorageInfo reqStorageInfo = openStorageInfo.getReqStorageInfo();
        UserInfo userInfo = openStorageInfo.getDiseaseManagerUserInfo();
        AgencyInfo agencyInfo = userInfo.getAgencyInfo();
        ReqUserDto reqUserDto = userService.getReqStorageUserInfo(openStorageInfo.getCreatedBy());

        OpenStorageInfoDto openStorageInfoDto = new OpenStorageInfoDto(openStorageInfo, reqStorageInfo, userInfo, agencyInfo, reqUserDto);

        return openStorageInfoDto;
    }

    private OpenStorageInfo getOpenStorageInfo(String openStorageId) {
        return openStorageInfoRepo.findById(openStorageId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공개정보 ID 입니다."));
    }

    public OpenStorageInfoDto cancelOpenStorageInfo(LoginUserInfo loginUserInfo, String openStorageId, OpenStorageInfoDto openStorageInfoDto) {
        OpenStorageInfo openStorageInfo = getOpenStorageInfo(openStorageId);
        checkMaker(loginUserInfo, openStorageInfo.getCreatedBy());

        openStorageInfo.reqCancel(openStorageInfoDto.getCancelReason());

        return openStorageInfo.convertDto();
    }

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

    public FileUploadResponse uploadFile(String bucketName, String folderName, List<MultipartFile> multipartFiles, LoginUserInfo loginUserInfo) throws InterruptedException {
        int fileCnt = 0;
        long fileSize = 0L;

        UserInfoDto userInfoDto = userService.getOriginUser(loginUserInfo.getUserSeq());

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

            customObjectStorage.uploadObject(endPoint, regionName, accessKey, secretKey, bucketName, folderName, objectName, targetFile);

            targetFile.delete();
        }

        return new FileUploadResponse(fileCnt, fileSize);
    }

    public void deleteObject(String bucketName, String objectName, LoginUserInfo loginUserInfo){

        UserInfoDto userInfoDto = userService.getOriginUser(loginUserInfo.getUserSeq());

//        String accessKey = laifAccessKey;
//        String secretKey = laifSecretKey;
        String accessKey = userInfoDto.getNCloudAccessKey();
        String secretKey = userInfoDto.getNCloudSecretKey();

        customObjectStorage.deleteObject(endPoint, regionName, accessKey, secretKey, bucketName, objectName);

    }

    public void deleteObjects(List<RequestObjectDto> requestObjectDtos, LoginUserInfo loginUserInfo) {

        for(RequestObjectDto requestObjectDto : requestObjectDtos){
            deleteObject(requestObjectDto.getBucketName(), requestObjectDto.getObjectName(), loginUserInfo);
        }
    }

    public List<UserInfoDto> getReqStorageAuthList(String reqStorageId) {
        ReqStorageInfoDto reqStorageInfoDto = this.getReqStorage(reqStorageId);
        if(reqStorageInfoDto.getDiseaseManagerUserSeq() == null){
            return new ArrayList<>();
        }
        List<UserInfoDto> listUserInfoDto = userService.getOnlyMyUploader(reqStorageInfoDto.getDiseaseManagerUserSeq());
        listUserInfoDto.add(0, reqStorageInfoDto.getDiseaseManagerUserInfo());
        return listUserInfoDto;
    }
}
