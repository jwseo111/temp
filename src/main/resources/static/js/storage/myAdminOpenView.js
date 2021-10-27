
/*
 * @name : myOpenReq.js
 * @date : 2021-07-07 오후 1:59
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH : {value: 0, name: "search", code: "S"},
    CANCEL : {value: 0, name: "cancel",code: "D"},  // 취소
    APPR :{value: 0, name: "appr",code: "A"}  // 승인
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
            userInfo : [],
            diseaseInfo : [],
            messages : "",
            reqStoreStatCd : "",
            //diseaseCdList : getCodeList('Disease',this.callback),
            saveInfo: {},
            openStorageInfo:{
                reqStorageInfo : {
                    diseaseCode :{}
                },
                agencyInfo :{},
                diseaseManagerUserInfo : {},
                openStorageStatCode : {},
                reqUserDto :{
                    diseaseCode : {}
                }
            },

        };
    },
    mounted:function(){
        this.getMyOpenStorageInfo();
    },
    methods: {
        // 마이페이지 > 공개신청 목록 > 상세조회
        getMyOpenStorageInfo: function () {
            get(TID.SEARCH,
                "/my/management/storage/open/" + this.openStorageId,
                {},
                this.callback);
        },
        // 목록버튼 클릭
        onclickList: function () {
            location.href = "/my/admin/open/list?menuId="+myMenuId;
        },
        // 승인버튼
        onclickApprove: function (){
            confirmMsg("승인하시겠습니까?", this.apprSave);
        },
        apprSave: function(){
            post(TID.APPR,
                "/management/storage/open/approve/"+this.openStorageId,
                null, this.callback);
        },
        // 취소신청버튼 클릭
        onclickCancel: function () {

            if(!this.saveInfo.rejectReason){
                alertMsg("거절사유는 필수입니다.",this.$refs.rejectReason);
                return;
            }
            confirmMsg("거절하시겠습니까?", this.cancelSave);

        },
        cancelSave: function(){
            post(TID.CANCEL,
                "/management/storage/open/reject/" + this.openStorageId,
                this.saveInfo,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case TID.CANCEL: // 거절신청 callback
                    if (results.success) {
                        alertMsgRtn("정상적으로 거절되었습니다.", this.onclickList);
                    } else {
                        alertMsg(results.error.message);
                    }
                    break;
                case TID.APPR: // 승인신청 callback
                    if (results.success) {
                        alertMsgRtn("정상적으로 승인되었습니다.", this.onclickList);
                    } else {
                        alertMsg(results.error.message);
                    }
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                this.openStorageInfo = results.response;
            } else {
                alertMsg(results.error.message,this.onclickList);
            }
        },

    }
});

