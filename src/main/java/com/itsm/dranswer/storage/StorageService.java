package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : StorageService.java
 * @date : 2021-06-24 오후 2:34
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */


import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.ncp.storage.CustomObjectStorage;
import com.itsm.dranswer.users.ReqUserDto;
import com.itsm.dranswer.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

    private final UserService userService;

    private final ReqStorageInfoRepo reqStorageInfoRepo;

    private final OpenStorageInfoRepo openStorageInfoRepo;

    private final ReqStorageInfoRepoSupport reqStorageInfoRepoSupport;

    private final CustomObjectStorage customObjectStorage;

    private final BucketInfoRepo bucketInfoRepo;

    @Value("${ncp.laif.end-point}")
    private String endPoint;
    @Value("${ncp.laif.region-name}")
    private String regionName;
    @Value("${ncp.laif.access-key}")
    private String laifAccessKey;
    @Value("${ncp.laif.secret-key}")
    private String laifSecretKey;

    @Autowired
    public StorageService(UserService userService, ReqStorageInfoRepo reqStorageInfoRepo, OpenStorageInfoRepo openStorageInfoRepo, ReqStorageInfoRepoSupport reqStorageInfoRepoSupport, CustomObjectStorage customObjectStorage, BucketInfoRepo bucketInfoRepo) {
        this.userService = userService;
        this.reqStorageInfoRepo = reqStorageInfoRepo;
        this.openStorageInfoRepo = openStorageInfoRepo;
        this.reqStorageInfoRepoSupport = reqStorageInfoRepoSupport;
        this.customObjectStorage = customObjectStorage;
        this.bucketInfoRepo = bucketInfoRepo;
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
    public Page<ReqStorageInfoDto> getReqStorageList(ReqStorageStat reqStorageStatCode, String dataName, Long userSeq, Pageable pageable) {
        return reqStorageInfoRepoSupport.searchAll(reqStorageStatCode, dataName, userSeq, pageable);
    }

    /**
     *
     * @methodName : getReqStorageInfo
     * @date : 2021-06-25 오후 1:59
     * @author : xeroman.k
     * @param loginUserInfo
     * @param reqStorageId
     * @return : com.itsm.dranswer.storage.ReqStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfoDto getReqStorage(LoginUserInfo loginUserInfo, String reqStorageId) {

        ReqStorageInfo reqStorageInfo = getReqStorageInfo(reqStorageId);
        ReqUserDto reqUserDto = userService.getReqStorageUserInfo(loginUserInfo.getUserSeq());

        return new ReqStorageInfoDto(reqStorageInfo, reqUserDto);
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
        ReqStorageInfo reqStorageInfo = reqStorageInfoRepo
                .findById(reqStorageId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 저장정보 ID 입니다."));

        return reqStorageInfo;
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
        checkMaker(reqStorageInfo, loginUserInfo);

        reqStorageInfo.reqCancel();
        
        return new ReqStorageInfoDto(reqStorageInfo);
    }

    /**
     *
     * @methodName : checkMaker
     * @date : 2021-06-28 오후 3:40
     * @author : xeroman.k
     * @param reqStorageInfo
     * @param loginUserInfo
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void checkMaker(ReqStorageInfo reqStorageInfo, LoginUserInfo loginUserInfo){
        if(!reqStorageInfo.checkCreateUser(loginUserInfo.getUserSeq())){
            throw new IllegalArgumentException("타인의 정보 입니다.");
        }
    }

    /**
     *
     * @methodName : approveReqStorageInfo
     * @date : 2021-06-28 오후 3:40
     * @author : xeroman.k
     * @param reqStorageId
     * @return : com.itsm.dranswer.storage.ReqStorageInfoDto
     * @throws
     * @modifyed :
     *
    **/
    public ReqStorageInfoDto approveReqStorageInfo(String reqStorageId) {

        ReqStorageInfo reqStorageInfo = getReqStorageInfo(reqStorageId);
        reqStorageInfo.approve();

        BucketInfo bucketInfo = makeBucketInfo(reqStorageInfo);

        makeBucket(bucketInfo);

        return reqStorageInfo.convertDto();
    }

    public BucketInfo makeBucketInfo(ReqStorageInfo reqStorageInfo){
        return bucketInfoRepo.save(new BucketInfo(reqStorageInfo));
    }

    public void makeBucket(BucketInfo bucketInfo){

        customObjectStorage.makeBucket(endPoint, regionName, laifAccessKey, laifSecretKey, bucketInfo.getBucketName());

    }

}
