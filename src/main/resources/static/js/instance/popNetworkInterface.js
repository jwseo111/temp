
/*
 * @name : popNetworkInterface.js
 * @date : 2021-09-16 오후 2:03
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appPopIf;
window.addEventListener('load', function() {
    appPopIf = new Vue({
        el: '#popupwrapif',
    });

});
Vue.component('popupif', {
    template: "#popup-templateif",
    data: function () {
        return {
            networkInterface: [],
            interfaceName:"new interface",
            bReload : false, // 재조회 여부
            accessControlGroupNo:"", // 선택된 Acg
            accessControlGroupCbList:[], // 콤보박스 acg list
            accessControlGroupList:[], // 추가할 acg list
            saveInfo:{
                networkInterfaceName:"", // networkInterface 이름
                vpcNo:"",
                subnetNo:"",
                ip:"", // primary IP
                accessControlGroupNoList:[],
                networkInterfaceDescription:"", // 메모
            },
            vpcName:"",
            vpcDesc:"",
            subnetName:"",
            ipSelect:"",
            message :{
                networkInterfaceName : "",
                ip:"",
            },
            pass : {
                networkInterfaceName:false,
                ip:false
            },
        };
    },
    mounted: function () {
        //this.getAcgList();
    },
    methods: {
        onload: function() {
            let vpcList = appMain.$refs.maincontents.vpcList;
            let subnetList = appMain.$refs.maincontents.subnetList;

            let vpcNo = this.saveInfo.vpcNo;
            let subnetNo = this.saveInfo.subnetNo;

            let vpcIdx = vpcList.findIndex(function(key) {return key.vpcNo === vpcNo});
            this.vpcName =  vpcList[vpcIdx].vpcName;
            this.vpcDesc =  vpcList[vpcIdx].vpcName + "(" + vpcList[vpcIdx].ipv4CidrBlock +")";

            let subnetIdx = subnetList.findIndex(function(key) {return key.subnetNo === subnetNo});
            this.subnetName =  subnetList[subnetIdx].subnetName;
            this.bReload = true;
            this.getAcgList();

            // 입력란 초기화
            this.saveInfo.networkInterfaceName = "";
            this.saveInfo.ip = "";
            this.ipSelect = "";
            this.accessControlGroupList=[];

        },
        // NetworkName 규칙 체크
        onKeyupNetworkName:function (e) {
            let str = e.target.value;
            let ll = str.length;
            let exp1 = /[a-z0-9]/; // 영문 또는 숫자 체크
            let exp2 = /^[a-z]{1}[a-z0-9-]+$/; // 첫문자는 소문자, 소문자, 숫자, 하이픈 허용

            if(str.length < 3 || str.length > 30){ // 최소 3글자 이상, 최대 30자까지만 입력이 가능합니다.
                this.pass.networkInterfaceName = false;
                this.message.networkInterfaceName = "최소 3글자 이상, 최대 30자까지만 입력이 가능합니다.";
            } else if(!exp2.test(str)) { // 소문자, 숫자,"-"의 특수문자만 허용하며 알파벳 문자로 시작해야 합니다.
                this.pass.networkInterfaceName = false;
                this.message.networkInterfaceName = "소문자, 숫자,\"-\"의 특수문자만 허용하며 알파벳 문자로 시작해야 합니다.";
            } else if(!exp1.test(str[ll-1])) { //영어 또는 숫자로 끝나야 합니다.
                this.pass.networkInterfaceName = false;
                this.message.networkInterfaceName = "영어 또는 숫자로 끝나야 합니다.";
            } else {
                this.pass.networkInterfaceName = true;
                this.message.networkInterfaceName = "";
            }
        },
        onKeyupIp:function (e){
            let str = e.target.value;
            let exp = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;

            if(this.ipSelect == "2" && !exp.test(str)) {                // 잘못된 형식입니다.
                this.pass.ip = false;
                this.message.ip = "잘못된 형식입니다.";
            } else {
                this.pass.ip = true;
                this.message.ip = "";
            }
        },
        // ACG 목록 조회
        getAcgList:function() {
            let acgCond = {vpcNo:this.saveInfo.vpcNo};
            get("acgList",
                "/my/management/instance/vpc/getAcgList",
                acgCond,
                this.callback);
        },
        // ACG 삭제 이벤트
        onclickAcgDel: function (idx){
            this.accessControlGroupList.splice(idx,1); // 선택 행 삭제
        },
        // 팝업 open
        onclickPop: function (popId) {
            // appPopAcg.$refs.popupacg.$data.saveInfo.createAccessControlGroupRequestDto.vpcNo = this.$refs.vpcNo.value;
            appPopAcg.$refs.popupacg.onload(this.$refs.vpcNo.value);
            document.getElementById(popId).style.display = "block";
            document.documentElement.style.overflowX = 'hidden';
            document.documentElement.style.overflowY = 'hidden';
        },
        onclickCancel: function() {

        },
        // 생성 클릭(network interface 생성)
        onclickCreateSave: function () {

            this.saveInfo.accessControlGroupNoList = [];
            for(let i=0; i<this.accessControlGroupList.length;i++) {
                this.saveInfo.accessControlGroupNoList.push(this.accessControlGroupList[i].accessControlGroupNo);
            }
            console.log("### : " + JSON.stringify(this.saveInfo));
            let param = [
                {value:this.saveInfo.networkInterfaceName, title:"Network Interface 이름 ", ref:this.$refs.networkInterfaceName},
                {value:this.saveInfo.vpcNo,       title:"VPC ",     ref:this.$refs.vpcNo},
                {value:this.saveInfo.subnetNo,    title:"Subnet ",  ref:this.$refs.subnetNo}
            ];
            if(!isValid(param)) return false;

            if(this.ipSelect == "2") {
                if(isNull(this.saveInfo.ip)) {
                    this.pass.ip = false;
                }
            } else if(this.ipSelect == "1") {
                this.saveInfo.ip = "";
                this.pass.ip = true;
                this.message.ip = "";
            } else {
                alertMsg("Primary IP는 필수 입니다.");
                return;
            }

            if(!this.pass.networkInterfaceName){
                alertMsg("Network Interface 이름을 확인하세요.",this.$refs.networkInterfaceName);
                return;
            }

            if(!this.pass.ip){
                alertMsg("IP 주소를 확인하세요.",this.$refs.ip);
                return;
            }

            if(this.saveInfo.accessControlGroupNoList.length == 0) {
                alertMsg("ACG는 1개 이상이어야 합니다.");
                return;
            }
            confirmMsg("생성하시겠습니까?",this.createNetwork);
        },
        createNetwork: function() {
            post(TID.SAVE,
                "/my/management/instance/vpc/createNetworkInterface",
                this.saveInfo,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case "acgList":
                    console.log(results);
                    if (results.success) {
                        if(!isNull(results.response.getAccessControlGroupListResponse)) {
                            this.accessControlGroupCbList = results.response.getAccessControlGroupListResponse.accessControlGroupList;
                        }
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }

                    let obj = {
                        selectDiv:"accessControlGroupDiv",
                        selectId:"accessControlGroupNo",
                        optionValue:"accessControlGroupNo",
                        optionName:"accessControlGroupName",
                    };
                    reloadSelect(obj, this.accessControlGroupCbList);

                    // // vpc 선택에 따라 acg selectbox 재구성
                    // if(this.bReload){
                    //     let obj = {
                    //         selectDiv:"accessControlGroupDiv",
                    //         selectId:"accessControlGroupNo",
                    //         optionValue:"accessControlGroupNo",
                    //         optionName:"accessControlGroupName",
                    //     };
                    //     reloadSelect(obj, this.accessControlGroupCbList);
                    // }
                    break;
                case TID.SAVE:
                    console.log(results);
                    this.saveCallback(results);
                    break;
            }
        },
        saveCallback: function (results) {
            console.log(results);
            if (results.success) {
                popupId = "networkInterfaceModal";
                appMain.$refs.maincontents.getNetworkInterfaceCbList();
                alertMsgRtn("정상적으로 생성되었습니다.", fnClosePopup(popupId));
            } else {
                console.log(results);
                alertMsg(results.error.message);
            }
        },

        // selectebox 이벤트(선택값을 추가한다.최대 3개)
        searchChange:function(id, data){
            if(id == "accessControlGroupNo") {
                // 선택 값이 3개 일 경우 더이상 선택 불가.
                if(this.accessControlGroupList.length == 3) {
                    alertMsg("최대 3개까지 선택 가능합니다.");
                    return;
                }
                // 이미 선택되어 있는지 확인한다.
                let ii = this.accessControlGroupList.findIndex(function (key) {
                    return key.accessControlGroupNo === data
                });

                if(ii > -1){
                    alertMsg("이미 선택된 ACG입니다.");
                    return;
                }

                let idx = this.accessControlGroupCbList.findIndex(function (key) {
                    return key.accessControlGroupNo === data
                });

                let obj = {}
                obj.accessControlGroupNo = data;
                obj.accessControlGroupName = this.accessControlGroupCbList[idx].accessControlGroupName;
                this.accessControlGroupList.push(obj);

                //this.saveInfo.accessControlGroupNoList.push(obj.accessControlGroupNo);
            }
        },
    }
});
// selectebox 이벤트
function acgChange(id){
    const data = document.querySelector('#'+id).value;
    // 선택된 값이 있는 경우 수행한다.
    if(!isNull(data)){
        appPopIf.$refs.popupif.searchChange(id, data);
    }
};
