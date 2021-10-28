
/*
 * @name : storageReqAdmin.js
 * @date : 2021-07-14 오후 1:23
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH : {value: 0, name: "search", code: "S"}, // 상세조회
    APPROVE :  {value: 0, name: "approve", code: "A"},  // 승인
    REJECT :  {value: 0, name: "reject"}  // 거절
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
                reqStorageId : reqStorageId
            },
            reqStorageId : reqStorageId,
            messages : "",
            reqStoreStatCd : "",

            reqStoreStatCdList  : getCodeList('ReqStorageStat',this.callback), // 처리상태
            isYn                : getCodeList('IsYn',this.callback), // 질변주관병원여부
            diseaseCdList       : getCodeList('Disease',this.callback), // 관리하는 질병코드

            saveInfo: {
            },
            reqStorageInfo : {
                reqStorageStatCode:{},
                bucketInfo:{}
            },
            reqUserInfo:{
                diseaseCode:{}
            },

        };
    },
    mounted:function(){
        this.getMyReqStorageInfo();
    },
    methods:{
        // 마이페이지 > 스토리지저장신청목록 > 상세 조회
        getMyReqStorageInfo:function (){
            get(TID.SEARCH,
                "/my/management/storage/req/"+this.reqStorageId,
                {},
                this.callback);
        },
        // 목록버튼 클릭
        onclickList: function () {
            location.href = "/my/admin/store/list?menuId=1";
        },
        // 승인버튼 클릭
        onclickApprove: function () {
            if(!this.saveInfo.bucketDesc){
                alertMsg("저장소설명은 필수입니다.", this.$refs.bucketDesc);
                return false;
            }
            confirmMsg("승인하시겠습니까?", this.approve);
        },
        approve: function() {
            post(TID.APPROVE,
                "/management/storage/req/approve/" + this.reqStorageId,
                this.saveInfo,
                this.callback);
        },
        // 거절버튼 클릭
        onclickReject: function () {
            let statCd = this.reqStorageInfo.reqStorageStatCode.name;

            if(!this.saveInfo.rejectReason) {
                alertMsg("거절사유는 필수입니다.", this.$refs.rejectReason);
                return;
            }
            if(statCd == "S_REQ") {
                confirmMsg("거절하시겠습니까?", this.reject);
            }

            if(statCd == "D_REQ") {
                confirmMsg("삭제신청 거절하시겠습니까?", this.reject);
            }
        },
        reject: function() {
            post(TID.REJECT,
                "/management/storage/req/reject/" + this.reqStorageId,
                this.saveInfo,
                this.callback);
        },
      // 삭제신청 승인버튼 클릭
        onclickDelApprove: function () {
            confirmMsg("삭제신청 승인하시겠습니까?", this.delApprove);
        },
        delApprove: function() {
            post(TID.APPROVE,
                "/management/storage/req/delete/" + this.reqStorageId,
                {},
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH: // 상세조회
                    this.searchCallback(results);
                    break;
                case "ReqStorageStat":
                    //console.log(results.response);
                    this.reqStoreStatCdList = results.response;
                    break;
                case "IsYn":
                    this.isYn = results.response;
                    break;
                case "Disease":
                    this.diseaseCdList = results.response;
                    break;
                case TID.APPROVE: // 승인처리
                    //console.log(results);
                    if (results.success) {
                        // alert("정상적으로 승인처리되었습니다.");
                        // this.onclickList();// 목록으로 이동
                        alertMsgRtn("정상적으로 승인처리되었습니다.",this.onclickList);
                    } else {
                        console.log(results);
                        alertMsgRtn("에러 :\n"+results.error.message);
                    }
                    break;
                case TID.REJECT: // 거절처리
                    if (results.success) {
                        // alert("정상적으로 거절처리되었습니다.");
                        // this.onclickList();// 목록으로 이동
                        alertMsgRtn("정상적으로 거절처리되었습니다.",this.onclickList);
                    } else {
                        console.log(results);
                        alertMsgRtn("에러 :\n"+results.error.message);
                    }
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                //console.log(JSON.stringify(results.response));
                //console.log(results.response);
                this.reqUserInfo = results.response.reqUserDto;
                this.reqStorageInfo   = results.response;
            } else {
                console.log(results);
                alertMsgRtn("에러 :\n"+results.error.message, this.onclickList);
            }
        }
    }
});

