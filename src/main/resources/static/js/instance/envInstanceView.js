
/*
 * @name : evnInstanceView.js
 * @date : 2021-09-10 오후 3:05
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
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
                reqSeq: reqSeq
            },
            userInfo : [],
            envInstanceInfo:[],
            messages : "",

            // diseaseCdList : getCodeList('Disease',this.callback),
            saveInfo: {
            },

            selected:"", // 선택된 콤보박스
            selectList:     getCodeList('ReqStorageStat',this.callback),//
            interfaceList:  getCodeList('Disease',this.callback),// NetworkInterface 콤보
            subnetList:     getCodeList('Disease',this.callback),// Subnet 콤보
            sInterface:"", // 선택된 NetworkInterface
            sSubnet:"", // 선택된 Subnet
            newNetworkInterface:{
                device:"",
                interface:"",
                subnet:"",
                ip:""
            },
            networkInterfaceList : [], //

        };
    },
    mounted:function(){
        this.getUserInfo();
        this.getEnvInstanceView();
        this.newNetworkInterface.device = "eth" + this.networkInterfaceList.length;
    },
    methods:{
        getUserInfo: function(){
            get("usrInfo","/user/my/info",{},this.callback);
        },
        // 신청화면에 출력될 데이타 조회
        getEnvInstanceView:function (){
            get(TID.SEARCH,
                "/env/instance/getDetail",
                this.cond,
                this.callback);
        },
        onclickPop: function (popId) {
            console.log("생성 팝업 : " + popId);//tmp
            document.getElementById(popId).style.display = "block";
            document.documentElement.style.overflowX = 'hidden';
            document.documentElement.style.overflowY = 'hidden';
        },
        // NetworkInterface 추가 클릭
        onclickCertAdd: function (){

            let cnt = this.networkInterfaceList.length;
            if(cnt == 3){
                alertMsg("Network Interface는 최대 3개까지 추가 가능합니다.");
                return;
            }

            // new interface 선택시 interface 생성 팝업으로 연결
            if(!this.$refs.sInterface.value) {
                console.log("interface 생성 팝업");//tmp
                document.getElementById("networkInterfaceModal").style.display = "block";
                document.documentElement.style.overflowX = 'hidden';
                document.documentElement.style.overflowY = 'hidden';

            } else { //  하단 리스트 추가
                if(!this.$refs.sSubnet.value) {
                    console.log("Subnet을 선택하세요.");
                    alertMsg("Subnet을 선택하세요.");
                    return;
                }
                this.newNetworkInterface.interface = this.$refs.sInterface.value;
                this.newNetworkInterface.subnet = this.$refs.sSubnet.value;

                // IP 미입력시 자동할당
                if (!this.newNetworkInterface.ip) {
                    this.newNetworkInterface.ip = "자동할당";
                }
                this.networkInterfaceList.push(this.newNetworkInterface);

                this.newNetworkInterface = {};
                this.newNetworkInterface.device = "eth" + this.networkInterfaceList.length;
            }
        },
        // NetworkInterface 삭제 클릭
        onclickCertDel: function (idx){
            this.networkInterfaceList.splice(idx,1);
            this.newNetworkInterface = {};
            this.newNetworkInterface.device = "eth" + this.networkInterfaceList.length;
        },
        // 취소 클릭(목록 이동)
        onclickCancel: function () {
            location.href = "/env/instance/main";
        },
        // 신청 클릭(신청)
        onclickReqSave: function () {
            this.saveReqStorage();
        },
        // 신청 메소드 호출
        saveReqStorage:function () {


            confirmMsg("신청하시겠습니까?",this.save);
        },
        save: function() {
            // post(TID.SAVE,
            //     "/storage/req",
            //     this.saveInfo,
            //     this.callback);
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
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case "Disease":
                    this.interfaceList = results.response;
                    this.subnetList = results.response;
                    break;
                case TID.SAVE:
                    //console.log(results);
                    this.saveCallback(results);
                    break;
                case "ReqStorageStat":
                    //console.log(results.response);
                    // this.interfaceList = results.response;
                    // this.subnetList = results.response;
                    this.selectList = results.response;
                    setTimeout(function() {
                        loadSelect();
                    },300);
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

    }
});

