
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
            subnetName:"",
            ipAddress:"",
            selected:"", // 선택된 콤보박스
            selectList:     getCodeList('ReqStorageStat',this.callback),//
        };
    },
    mounted: function () {

    },
    methods: {
        onclickPop: function (popId) {
            console.log("생성 팝업 : " + popId);//tmp
            //document.getElementById("vpcModal").style.display = "block";
            document.getElementById(popId).style.display = "block";
            document.documentElement.style.overflowX = 'hidden';
            document.documentElement.style.overflowY = 'hidden';
        },
        onclickCancel: function() {

        },
        onclickReqSave: function() {
            console.log("VPC 생성 클릭");//tmp
            console.log("VPC 이름 : " +  this.vpcName);//tmp

        },

        callback: function (tid, results) {
            switch (tid) {
                case "ReqStorageStat":
                    //console.log(results.response);
                    // this.interfaceList = results.response;
                    // this.subnetList = results.response;
                    this.selectList = results.response;

                    break;
            }
        },
        searchCallback: function (tid,results) {
            if (results.success) {

                this.agencyList = results.response.content;
            } else {
                console.log(results);
            }
        },



    }
});

