
/*
 * @name : storageReq.js
 * @date : 2021-06-23 오후 1:05
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH_USER : {value: 0, name: "search", code: "S"},
    SAVE        : {value: 0, name: "save", code: "I"}
};
window.onload = function(){
    appMain = new Vue({
        el: '#maincontentswrap',
    });
}

Vue.component('maincontents', {
    template : "#main-template",
    data: function() {
        return {
            cond: {
                page: 0,
                size: 5,
                dataName: "",
                reqStorageStatCode: reqStorageStatCode,
                sort: ""
            },
            reqStorageList: [],
            userInfo : [],
            diseaseInfo : [],
            pageInfo: {
                curr : 1,
                max : 1,
                first : 1,
                last : 1,
                prev : 1,
                next : 1,
                pages: [1]
            },
            messages : "",
            reqStoreStatCd : "",
            reqStoreStatCdList : getCodeList('ReqStorageStat',this.callback),
            isYn : getCodeList('IsYn',this.callback),
            saveInfo: {
                dataName : "",
                diseaseCode : "",
                diseaseManagerUserSeq : ""
            },
            reqStorageInfoDto : {
                dataName : "",
                diseaseCode : "",
                diseaseManagerUserSeq : ""
            }

        };
    },
    mounted:function(){
        /*
        get(TID.SEARCH,
            "/user/req/storage",
            this.cond,
            this.callback);
         */
        this.getReqUser();
    },
    methods:{
        onclickSearch: function () {
            this.cond.page = 0;
            this.cond.reqStorageStatCode = this.selected;
            this.getReqStorageList();
        },


        // 신청화면에 출력될 데이타 조회
        getReqUser:function (){
            get(TID.SEARCH_USER,
                "/user/req/storage",
                this.cond,
                this.callback);
        },
        // 취소 클릭(목록 이동)
        onclickCancel: function () {
            location.href = "/lndata/store/main";
        },
        // 신청 클릭(신청)
        onclickReqSave: function () {

            console.log(("신청 클릭"));//tmp
            this.saveReqStorage();
        },
        /*
        getReqStorageList:function () {
            get(TID.SEARCH,
                "/storage/req/list",
                this.cond,
                this.callback);
        },
         */
        saveReqStorage:function () {
            console.log(("신청 메소드 호출"));//tmp
            //this.saveInfo.dataName = this.saveInfo.dataName;
            this.saveInfo.diseaseCode = this.userInfo.diseaseCode.descEng;
            this.saveInfo.diseaseManagerUserSeq = this.userInfo.managerUserSeq;
            console.log("저장데이터명 : " + this.saveInfo.dataName);
            console.log("질병코드  : "   + this.saveInfo.diseaseCode);
            console.log("질병책임자   : " + this.saveInfo.diseaseManagerUserSeq);
            post(TID.SAVE,
                "/storage/req",
                this.saveInfo,
                this.callback);

        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH_USER:
                    this.searchCallback(results);
                    break;
                case "ReqStorageStat":
                    console.log(results.response);
                    this.reqStoreStatCdList = results.response;
                    break;
                case "IsYn":
                    console.log(results.response);
                    this.isYn = results.response;
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                this.userInfo      = results.response;
                this.diseaseInfo   = results.response.diseaseCode; // 관리하는 질병
            } else {
                console.log(results);
            }
        },
    }
});
