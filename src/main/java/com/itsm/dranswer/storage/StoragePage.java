package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : StoragePage.java
 * @date : 2021-06-23 오후 3:32
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.users.AgencyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoragePage {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // 질환데이터 저장신청 리스트
    @GetMapping(value = "/lndata/store/main")
    public String storageList() {

        return "pages/storage/storageList";
    }
    // 질환데이터 저장신청 상세
    @GetMapping(value = "/lndata/store/req")
    public String storageReq() {

        return "pages/storage/storageReq";
    }

    // 질환데이터 공개신청 리스트
    @GetMapping(value = "/lndata/open/main")
    public String openList() {

        return "pages/storage/openList";
    }
    // 질환데이터 공개신청 상세
    @GetMapping(value = "/lndata/open/req")
    public String openReq() {

        return "pages/storage/openReq";
    }

    // 저장소 불러오기 팝업
    @GetMapping(value = "/popup/storage")
    public String popupAgency() {

        return "pages/storage/popStorageSearch";
    }

    // 마이페이지 > 질환데이터저장신청 목록
    @GetMapping(value = "/my/store/list")
    public String myStorageList() {

        return "pages/user/myStorageList";
    }

    // 마이페이지 > 질환데이터저장신청 상세
    @GetMapping(value = "/my/store/req")
    public String myStorageReq() {

        return "pages/user/myStorageReq";
    }

    // 마이페이지 > 질환데이터 공개신청 목록
    @GetMapping(value = "/my/open/list")
    public String myOpenList() {

        return "pages/user/myOpenList";
    }

    // 마이페이지 > 질환데이터 공개신청 상세
    @GetMapping(value = "/my/open/req")
    public String myOpenReq() {

        return "pages/user/myOpenReq";
    }

    // 마이페이지 > 질환데이터저장신청 상세(ADMIN)
    @GetMapping(value = "/my/store/req/admin")
    public String myStorageReqAdmin() {

        return "pages/user/myStorageReqAdmin";
    }

    // 마이페이지 > 질환데이터 업로드
    @GetMapping(value = "/my/diseaseUpload")
    public String myDiseaseUpload() {

        return "pages/user/myDiseaseUpload";
    }


    // 마이페이지 > 질환데이터 공개신청 목록(ADMIN)
    @GetMapping(value = "/my/admin/open/list")
    public String myAdminOpenList() {

        return "pages/storage/myAdminOpenList";
    }


    // 마이페이지 > 질환데이터 공개신청 상세(ADMIN)
    @GetMapping(value = "/my/admin/open/view")
    public String myAdminOpenView() {

        return "pages/storage/myAdminOpenView";
    }

}
