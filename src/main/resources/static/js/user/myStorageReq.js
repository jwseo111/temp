
/*
 * @name : storageReq.js
 * @date : 2021-06-23 오후 1:05
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH : {value: 0, name: "search", code: "S"}, // 상세조회
    CANCEL : {value: 0, name: "cancel",code: "D"}  // 취소
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
                reqStorageStatCode:{}
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
            location.href = "/my/store/list";
        },

        // 취소신청버튼 클릭
        onclickCancel: function () {
            let statCd = this.reqStorageInfo.reqStorageStatCode.name;
            let uri = "/my/management/storage/req/cancel/"; // 호출할 uri

            if(statCd == "S_ACC") { // 처리상태가 저장신청승인(S_ACC)이면 취소사유 필수
                if(!this.saveInfo.deleteReason) {
                    alert("저장신청승인 상태인 경우, 취소사유는 필수입니다.");
                    return;
                }
                if(!confirm("저장소 데이터가 삭제되고, 공개 대상에서 제외됩니다.\n취소하시겠습니까?")) {
                    return;
                }
                uri = "/my/management/storage/req/delete/";
            }

            post(TID.CANCEL,
                uri + this.reqStorageId,
                this.saveInfo,
                this.callback);

        },

        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH: // 상세조회
                    this.searchCallback(results);
                    break;
                case "ReqStorageStat":
                    console.log(results.response);
                    this.reqStoreStatCdList = results.response;
                    break;
                case "IsYn":
                    //console.log(results.response);
                    this.isYn = results.response;
                    break;
                case "Disease":
                    this.diseaseCdList = results.response;
                    break;
                case TID.APPROVE: // 승인처리
                    //console.log(results);
                    if (results.success) {
                        alert("정상적으로 승인처리되었습니다.");
                        location.href = "/my/store/main"; // 목록으로 이동
                    } else {
                        console.log(results);
                        alert("에러 :\n"+results.error.message);
                    }
                    break;
                case TID.CANCEL: // 취소신청
                    if (results.success) {
                        alert("정상적으로 취소신청되었습니다.");
                        location.href = "/my/store/list"; // 목록으로 이동
                    } else {
                        console.log(results);
                        alert("에러 :\n"+results.error.message);
                    }
                    break;

            }
        },
        searchCallback: function (results) {
            if (results.success) {
                //console.log(results.response);
                this.reqUserInfo = results.response.reqUserDto;
                this.reqStorageInfo   = results.response;
            } else {
                console.log(results);
                alert("에러 :\n"+results.error.message);
            }
        }
    }
});

