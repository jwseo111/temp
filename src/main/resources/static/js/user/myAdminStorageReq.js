
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
                alert("저장소설명은 필수입니다.");
                return false;
            }
            if(confirm("승인하시겠습니까?")) {
                post(TID.APPROVE,
                    "/management/storage/req/approve/" + this.reqStorageId,
                    this.saveInfo,
                    this.callback);
            }

        },
        // 거절버튼 클릭
        onclickReject: function () {
            let uri = "/management/storage/req/reject/"; // 호출할 uri

            if(!this.saveInfo.rejectReason) {
                alert("거절사유는 필수입니다.");
                return;
            }
            if(confirm("거절하시겠습니까?")) {
                post(TID.REJECT,
                    uri + this.reqStorageId,
                    this.saveInfo,
                    this.callback);
            }
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
                    console.log(results.response);
                    this.isYn = results.response;
                    break;
                case "Disease":
                    this.diseaseCdList = results.response;
                    break;
                case TID.APPROVE: // 승인처리
                    console.log(results);
                    if (results.success) {
                        alert("정상적으로 승인처리되었습니다.");
                        this.onclickList();// 목록으로 이동
                    } else {
                        console.log(results);
                    }
                    break;
                case TID.REJECT: // 거절처리
                    if (results.success) {
                        alert("정상적으로 거절처리되었습니다.");
                        this.onclickList();// 목록으로 이동
                    } else {
                        console.log(results);
                    }
                    break;

            }
        },
        searchCallback: function (results) {
            if (results.success) {
                //console.log(JSON.stringify(results.response));
                console.log(results.response);
                this.reqUserInfo = results.response.reqUserDto;
                this.reqStorageInfo   = results.response;
            } else {
                console.log(results);
            }
        }
    }
});

