
/*
 * @name : myUseView.js
 * @date : 2021-10-18 오후 1:59
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH : {value: 0, name: "search", code: "S"},
    CANCEL : {value: 0, name: "cancel",code: "D"},  // 취소
    REJECT :{value: 0, name: "reject",code: "R"},  // 거절
    APPROVE :{value: 0, name: "approve",code: "A"}  // 승인
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
            useStorageId : useStorageId,
            useStorageStatCode:"", // 사용신청 처리상태
            saveInfo: {
            },
            useStorageInfo:{
                openStorageInfo:{},
                agencyInfo:{},
                hUserInfo:{
                    diseaseCode:{},
                    agencyInfo:{},
                },
                cUserInfo:{
                    agencyInfo:{},
                },
                useStorageStatCode:{},
            },
        };
    },
    mounted:function(){
        this.getMyUseStorageInfo();
    },
    methods: {
        // 마이페이지 > 사용신청 목록 > 상세조회
        getMyUseStorageInfo: function () {
            get(TID.SEARCH,
                "/my/management/storage/use/" + this.useStorageId,
                {},
                this.callback);
        },
        // 목록버튼 클릭
        onclickList: function () {
            location.href = "/my/use/list?menuId="+myMenuId;
        },
        // 취소신청 이밴트
        onclickCancel: function () {
            if(isNull(this.saveInfo.cancelReason)){
                alertMsg("취소사유는 필수입니다.",this.$refs.cancelReason);
                return;
            }
            //console.log(JSON.stringify(this.saveInfo));
            confirmMsg("취소신청 하시겠습니까?", this.cancel);
        },
        cancel: function(){
            post(TID.CANCEL,
                "/my/management/storage/use/cancel/" + this.useStorageId,
                this.saveInfo,
                this.callback);
        },
        // 거절 이벤트
        onclickReject: function() {
            if(isNull(this.saveInfo.rejectReason)){
                alertMsg("거절 사유는 필수입니다.",this.$refs.rejectReason);
                return;
            }
            //console.log(JSON.stringify(this.saveInfo));
            confirmMsg("거절 하시겠습니까?", this.reject);
        },
        reject: function(){
            post(TID.REJECT,
                "/management/storage/use/reject/" + this.useStorageId,
                this.saveInfo,
                this.callback);
        },
        // 승인 이벤트
        onclickApprove: function() {
            //console.log(JSON.stringify(this.saveInfo));
            confirmMsg("승인 하시겠습니까?", this.approve);
        },
        approve: function(){
            post(TID.APPROVE,
                "/management/storage/use/approve/" + this.useStorageId,
                this.saveInfo,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case TID.CANCEL: // 취소신청 callback
                    if (results.success) {
                        alertMsgRtn("정상적으로 취소신청되었습니다.", this.onclickList);
                    } else {
                        alertMsg(results.error.message);
                    }
                    break;
                case TID.REJECT: // 거절 callback
                    if (results.success) {
                        alertMsgRtn("정상적으로 거절 처리되었습니다.", this.onclickList);
                    } else {
                        alertMsg(results.error.message);
                    }
                    break;
                case TID.APPROVE: // 승인 처리callback
                    if (results.success) {
                        alertMsgRtn("정상적으로 승인 처리되었습니다.", this.onclickList);
                    } else {
                        alertMsg(results.error.message);
                    }
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                //console.log(results.response);
                this.useStorageInfo = results.response;
                this.useStorageStatCode = results.response.useStorageStatCode.name;
            } else {
                alertMsg(results.error.message);
            }
        },
        isNullText:function (text) {
            if(isNull(text)){
                text = "-";
            }
            return text;
        }
    }
});

