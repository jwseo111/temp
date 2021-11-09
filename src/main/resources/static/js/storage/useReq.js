
/*
 * @name : useReq.js
 * @date : 2021-07-28 오후 4:48
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH : {value: 0, name: "search", code: "S"},
    SEARCH_USER : {value: 0, name: "searchUser", code: "S"},
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
            openStorageId : openStorageId,
            openStorageList: [], // 공개데이터 리스트
            openStorageInfo : { // 공개데이터 정보
                agencyInfo:{},
                diseaseManagerUserInfo:{
                    diseaseCode:{}
                },
            },
            userInfo : {
                agencyInfo:{}
            },
            messages : "",
            reqStoreStatCd : "",
            saveInfo: {
                reqOpenId:openStorageId,
                usingDays : "0"    // 사용일수(0:무기한)
            },
        };
    },
    mounted:function(){
        this.getUserInfo();
        this.getUseStorageList();
    },

    methods:{
        // 신청화면에 출력될 신청자 정보
        getUserInfo: function(){
            get("usrInfo","/user/my/info",{},this.callback);
        },
        // 공개신청 상세조회
        getUseStorageList:function () {
            get(TID.SEARCH,
                "/storage/use/list",
                this.cond,
                this.callback);
        },
        // 취소 클릭(목록 이동)
        onclickCancel: function () {
            location.href = "/lndata/use/main";
        },
        // 신청 클릭(신청)
        onclickReqSave: function () {
            this.saveReqStorage();
        },
        // 신청 메소드 호출
        saveReqStorage:function () {
            console.log("신청 : " + JSON.stringify(this.saveInfo));
            // 2021.11.09 고객요청으로 임시 주석 처리
            // if(isNull(this.saveInfo.usingDays)){
            //     alertMsg("사용일수는 필수입니다.",this.$refs.usingDays);
            //     return false;
            // }

            confirmMsg("신청하시겠습니까?",this.save);
        },
        save: function() {
                post(TID.SAVE,
                    "/storage/use/req",
                    this.saveInfo,
                    this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case "usrInfo":
                    console.log(results);
                    if (results.success) {
                        this.userInfo = results.response;
                    } else {
                        console.log(results);
                        //alertMsg(results.error.message);
                    }
                    break;
                case TID.SAVE:
                    console.log(results);
                    this.saveCallback(results);
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                // console.log(results.response.content);
                this.openStorageList = results.response.content;
                let id = this.openStorageId;
                let idx = this.openStorageList.findIndex(function(key){return key.reqOpenId === id});
                this.openStorageInfo = this.openStorageList[idx];
            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
        saveCallback: function (results) {
            if (results.success) {
                alertMsgRtn("정상적으로 신청되었습니다.", this.onclickCancel);
            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
    }
});

