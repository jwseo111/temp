package com.itsm.dranswer.storage;

import com.itsm.dranswer.commons.Disease;
import com.itsm.dranswer.config.LoginUser;
import com.itsm.dranswer.config.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class UseStorageRestCtrl {

    private final StorageService storageService;

    @Autowired
    public UseStorageRestCtrl(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/storage/use/list")
    public ApiResult<Page<OpenStorageInfoDto>> getUseStorageList(
            Disease disease, String keyword, Pageable pageable){

        Page<OpenStorageInfoDto> pageOpenStorages =
                storageService.getOpenStorageListForUse(disease, keyword, pageable);

        return success(pageOpenStorages);
    }

    @PostMapping(value = "/storage/use/req")
    public ApiResult<UseStorageInfoDto> reqUseStorage(
            @LoginUser LoginUserInfo loginUserInfo, @RequestBody UseStorageInfoDto reqUseStorageInfoDto
            ){

        UseStorageInfoDto useStorageInfoDto =
                storageService.reqUseStorage(loginUserInfo, reqUseStorageInfoDto);

        return success(useStorageInfoDto);
    }
}
