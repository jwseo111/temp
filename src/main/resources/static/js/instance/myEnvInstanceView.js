
/*
 * @name : myEvnInstanceView.js
 * @date : 2021-10-14 오전 10:24
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH : {value: 0, name: "search", code: "S"},
    APPROVE : {value: 0, name: "approve", code: "A"},
    CREATE : {value: 0, name: "create", code: "C"},
    REJECT : {value: 0, name: "reject", code: "R"},
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
                reqSeq: reqSeq
            },
            userInfo : {},
            envInstanceInfo:{
                osImageType:{},
                productType:{},
                approveStatus:{},
            },
            approveStatusCode:"" , // 처리상태코드
            saveInfo:{
                rejectReason:"",
            },
        };
    },
    mounted:function(){
        this.getEnvInstanceView();
    },
    methods: {
        // 신청화면에 출력될 데이타 조회
        getEnvInstanceView: function () {
            get(TID.SEARCH,
                "/env/instance/getDetail",
                this.cond,
                this.callback);
        },
        // 인증키 내려받기
        onclickPrivateKey: function(){
            post('getPrivateKey',
                "/management/instance/environment/getPrivateKey/"+this.cond.reqSeq,
                null,
                this.callback);
        },
        // root 비밀번호 찾기
        onclickRootPassword: function(){
            post('getRootPassword',
                "/management/instance/environment/getRootPassword/"+this.cond.reqSeq,
                null,
                this.callback);
        },
        // 목록 클릭(목록 이동)
        onclickList: function () {
            location.href = "/my/envInstance/list?menuId="+myMenuId;
        },
        // 취소신청 이벤트
        onclickCancel: function () {
            if(isNull(this.saveInfo.cancelReason)){
                alertMsg("취소사유는 필수입니다.",this.$refs.cancelReason);
                return;
            }
            //console.log(JSON.stringify(this.saveInfo));
            confirmMsg("취소신청 하시겠습니까?", this.cancel);
        },
        cancel: function(){
        },
        // 승인 이벤트
        onclickApprove: function () {
            //console.log(JSON.stringify(this.saveInfo));
            confirmMsg("승인처리 하시겠습니까?", this.approve);
        },
        approve: function(){
            post(TID.APPROVE,
                "/management/instance/environment/approve/"+this.cond.reqSeq,
                null,
                this.callback);
        },
        // 거절 이벤트
        onclickReject: function () {
            //console.log(JSON.stringify(this.saveInfo));
            if(isNull(this.saveInfo.rejectReason)){
                alertMsg("거절사유는 필수입니다.",this.$refs.rejectReason);
                return;
            }
            confirmMsg("거절처리 하시겠습니까?", this.reject);
        },
        reject: function(){
            post(TID.REJECT,
                "/management/instance/environment/reject/"+this.cond.reqSeq,
                this.saveInfo,
                this.callback);
        },
        // 생성 이벤트
        onclickCreate: function () {
            //console.log(JSON.stringify(this.saveInfo));
            confirmMsg("생성 하시겠습니까?", this.create);
        },
        create: function(){
            post(TID.CREATE,
                "/management/instance/environment/create/"+this.cond.reqSeq,
                null,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case TID.APPROVE:
                    if (results.success) {
                        alertMsgRtn("정상적으로 승인 되었습니다.", this.getEnvInstanceView);
                    } else {
                        alertMsg(results.error.message);
                    }
                    break;
                case TID.REJECT:
                    if (results.success) {
                        alertMsgRtn("정상적으로 거절처리 되었습니다.", this.getEnvInstanceView);
                    } else {
                        alertMsg(results.error.message);
                    }
                    break;
                case TID.CREATE:
                    if (results.success) {
                        alertMsgRtn("정상적으로 생성 되었습니다.", this.getEnvInstanceView);
                    } else {
                        alertMsg(results.error.message);
                    }
                    break;
                case "getRootPassword":
                    if (results.success) {
                        alertMsg(results.response);
                    } else {
                        alertMsg(results.error.message);
                    }
                    break;
                case "getPrivateKey":
                    if (results.success) {
                        // 인증키 다운로드
                        textDownload(this.cond.reqSeq+".pem", results.response);
                    } else {
                        alertMsg(results.error.message);
                    }
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                console.log(results);
                this.envInstanceInfo   = results.response;
                this.approveStatusCode = this.envInstanceInfo.approveStatus.name;
            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
    }
});