
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
            }
        };
    },
    mounted: function () {

    },
    methods: {
        onclickCreateSave: function() {
            console.log("VPC 생성 클릭");//tmp
            console.log("VPC 이름 : " +  this.saveInfo.vpcName);//tmp
            console.log("VPC ipAddress : " +  this.saveInfo.ipv4CidrBlock);//tmp
            if(!this.saveInfo.vpcName){
                alertMsg("VPC 이름은 필수입니다.",this.$refs.vpcName);
                return false;
            }
            if(!this.saveInfo.ipv4CidrBlock){
                alertMsg("IP 주소 범위는 필수입니다.",this.$refs.ipv4CidrBlock);
                return false;
            }
            confirmMsg("생성하시겠습니까?", this.save);

        },
        save: function(){
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
            if (results.success) {
                alertMsgRtn("정상적으로 생성되었습니다.", fnClosePopup('vpcModal'));
            } else {
                console.log(results);
                alertMsg(results.error.message);
            }
        },


    }
});

