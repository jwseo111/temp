
/*
 * @name : openReq.js
 * @date : 2021-07-02 오후 1:05
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
            //reqStorageList: [],
            userInfo : [],
            diseaseInfo : [],
            messages : "",
            reqStoreStatCd : "",
            diseaseCdList : getCodeList('Disease',this.callback),
            saveInfo: {
                bucketName : "",    // 저장소명
                openDataName : "",   // 공개데이터명
                reqStorageId : "",  // 저장신청Id
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
        // // 저장소불러오기(팝업)
        // onclickOpenStorage: function () {
        //     window.open("/popup/storage", "pop", "width=500px,height=500px");
        // },

        // 저장소불러오기(팝업)
        openStorageModalPop : function() {
            fnOpenPopup("storageModal", "");
        },
        callbackPopupStorage : function(item){   // 저장소불러오기(팝업) Callback
            this.saveInfo.bucketName = item.dataName; // 저장소명
            this.saveInfo.reqStorageId = item.reqStorageId; //저장신청Id
        },
        // 신청정보 초기화 클릭
        onclickReset: function () {
            this.saveInfo.reqStorageId = "";
            this.saveInfo.bucketName = "";
            this.saveInfo.openDataName = "";
        },
        // 취소 클릭(목록 이동)
        onclickCancel: function () {
            location.href = "/lndata/open/main";
        },
        // 신청 클릭(신청)
        onclickReqSave: function () {
            this.saveReqStorage();
        },
        // 신청 메소드 호출
        saveReqStorage:function () {

            if(!this.saveInfo.reqStorageId){
                alertMsg("저장소를 선택해주세요.");
                return false;
            }
            if(!this.saveInfo.openDataName){
                alertMsg("공개데이터명은 필수입니다.",this.$refs.openDataName);
                return false;
            }

            this.saveInfo.diseaseManagerUserSeq = this.userInfo.managerUserSeq;
            confirmMsg("신청하시겠습니까?", this.save);
        },
        save: function(){
            post(TID.SAVE,
                "/storage/open",
                this.saveInfo,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH_USER:
                    this.searchCallback(results);
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
                alertMsgRtn("정상적으로 신청되었습니다.", this.onclickCancel);
                // alert("정상적으로 신청되었습니다.");
                // location.href = "/lndata/open/main";
            } else {
                console.log(results);
                alertMsg(results.error.message);
            }
        },
    }
});

