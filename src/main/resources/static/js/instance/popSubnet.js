
/*
 * @name : popSubnet.js
 * @date : 2021-09-23 오전 10:46
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appPopSubet;
window.addEventListener('load', function() {
    appPopSubet = new Vue({
        el: '#popupwrapsubnet',
    });
});
Vue.component('popupsubnet', {
    template: "#popup-template-subnet",
    data: function () {
        return {
            saveInfo:{
                subnetName:"", // subnet 이름
                vpcNo:"",
                vpcName:"",
                subnet:"", // IP 허용범위
                zoneCode:"", // 가용ZONE
                networkAclNo:"",
            },
            vpcList: [], // VPC 콤보 리스트
            vpcDecs:"",
            zoneCbList: [], // 가용Zone 콤보 리스트
            aclCbList: [], // ACL 콤보 리스트
            nameChk1 : false, // 최소 3글자 이상, 최대 30자까지만 입력이 가능합니다
            nameChk2 : false, // 소문자, 숫자,"-"의 특수문자만 허용하며 알파벳 문자로 시작해야 합니다.
            nameChk3 : false, // 영어 또는 숫자로 끝나야 합니다.
            namePass : false,

            ipChk1: false, // 잘못된 형식입니다.
            ipPass : true,
        };
    },
    mounted: function () {

        this.getZoneCbList(); // 가용 ZONE 콤보 조회
        //this.getAclCbList(); // ACL 콤보 조회
    },
    methods: {
        onload: function() {
            let vpcList = appMain.$refs.maincontents.vpcList;
            let vpcNo = this.saveInfo.vpcNo;
            let idx = vpcList.findIndex(function(key) {return key.vpcNo === vpcNo});
            this.vpcDecs=  vpcList[idx].vpcName +"("+vpcList[idx].ipv4CidrBlock+")";

            // 초기화
            this.saveInfo.subnetName = "";
            this.saveInfo.subnet = "";
            this.getAclCbList();
        },
        onKeyupName:function (e){
            let str = e.target.value;
            let ll = str.length;
            let exp1 = /[a-z0-9]/; // 영문 또는 숫자 체크
            let exp2 = /^[a-z]{1}[a-z0-9-]+$/; // 첫문자는 소문자, 소문자, 숫자, 하이픈 허용
            if(str.length < 3 || str.length > 30){
                this.nameChk1 = true;
                this.nameChk2 = false;
                this.nameChk3 = false;
                this.namePass = false;
            } else if(!exp2.test(str)) { // 소문자, 숫자, 특수문자(-)만 허용
                this.nameChk1 = false;
                this.nameChk2 = true;
                this.nameChk3 = false;
                this.namePass = false;
            } else if(!exp1.test(str[ll-1])) { // 마지막 문자는 소문자 or 숫자
                this.nameChk1 = false;
                this.nameChk2 = false;
                this.nameChk3 = true;
                this.namePass = false;
            } else {
                this.nameChk1 = false;
                this.nameChk2 = false;
                this.nameChk3 = false;
                this.namePass = true;
            }
        },
        onKeyupIp:function (e){

            let str = e.target.value;
            let ll = str.length;
            let exp = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\/(?:[0-9]|[1-2][0-9]|3[0-2]|[01]?)$/;

            if(!exp.test(str)) {                // 잘못된 형식입니다.
                this.ipChk1 = true;
                this.ipPass = false;
            } else {
                this.ipChk1 = false;
                this.ipPass = true;
            }
        },
        onclickPop: function (popId) {
            console.log("생성 팝업 : " + popId);//tmp
            //appPopAcl.$refs.popupacl.$data.saveInfo.createNetworkAclRequestDto.vpcNo = this.$refs.vpcNo.value;
            appPopAcl.$refs.popupacl.onload(this.$refs.vpcNo.value);

            document.getElementById(popId).style.display = "block";
            document.documentElement.style.overflowX = 'hidden';
            document.documentElement.style.overflowY = 'hidden';
        },
        // 가용Zone 콤보 리스트 조회
        getZoneCbList: function() {
            get("zoneCbList",
                "/my/management/instance/vpc/server/getZoneList",
                {},
                this.callback);
        },
        // ACL 콤보 리스트 조회
        getAclCbList: function() {
            let cond = {vpcNo : ""};
            cond.vpcNo = this.saveInfo.vpcNo;
            get("aclCbList",
                "/my/management/instance/vpc/getNetworkAclList",
                cond,
                this.callback);
        },

        onclickCreateSave: function() {
            console.log("subnet 생성 클릭");//tmp
            let param = [
                {value:this.saveInfo.subnetName, title:"Subnet 이름 ", ref:this.$refs.subnetName},
                {value:this.saveInfo.subnet,     title:"IP 주소 범위 ", ref:this.$refs.subnet},
            ];
            if(!isValid(param)) return false;

            if(!this.namePass){
                alertMsg("Subnet 이름을 확인하세요.",this.$refs.subnetName);
                return false;
            }

            if(!this.ipPass){
                alertMsg("IP 주소 범위를 확인하세요.",this.$refs.subnet);
                return false;
            }

            if(!this.$refs.zoneCode.value){
                alertMsg("가용Zone을 선택해주세요.", this.$refs.zoneCode );
                return false;
            } else {
                this.saveInfo.zoneCode = this.$refs.zoneCode.value;
            }

            if(!this.$refs.networkAclNo.value) {
                alertMsg("Network ACL을 선택해주세요.", this.$refs.networkAclNo );
                return false;
            } else {
                this.saveInfo.networkAclNo = this.$refs.networkAclNo.value;
            }
            console.log(JSON.stringify(this.saveInfo));//tmp
            confirmMsg("생성하시겠습니까?", this.save);
        },
        save: function(){
            post(TID.SAVE,
                "/my/management/instance/vpc/createSubnet",
                this.saveInfo,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case "zoneCbList":
                    console.log("zoneCbList=================");
                    console.log(results);
                    if (results.success) {
                        this.zoneCbList = results.response.getZoneListResponse.zoneList;
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    break;
                case "aclCbList":
                    console.log("aclCbList=================");
                    console.log(results);
                    if (results.success) {
                        this.aclCbList = results.response.getNetworkAclListResponse.networkAclList;
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    // subnet 선택에 따라 acl selectbox 재구성
                    let obj = {
                        selectDiv:"aclDiv",
                        selectId:"networkAclNo",
                        optionValue:"networkAclNo",
                        optionName:"networkAclName",
                    };
                    reloadSelect(obj, this.aclCbList);
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
                // alertMsgRtn("정상적으로 생성되었습니다.", fnClosePopup('subnetModal'));
                appMain.$refs.maincontents.getSubnetList();
                alertMsgRtn("정상적으로 생성되었습니다.", fnClosePopup('subnetModal'));
            } else {
                console.log(results);
                alertMsg(results.error.message);
            }
        },

    }        // // 검색 selectebox 이벤트
    // searchChange:function(data){
    //     this.saveInfo.zoneCode = data;
    // }
});

// // 검색 selectebox 이벤트
// function selectChange(){
//     const data= document.querySelector("#zoneCode").value;
//     appPopSubet.$refs.popupsubnet.searchChange(data);
// }