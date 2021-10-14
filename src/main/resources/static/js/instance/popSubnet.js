
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
            vpcDesc:"",
            zoneCbList: [], // 가용Zone 콤보 리스트
            aclCbList: [], // ACL 콤보 리스트
            message:{
                subnetName : "",
                subnet:"",// IP 허용범위
            },
            pass :{
                subnetName : "",
                subnet:"",// IP 허용범위
            },
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
            this.vpcDesc=  vpcList[idx].vpcName +"("+vpcList[idx].ipv4CidrBlock+")";

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
            if(str.length < 3 || str.length > 30){ // 최소 3글자 이상, 최대 30자까지만 입력이 가능합니다
                this.pass.subnetName = false;
                this.message.subnetName = "최소 3글자 이상, 최대 30자까지만 입력이 가능합니다.";
            } else if(!exp2.test(str)) { // 소문자, 숫자,"-"의 특수문자만 허용하며 알파벳 문자로 시작해야 합니다.
                this.pass.subnetName = false;
                this.message.subnetName = "소문자, 숫자,\"-\"의 특수문자만 허용하며 알파벳 문자로 시작해야 합니다.";
            } else if(!exp1.test(str[ll-1])) { // 영어 또는 숫자로 끝나야 합니다.
                this.pass.subnetName = false;
                this.message.subnetName = "영어 또는 숫자로 끝나야 합니다.";
            } else {
                this.pass.subnetName = true;
                this.message.subnetName = "";
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

            let param = [
                {value:this.saveInfo.subnetName, title:"Subnet 이름 ", ref:this.$refs.subnetName},
                {value:this.saveInfo.subnet,     title:"IP 주소 범위 ", ref:this.$refs.subnet},
            ];
            if(!isValid(param)) return false;

            if(!this.pass.subnetName){
                alertMsg("Subnet 이름을 확인하세요.",this.$refs.subnetName);
                return false;
            }

            if(!this.pass.subnet){
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

    }
});

