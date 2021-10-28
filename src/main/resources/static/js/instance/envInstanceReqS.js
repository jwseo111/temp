
/*
 * @name : evnInstanceReqS.js
 * @date : 2021-09-10 오후 3:05
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
let popupId;
const TID = {
    SEARCH : {value: 0, name: "search", code: "S"},
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
            serverProductCond: {},
            bReload : false,
            userInfo : {
                agencyInfo:{},
            },
            envInstanceInfo:{},
            osImageTypeCdList: getCodeList("OsImageType", this.callback), // 운영체제
            productTypeCdList: getCodeList("ProductType", this.callback), // 서버타입 selectbox list
            serverProductList :[], // 서버스펙 selectbox list
            usingDays : "", // 사용일수(화면에 보이는 값)
            saveInfo: {
                osImageType:"", // 운영체제
                productType: "", // 서버타입
                storageType: "", //스토리지(SSD/HDD)
                productCode:"", //서버스펙
                serverDescription:"", // 서버설명
                usingDays:"", // 사용일수
            },
            usingDaysMax:"",

        };
    },
    mounted:function(){
        this.getUserInfo();
        setTimeout(function() {
            loadSelect();
        },300);
    },
    methods: {
        // 사용일수 입력 이벤트
        onInputUsingDays:function (e){
            if(!isNull(e.target.value) && e.target.value > 0) {
                this.usingDaysMax = "";
            } else {
                this.usingDaysMax = "Y";
            }
            this.saveInfo.usingDays = e.target.value;
            this.onChangeUsingDays(this.usingDaysMax);
        },
        // 사용일수 무기한 이벤트
        onChangeUsingDays:function (val){
            if(val == "Y") {
                this.usingDays = "";
                this.saveInfo.usingDays = "0";
            } else {
            }
        },
        getUserInfo: function () {
            get("usrInfo", "/user/my/info", {}, this.callback);
        },

        // ServerImageProductList 리스트 조회
        getServerImageProductList: function () {
            get("serverImageProductList",
                "/my/management/instance/vpc/server/getServerImageProductList",
                {},
                this.callback);
        },
        // ServerProductList 리스트 조회(서버 스펙)
        getServerProductList: function () {
            console.log("서버스펙 조회 : " + JSON.stringify(this.serverProductCond));//tmp
            get("serverProductList",
                "/my/management/instance/vpc/server/getServerProductList",
                this.serverProductCond,
                this.callback);
        },

        // 신청화면에 출력될 데이타 조회
        getEnvInstanceView: function () {
            get(TID.SEARCH,
                "/env/instance/getDetail",
                this.cond,
                this.callback);
        },

        // 운영체제 변경
        onChangeOsImage: function (productCode) {
            this.serverProductCond.serverImageProductCode = productCode;
            this.getServerProductList();
        },
        // 서버타입 변경
        onChangeProduct: function (name) {
            this.serverProductCond.productType  = name;
            // 운영체제 선택되어 있을 경우, 서버스펙 리스트를 조회한다.
            if(!isNull(this.serverProductCond.serverImageProductCode)) {
                this.getServerProductList();
            }
        },
        // 스토리지 변경
        onChangeStorage: function (storage){
            this.serverProductCond.storageType  = storage;
            this.getServerProductList();
        },
        // 취소 클릭(목록 이동)
        onclickCancel: function () {
            location.href = "/env/instance/main";
        },
        // 신청 클릭(학습환경 신청)
        onclickCreateSave: function () {
            this.saveCreateEnvironment();
        },
        // 신청 메소드 호출
        saveCreateEnvironment:function () {
            let param = [
                {value:this.saveInfo.osImageType, title:"운영체제 ", ref:this.$refs.osImageType},
                {value:this.saveInfo.productType, title:"서버타입 ", ref:this.$refs.productType},
                {value:this.saveInfo.storageType, title:"스토리지 ", ref:this.$refs.storageType},
                {value:this.saveInfo.productCode, title:"서버스펙 ", ref:this.$refs.productCode},
                {value:this.saveInfo.usingDays,   title:"사용 일수 ", ref:this.$refs.usingDays},
            ];

            if(!isValid(param)) return false;

            //console.log("신청  : " + JSON.stringify(this.saveInfo));
            confirmMsg("신청하시겠습니까?",this.createEnvironment);
        },
        createEnvironment: function() {
            post(TID.SAVE,
                "/env/instance/vpc/reqCreateEnvironment",
                this.saveInfo,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case "usrInfo":
                    //console.log(results);
                    if (results.success) {
                        this.userInfo = results.response;
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }

                    break;
                case "serverImageProductList":
                    //console.log(results);
                    if (results.success) {

                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    break;
                case "serverProductList":
                    //console.log(results);
                    if (results.success) {
                        if(!isNull(results.response.getServerProductListResponse)) {
                            this.serverProductList = results.response.getServerProductListResponse.productList;
                        }
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    // 서버스펙 select box  reload
                    let targetObj = {
                        selectDiv: "serverProductDiv",
                        selectId: "productCode",
                        optionValue: "productCode",
                        optionName: "productDescription",
                    };
                    reloadSelect(targetObj, this.serverProductList);
                    break;
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case TID.SAVE:
                    //console.log(results);
                    this.saveCallback(results);
                    break;
                case "ProductType": // 서버스펙
                    console.log(results.response);
                    if (results.success) {
                        this.productTypeCdList = results.response;
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    break;
                case "OsImageType": // 운영체제
                    console.log(results.response);
                    if (results.success) {
                        this.osImageTypeCdList = results.response;
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                console.log(results);
                this.envInstanceInfo   = results.response;
            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
        saveCallback: function (results) {
            if (results.success) {
                alertMsgRtn("정상적으로 신청되었습니다.",this.onclickCancel);
            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },

        // selectebox 이벤트
        searchChange:function(id, data){
           if(id == "productCode") {
                this.saveInfo.productCode = data;
            }
        },
    }
});

// selectebox 이벤트
function selectedChange(id) {
    const data = document.querySelector('#' + id).value;
    // 선택된 값이 있는 경우 수행한다.
     if(!isNull(data)){
    appMain.$refs.maincontents.searchChange(id, data);
    }
};

