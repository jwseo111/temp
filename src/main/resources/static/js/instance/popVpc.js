
/*
 * @name : popVpc.js
 * @date : 2021-09-23 오전 10:46
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appPopVpc;
window.addEventListener('load', function() {
    appPopVpc = new Vue({
        el: '#popupwrapvpc',
    });
});
Vue.component('popupvpc', {
    template: "#popup-template-vpc",
    data: function () {
        return {
            saveInfo : {
                vpcName: "", // VPC 이름
                ipv4CidrBlock: "" // IP 주소 범위
            },
            vpcNameChk1 : false, // 최소 3글자 이상, 최대 30자까지만 입력이 가능합니다
            vpcNameChk2 : false, // 소문자, 숫자,"-"의 특수문자만 허용하며 알파벳 문자로 시작해야 합니다.
            vpcNameChk3 : false, // 영어 또는 숫자로 끝나야 합니다.
            vpcNamePass : false,

            ipChk1: false, // 잘못된 형식입니다.
            ipPass : true,

        };
    },
    mounted: function () {

    },
    methods: {
        onload: function(){
            this.saveInfo = {vpcName:"", ipv4CidrBlock: ""};
        },
        onKeyupName:function (e){

            let str = e.target.value;
            let ll = str.length;

            let exp1 = /[a-z0-9]/; // 영문 또는 숫자 체크
            let exp2 = /^[a-z]{1}[a-z0-9-]+$/; // 첫문자는 소문자, 소문자, 숫자, 하이픈 허용

            if(str.length < 3 || str.length > 30){
                this.vpcNameChk1 = true;
                this.vpcNameChk2 = false;
                this.vpcNameChk3 = false;
                this.vpcNamePass = false;
            } else if(!exp2.test(str)) { // 소문자, 숫자, 특수문자(-)만 허용
                this.vpcNameChk1 = false;
                this.vpcNameChk2 = true;
                this.vpcNameChk3 = false;
                this.vpcNamePass = false;
            } else if(!exp1.test(str[ll-1])) { // 마지막 문자는 소문자 or 숫자
                this.vpcNameChk1 = false;
                this.vpcNameChk2 = false;
                this.vpcNameChk3 = true;
                this.vpcNamePass = false;
            } else {
                this.vpcNameChk1 = false;
                this.vpcNameChk2 = false;
                this.vpcNameChk3 = false;
                this.vpcNamePass = true;
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
        onclickCreateSave: function() {
            console.log("VPC 생성 클릭");//tmp
            console.log("VPC 이름 : " +  this.saveInfo.vpcName);//tmp
            console.log("VPC ipAddress : " +  this.saveInfo.ipv4CidrBlock);//tmp

            this.saveInfo.vpcName = this.saveInfo.vpcName.replace(/(\s*)/g,""); // vpc 이름 공백 제거
            if(!this.saveInfo.vpcName){
                alertMsg("VPC 이름은 필수입니다.",this.$refs.vpcName);
                return false;
            }
            if(!this.vpcNamePass){
                alertMsg("VPC 이름을 확인하세요.",this.$refs.vpcName);
                return false;
            }

            if(!this.saveInfo.ipv4CidrBlock){
                alertMsg("IP 주소 범위는 필수입니다.",this.$refs.ipv4CidrBlock);
                return false;
            }
            if(!this.ipPass){
                alertMsg("IP 주소 범위를 확인하세요.",this.$refs.ipv4CidrBlock);
                return false;
            }

            confirmMsg("생성하시겠습니까?", this.save);

        },
        save: function(){
            console.log("VPC 생성 : " + JSON.stringify(this.saveInfo));//tmp
            //alertMsgRtn("정상적으로 생성되었습니다.", fnClosePopup('vpcModal'));
            post(TID.SAVE,
                "/my/management/instance/vpc/createVpc",
                this.saveInfo,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SAVE:
                    console.log(results);
                    this.saveCallback(results);
                    break;
            }
        },
        saveCallback: function (results) {
            console.log(results);
            if (results.success) {
                //alertMsgRtn("정상적으로 생성되었습니다.", fnClosePopup('vpcModal'));
                appMain.$refs.maincontents.getVpcList(true); // reload = true
                alertMsgRtn("정상적으로 생성되었습니다.", appMain.$refs.maincontents.vpcCbListReload());
            } else {
                console.log(results);
                alertMsg(results.error.message);
            }
        },


    }
});

