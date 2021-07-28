
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
    methods:{
        // 마이페이지 > 공개신청 목록 > 상세조회
        getMyOpenStorageInfo:function (){
            get(TID.SEARCH,
                "/my/management/storage/open/"+this.openStorageId,
                {},
                this.callback);
        },
        // 목록버튼 클릭
        onclickList: function () {
            location.href = "/my/open/list?menuId="+myMenuId;
        },
        // 취소신청버튼 클릭
        onclickCancel: function () {
            //let statCd = this.openStorageInfo.openStorageStatCode.name; // 처리상태
            if(!this.saveInfo.cancelReason){
                alert("취소사유는 필수입니다.");
                return;
            }
            if(confirm("취소하시겠습니까?")) {
                post(TID.CANCEL,
                    "/my/management/storage/open/cancel/"+this.openStorageId,
                    this.saveInfo,
                    this.callback);
            }
        },

        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case TID.CANCEL: // 취소신청 callback
                    if (results.success) {
                        alert("정상적으로 취소신청되었습니다.");
                        this.onclickList(); // 목록으로 이동
                    } else {
                        console.log(results);
                        alert("에러 :\n"+results.error.message);
                    }
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                console.log(results.response);
                this.openStorageInfo   = results.response;
            } else {
                console.log(results);
                alert("에러 :\n"+results.error.message);
            }
        },

    }
});

