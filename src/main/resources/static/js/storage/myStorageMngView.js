
/*
 * @name : myStorageView.js
 * @date : 2021-07-21 오후 12:50
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH : {value: 0, name: "search", code: "S"}, // 상세조회
    SEARCH_OBJECT : {value: 0, name: "searchObj", code: "S"}, // 오브젝트조회
    SEARCH_AUTH : {value: 0, name: "searchAuth", code: "S"}, // 권한조회
    DELETE : {value: 0, name: "delete", code: "D"} // 저장소삭제
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
                reqStorageId : reqStorageId,
                folderName : ""
            },
            reqStorageId : reqStorageId,
            messages : "",
            reqStoreStatCd : "",

            reqStorageInfo : {
                agencyInfo: {},
                diseaseCode: {},
                reqStorageStatCode:{}
            },
            reqUserInfo:{
                diseaseCode:{}
            },

            myStorageObjectList: [], // 오브젝트 정보
            myStorageAuthList : [], // 권한 정보

        };
    },
    mounted:function(){
        this.getMyStorageInfo();
    },
    methods:{
        // 마이페이지 > 저장소관리 > 상세 조회
        getMyStorageInfo:function (){
            get(TID.SEARCH,
                "/my/management/storage/req/"+this.reqStorageId,
                {},
                this.callback);
        },
        // 오브젝트 정보 조회
        getMyStorageObjectList:function (bucketName){
            get(TID.SEARCH_OBJECT,
                "/my/management/storage/object/list/"+bucketName,
                this.cond,
                this.callback);
        },
        // 권한 정보 조회
        getMyStorageAuthList:function (reqStorageId){
            get(TID.SEARCH_AUTH,
                "/storage/req/auth/list/"+reqStorageId,
                this.cond,
                this.callback);
        },

        // 목록버튼 클릭
        onclickList: function () {
            location.href = "/my/storeMng/list?menuId="+myMenuId;
        },
        // 저장소삭제 버튼 클릭
        onclickDelete: function (reqStorageId){
            if(confirm("저장소를 삭제하시겠습니까?")) {
                post(TID.DELETE,
                    "/management/storage/req/delete/" + this.reqStorageId,
                    {},
                    this.callback);
            }
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH: // 상세조회
                    this.searchCallback(results);
                    break;
                case TID.SEARCH_OBJECT: // 오브젝트정보 조회
                    if(results.success) {
                        this.myStorageObjectList = results.response;
                    } else{
                        console.log(results);
                    }
                    break;
                case TID.SEARCH_AUTH:
                    //console.log(results);//tmp
                    if(results.success) {
                        this.myStorageAuthList = results.response;
                    } else{
                        console.log(results);
                    }
                    break;
                case TID.DELETE: // 저장소삭제
                    if (results.success) {
                        alert("정상적으로 삭제되었습니다.");
                        this.onclickList();// 목록으로 이동
                    } else {
                        console.log(results);
                        alert("에러:\n" + results.error.message);
                    }
                    break;


            }
        },
        searchCallback: function (results) {
            if (results.success) {
                this.reqUserInfo = results.response.reqUserDto;
                this.reqStorageInfo   = results.response;
                // 오브젝트 정보 조회
                this.getMyStorageObjectList(results.response.bucketName);
                // 권한 정보 조회
                this.getMyStorageAuthList(results.response.reqStorageId);
            } else {
                console.log(results);
            }
        }
    }
});

