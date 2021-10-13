
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
            message:{
                vpcName : "",
                ipv4CidrBlock:"",// IP
            },
            pass :{
                vpcName : "",
                ipv4CidrBlock:"",// IP
            },

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

            if(str.length < 3 || str.length > 30){// 최소 3글자 이상, 최대 30자까지만 입력이 가능합니다
                this.pass.vpcName = false;
                this.message.vpcName = "최소 3글자 이상, 최대 30자까지만 입력이 가능합니다.";
            } else if(!exp2.test(str)) { // 소문자, 숫자,"-"의 특수문자만 허용하며 알파벳 문자로 시작해야 합니다.
                this.pass.vpcName = false;
                this.message.vpcName = "소문자, 숫자,\"-\"의 특수문자만 허용하며 알파벳 문자로 시작해야 합니다.";
            } else if(!exp1.test(str[ll-1])) { /// 영어 또는 숫자로 끝나야 합니다.
                this.pass.vpcName = false;
                this.message.vpcName = "영어 또는 숫자로 끝나야 합니다.";
            } else {
                this.pass.vpcName = true;
                this.message.vpcName = "";
            }
        },
        onKeyupIp:function (e){

            let str = e.target.value;
            let ll = str.length;

            let exp = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\/(?:[0-9]|[1-2][0-9]|3[0-2]|[01]?)$/;

            if(!exp.test(str)) {                // 잘못된 형식입니다.
                this.pass.ipv4CidrBlock = false;
                this.message.ipv4CidrBlock = "잘못된 형식입니다.";
            } else {
                this.pass.ipv4CidrBlock = true;
                this.message.ipv4CidrBlock = "";
            }
        },
        onclickCreateSave: function() {

            this.saveInfo.vpcName = this.saveInfo.vpcName.replace(/(\s*)/g,""); // vpc 이름 공백 제거
            if(!this.saveInfo.vpcName){
                alertMsg("VPC 이름은 필수입니다.",this.$refs.vpcName);
                return false;
            }
            if(!this.pass.vpcName){
                alertMsg("VPC 이름을 확인하세요.",this.$refs.vpcName);
                return false;
            }


            if(!this.pass.ipv4CidrBlock){
                alertMsg("IP 주소 범위를 확인하세요.",this.$refs.ipv4CidrBlock);
                return false;
            }

            confirmMsg("생성하시겠습니까?", this.save);

        },
        save: function(){
            console.log("VPC 생성 : " + JSON.stringify(this.saveInfo));//tmp
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

