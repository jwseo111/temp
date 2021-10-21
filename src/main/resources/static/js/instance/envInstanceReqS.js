
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
            cond: {
                // reqSeq: reqSeq
            },
            serverProductCond: {
               // productType: {name:"", desc:""},
            },
            //ProductType:{name:"STAND", desc:"Standard"},
            bReload : false,
            userInfo : [],
            envInstanceInfo:[],
            messages : "",
            osImageTypeCdList: getCodeList("OsImageType", this.callback), // 운영체제
            productTypeCdList: getCodeList("ProductType", this.callback), // 서버타입 selectbox list
            serverProductList :[], // 서버스펙 selectbox list
            loginKey:"", //인증키 selectbox
            loginKeyList : [], // 인증키 selectbox list
            usingDays : "", // 사용일수(화면에 보이는 값)
            saveInfo: {
                vpcNo:"",
                subnetNo:"",
                //osImage:"", // 운영체제
                osImageType:"", // 운영체제
                productType: "", // 서버타입
                storageType: "", //스토리지(SSD/HDD)
                productCode:"", //서버스펙
                associateWithPublicIp:true, // 공인IP(boolean)
                serverDescription:"", // 서버설명
                loginKeyName:"", // 인증키이름
                usingDays:"", // 사용일수
                networkInterfaceList:[] // network interface
            },
            createLoginKey:{ // 인증키 생성
                keyName:"",//인증키 이름
            },
            usingDaysMax:"",
            vpcList: [], // VPC 콤보목록
            vpcNo:"", // VPC 콤보 선택

            subnetList: [], // Subnet 콤보목록
            subnetSelected:"", // Subnet 콤보 선택

            selected:"", // 선택된 콤보박스
            selectList:     getCodeList('ReqStorageStat',this.callback),//
            //interfaceList:  getCodeList('Disease',this.callback),// NetworkInterface 콤보
            networkInterfaceCbList: [],// NetworkInterface 콤보
            networkInterfaceNo:"", // 선택된 NetworkInterface
            sSubnet:"", // 선택된 Subnet
            networkInterfaceOrder : 0,
            newNetworkInterface:{
                networkInterfaceOrder:"", // 디바이스 순서
                // interface:"",
                // subnet:"",
                subnetNo:"", // subnet 번호
                subnetName:"", // subnet 이름
                ip:"", // ip
                networkInterfaceNo:"",    // network interface 번호
                networkInterfaceName:"", // network interface 이름
            },
            networkInterfaceList : [], // 추가된 network interface
            loginKeyChk1: false, // 최소 3글자 이상, 최대 30자까지만 입력이 가능합니다
            loginKeyPass : true,

        };
    },
    mounted:function(){
        this.getUserInfo();
        this.getVpcList(); // VPC 콤보리스트
        // //this.getServerImageProductList();
        // this.getServerProductList(); // 서버스펙 콤보(운영체제, 서버타입에 따라 변경)
        //this.getNetworkInterfaceCbList();//network interface 콤보(subnet 콤보에 따라 변경)
        // //this.getEnvInstanceView(); // 학습환경 신청 정보 조회
        this.getLoginKeyList();

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

        // VPC 콤보 리스트 조회
        getVpcList: function (bReload) {
            if(bReload) {
                this.bReload =bReload;
            }
            get("vpcList",
                "/my/management/instance/vpc/getVpcList",
                {},
                this.callback);
        },
        // Subnet 콤보 리스트 조회
        getSubnetList: function () {
            let cond = {vpcNo : ""};
            cond.vpcNo = this.saveInfo.vpcNo;
            get("subnetList",
                "/my/management/instance/vpc/getSubnetList",
                cond,
                this.callback);
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
        // networkInterfaceCbList 콤보 리스트 조회
        getNetworkInterfaceCbList: function () {
            let cond = {subnetName: ""};
            cond.subnetName = this.saveInfo.subnetName;
            get("networkInterfaceCbList",
                "/my/management/instance/vpc/getNetworkInterfaceList",
                cond,
                this.callback);
        },

        // loginKeyList 인증키 콤보 리스트 조회
        getLoginKeyList: function () {
            get("loginKeyList",
                "/my/management/instance/server/getLoginKeyList",
                {},
                this.callback);
        },

        // 신청화면에 출력될 데이타 조회
        getEnvInstanceView: function () {
            get(TID.SEARCH,
                "/env/instance/getDetail",
                this.cond,
                this.callback);
        },
        // 팝업 open
        onclickPop: function (popId) {
            // VPC 3개 이상일 경우, return
            if (popId === "vpcModal") {
                if (this.vpcList.length == 3) {
                    alertMsg("VPC는 계정당 최대 3개까지만 생성할 수 있습니다.");
                    return;
                }
                appPopVpc.$refs.popupvpc.onload();
            }
            // subnet 생성 버튼 클릭시, VPC 콤보 미선택시 return
            if (popId === "subnetModal") {
                if (!this.$refs.vpcNo.value) {
                    alertMsg("Subnet 생성 시 VPC를 선택하세요.");
                    return;
                }
                appPopSubet.$refs.popupsubnet.$data.saveInfo.vpcNo = this.$refs.vpcNo.value;
                appPopSubet.$refs.popupsubnet.onload();
            }
            document.getElementById(popId).style.display = "block";
            document.documentElement.style.overflowX = 'hidden';
            document.documentElement.style.overflowY = 'hidden';
        },
        // // 팝업 close
        // onclickPopClose:function(id){
        //    document.getElementById(id).style.display = "none";
        //    document.documentElement.style.overflowX = 'auto';
        //    document.documentElement.style.overflowY = 'auto';
        // },
        // 운영체제 변경
        onChangeOsImage: function (productCode) {
            console.log("운영체제 변경 productCode: " + productCode);
            this.serverProductCond.serverImageProductCode = productCode;
            this.getServerProductList();
        },
        // 서버타입 변경
        onChangeProduct: function (name) {
            console.log("서버타입 변경 name: " + name);
            this.serverProductCond.productType  = name;
            this.getServerProductList();
        },
        onChangeStorage: function (storage){
            this.serverProductCond.storageType  = storage;
            this.getServerProductList();
        },
        // NetworkInterface 추가 버튼 이벤트
        onclickIFAdd: function () {
            let cnt = this.networkInterfaceList.length;
            if (cnt === 3) {
                alertMsg("Network Interface는 최대 3개까지 추가 가능합니다.");
                return;
            }
            // new interface 선택시 interface 생성 팝업으로 연결
            if (isNull(this.newNetworkInterface.networkInterfaceNo)) {
                if (!this.$refs.vpcNo.value) {
                    alertMsg("Network Interface 생성 시 VPC를 선택하세요.");
                    return;
                }
                if (!this.$refs.subnetNo.value) {
                    alertMsg("Network Interface 생성 시 Subnet를 선택하세요.");
                    return;
                }
                appPopIf.$refs.popupif.$data.saveInfo.vpcNo = this.$refs.vpcNo.value;
                appPopIf.$refs.popupif.$data.saveInfo.subnetNo = this.$refs.subnetNo.value;
                appPopIf.$refs.popupif.onload();

                document.getElementById("networkInterfaceModal").style.display = "block";
                document.documentElement.style.overflowX = 'hidden';
                document.documentElement.style.overflowY = 'hidden';

            } else { //  하단 리스트 추가
                // networkInterfaceOrder 값 설정
                this.networkInterfaceOrder = this.networkInterfaceOrder + 1;
                this.newNetworkInterface.networkInterfaceOrder = this.networkInterfaceOrder;

                let obj = {}
                obj.networkInterfaceOrder = this.newNetworkInterface.networkInterfaceOrder;
                obj.networkInterfaceNo = this.newNetworkInterface.networkInterfaceNo;
                obj.networkInterfaceName = this.newNetworkInterface.networkInterfaceName;
                obj.subnetNo = this.newNetworkInterface.subnetNo;
                obj.subnetName = this.newNetworkInterface.subnetName;
                obj.ip = this.newNetworkInterface.ip;

                this.networkInterfaceList.push(obj);

                this.newNetworkInterface.ip="";
                this.newNetworkInterface.networkInterfaceNo="";
                this.newNetworkInterface.networkInterfaceName="";

                this.networkInterfaceCbListReload();
            }
        },
        // Network Interface 입력영역 초기화
        newNetworkInterfaceReset:function (){

            this.newNetworkInterface = {
                networkInterfaceOrder:"",
                subnetNo:"",
                subnetName:"",
                ip:"",
                networkInterfaceNo:"",
                networkInterfaceName:"",
            };
            this.networkInterfaceCbListReload();
        },
        // Network Interface 추가된 리스트 삭제
        networkInterfaceAllDel:function (){
            this.networkInterfaceList = [];
        },
        // NetworkInterface 삭제 클릭
        onclickIFDel: function (idx){
            this.networkInterfaceList.splice(idx,1); // 선택 행 삭제
        },
        // 인증키 설정
        onChangeKey : function(val) {
            if(val == "Y") {// 보유하고 있는 인증키 이용
                document.getElementById("loginKeyY").style.display="block";
                document.getElementById("loginKeyN").style.display="none";
                this.createLoginKey.keyName="";
                this.loginKeyChk1 = false;
                this.loginKeyPass = true;
            } else { // 새로운 인증키 생성
                document.getElementById("loginKeyY").style.display="none";
                document.getElementById("loginKeyN").style.display="block";
                this.loginKeyChk1 = false;
                this.loginKeyPass = false;
            }
        },
        // 인증키 이름 체크
        onKeyupLoginKey:function (e){
            let str = e.target.value;
            if(str.length < 3 || str.length > 30){ //최소 3글자 이상, 최대 30자까지만 입력이 가능합니다.
                this.loginKeyChk1 = true;
                this.loginKeyPass = false;
            } else {
                this.loginKeyChk1 = false;
                this.loginKeyPass = true;
            }
        },
        // 인증키 생성 및 저장
        onclickLoginKeyCreate: function() {
            if(isNull(this.createLoginKey.keyName)){
                alertMsg("인증키 이름을 입력하세요.",this.$refs.keyName);
                return false;
            }
            if(!this.loginKeyPass){
                alertMsg("인증키 이름을 확인하세요.",this.$refs.keyName);
                return false;
            }
            confirmMsg("인증키를 생성하시겠습니까?",this.loginKeyCreate);
        },
        loginKeyCreate: function(){
            get("loginKeyCreate",
                "/my/management/instance/server/createLoginKey",
                this.createLoginKey,
                this.callback);
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
            this.saveInfo.networkInterfaceList = this.networkInterfaceList;
            console.log("### : " + JSON.stringify(this.saveInfo));//tmp

            // let param = [
            //     {value:this.saveInfo.osImageType, title:"운영체제 ", ref:this.$refs.osImageType},
            //     {value:this.saveInfo.productType, title:"서버타입 ", ref:this.$refs.productType},
            //     {value:this.saveInfo.vpcNo,       title:"VPC ",     ref:this.$refs.vpcNo},
            //     {value:this.saveInfo.subnetNo,    title:"Subnet ",  ref:this.$refs.subnetNo},
            //     {value:this.saveInfo.storageType, title:"스토리지 ", ref:this.$refs.storageType},
            //     {value:this.saveInfo.productCode, title:"서버스펙 ", ref:this.$refs.productCode},
            // ];
            //
            // if(!isValid(param)) return false;
            console.log("sdaadssda");

            // if(this.saveInfo.networkInterfaceList.length == 0) {
            //     alertMsg("Network Interface는 1개 이상이어야 합니다.");
            //     return false;
            // }

            // let param = [
            //     {value:this.saveInfo.associateWithPublicIp, title:"공인IP ", ref:this.$refs.associateWithPublicIp},
            //     {value:this.saveInfo.usingDays,   title:"사용 일수 ", ref:this.$refs.usingDays},
            // ];
            // if(!isValid(param)) return false;

console.log("신청  : " + JSON.stringify(this.saveInfo));//tmp
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
                case "vpcList":
                    console.log(results);
                    if (results.success) {
                        if(!isNull(results.response.getVpcListResponse)) {
                            this.vpcList = results.response.getVpcListResponse.vpcList;
                        }
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    if(!this.bReload){
                        setTimeout(function() {
                            loadSelect();
                        },300);
                    }
                    break;
                case "subnetList":
                    console.log(results);
                    if (results.success) {
                        if(!isNull(results.response.getSubnetListResponse)) {
                            this.subnetList = [];
                            this.subnetList = results.response.getSubnetListResponse.subnetList;
                        }
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    // vpc 선택에 따라 subnet selectbox 재구성
                    let subnetObj = {
                        selectDiv:"subnetDiv",
                        selectId:"subnetNo",
                        optionValue:"subnetNo",
                    };
                    reloadSelect(subnetObj, this.subnetList);

                    break;
                case "serverImageProductList":
                    console.log("serverImageProductList =========");//tmp
                    console.log(results);
                    if (results.success) {

                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    break;
                case "serverProductList":
                    console.log(results);
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
                        selectDiv:"serverProductDiv",
                        selectId:"productCode",
                        optionValue:"productCode",
                        optionName:"productDescription",
                    };
                    reloadSelect(targetObj, this.serverProductList);
                    break;
                case "networkInterfaceCbList":
                    console.log(results);
                    if (results.success) {
                        if(!isNull(results.response.getNetworkInterfaceListResponse)) {
                            this.networkInterfaceCbList = results.response.getNetworkInterfaceListResponse.networkInterfaceList;

                            this.networkInterfaceCbListReload(popupId);
                            if(!isNull(popupId)){

                            }
                        }
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    break;
                case "loginKeyList":
                    console.log(results);
                    if (results.success) {
                        if(!isNull(results.response.getLoginKeyListResponse)) {
                            this.loginKeyList = results.response.getLoginKeyListResponse.loginKeyList;
                        }
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    break;
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;

                case "loginKeyCreate":
                    console.log(results);
                    if (results.success) {
                        // 인증키 생성 후 다운로드 실행
                        textDownload(results.response.keyName+".pem", results.response.privateKey);
                        let targetObj = {
                            selectDiv:"loginKeyDiv",
                            selectId:"loginKey",
                            optionValue:"fingerprint",
                            optionName:"keyName",
                        };
                        //  생성후 인증키 콤보 reload
                        alertMsgRtn("정상적으로 생성되었습니다.",
                            reloadSelect(targetObj, this.loginKeyList)
                        );
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
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
            if(id == "vpcNo") {
                this.saveInfo.vpcNo = data;
                document.querySelector("#subnetNo").value = "";
                // this.condSubnet.vpcNo = data;
                if(!isNull(data)) {
                    // this.saveInfo.vpcNo = data;
                    // this.condSubnet.vpcNo = data;
                    this.getSubnetList();
                } else {
                    // vpc 선택에 따라 subnet selectbox 재구성
                    // vpc 선택값이 없을 경우, subnet selectbox reset
                    this.subnetList = [];
                    let obj = {
                        selectDiv:"subnetDiv",
                        selectId:"subnetNo",
                        optionValue:"subnetNo",
                    };
                    reloadSelect(obj, this.subnetList);
                }
                selectedChange("subnetNo");
            } else if(id == "subnetNo") { // subnet 콤보 변경
                // network interface 관련 데이타 초기화
                this.newNetworkInterfaceReset(); // network interface 입력 영역 리셋
                this.networkInterfaceAllDel();// network interface 추가된 영역 리셋

                if(!isNull(data)) {
                    let subnetNo = data;
                    let idx = this.subnetList.findIndex(function (key) {
                        return key.subnetNo === subnetNo
                    });
                    this.saveInfo.subnetNo = data;
                    this.saveInfo.subnetName = this.subnetList[idx].subnetName;
                    this.getNetworkInterfaceCbList(); // network interface select 조회

                } else {
                    this.saveInfo.subnetNo = "";
                    this.saveInfo.subnetName = "";
                    this.networkInterfaceCbList = []; // network interface select list clear
                    this.networkInterfaceCbListReload();
                }
                this.newNetworkInterface.subnetNo = this.saveInfo.subnetNo;
                this.newNetworkInterface.subnetName = this.saveInfo.subnetName;



            } else if(id == "productCode") {
                this.saveInfo.productCode = data;
            } else if(id == "loginKey") {
                this.saveInfo.loginKeyName = data;
            }
        },
        searchNetworkIFChange:function(data){ // 네트워크인터페이스 콤보 변경
            this.newNetworkInterface.networkInterfaceNo = data;
            if(!isNull(data)) {
                let idx = this.networkInterfaceCbList.findIndex(function (key) {
                    return key.networkInterfaceNo === data
                });
                this.newNetworkInterface.networkInterfaceName = this.networkInterfaceCbList[idx].networkInterfaceName;
                this.newNetworkInterface.ip = this.networkInterfaceCbList[idx].ip;
            } else {
                this.newNetworkInterfaceReset(); // network interface 입력영역 초기화
            }
        },
        // VPC 생성 후 팝업 닫힐때 vpc list 재조회
        vpcCbListReload:function(){
            let targetObj = {
                selectDiv:"vpcDiv",
                selectId:"vpcNo",
                optionValue:"vpcNo",
                optionName:"vpcName",
            };
            // VPC select box 리로드
            reloadSelect(targetObj, this.vpcList);
            fnClosePopup('vpcModal');
        },
        // networkInterfaceCbList list 재조회
        networkInterfaceCbListReload:function(popupId){
            let obj = {
                selectDiv:"networkInterfaceDiv",
                selectId:"networkInterfaceNo",
                optionValue:"networkInterfaceNo",
                optionName:"networkInterfaceName",
            };
            // network select box 리로드
            reloadSelect(obj, this.networkInterfaceCbList);

            // popupId 가 있을 경우, 팝업을 닫는다.
            if(!isNull(popupId)) {
                fnClosePopup(popupId);
            }
        },

    }
});

// selectebox 이벤트
function selectedChange(id){
    const data = document.querySelector('#'+id).value;
    // 선택된 값이 있는 경우 수행한다.
    // if(!isNull(data)){
    if(id == "networkInterfaceNo") { // networkInterface 콤보 변경
        if(isNull(document.querySelector('#vpcNo').value)) {
            alertMsg("VPC를 선택해주세요.");
            appMain.$refs.maincontents.networkInterfaceCbListReload();
            return;
        }
        if(isNull(document.querySelector('#subnetNo').value)) {
            alertMsg("Subnet을 선택해주세요.");
            appMain.$refs.maincontents.networkInterfaceCbListReload();
            return;
        }
        appMain.$refs.maincontents.searchNetworkIFChange(data);
    } else {
        appMain.$refs.maincontents.searchChange(id, data);
    }
};

