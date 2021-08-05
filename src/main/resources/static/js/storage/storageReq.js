
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
            reqStorageList: [],
            userInfo : [],
            diseaseInfo : [],
            messages : "",
            reqStoreStatCd : "",
            diseaseCdList : getCodeList('Disease',this.callback),
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
        this.getReqUser();
    },
    methods:{
        // 신청화면에 출력될 데이타 조회
        getReqUser:function (){
            get(TID.SEARCH_USER,
                "/user/req/storage",
                {},
                this.callback);
        },
        // 취소 클릭(목록 이동)
        onclickCancel: function () {
            location.href = "/lndata/store/main";
        },
        // 신청 클릭(신청)
        onclickReqSave: function () {
            this.saveReqStorage();
        },
        // 신청 메소드 호출
        saveReqStorage:function () {
            if(!this.saveInfo.dataName){
                alertMsg("저장데이터명은 필수입니다.", dataName );
                return false;
            }
            this.saveInfo.diseaseManagerUserSeq = this.userInfo.managerUserSeq;
            let userDiseaseCode = this.userInfo.diseaseCode.desc;
            let idx = this.diseaseCdList.findIndex(function(key) {return key.desc === userDiseaseCode});
            this.saveInfo.diseaseCode = this.diseaseCdList[idx].name;

            //console.log("저장데이터명 : " + this.saveInfo.dataName);
            //console.log("질병코드  : "   + this.saveInfo.diseaseCode);
            //console.log("질병책임자   : " + this.saveInfo.diseaseManagerUserSeq);

            confirmMsg("신청하시겠습니까?",this.save);
        },
        save: function() {
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
                case "Disease":
                    this.diseaseCdList = results.response;
                    break;
                case TID.SAVE:
                    //console.log(results);
                    this.saveCallback(results);
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                this.userInfo      = results.response;
                this.diseaseInfo   = results.response.diseaseCode; // 관리하는 질병
            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
        saveCallback: function (results) {
            if (results.success) {
                alertMsg("정상적으로 신청되었습니다.");
                location.href = "/lndata/store/main";
            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
    }
});

