
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
            vpcName:"",
            ipAddress:"",
        };
    },
    mounted: function () {

    },
    methods: {
        onKeyup:function (e){
            if (e.keyCode == 13){
                this.onclickSearch();
            }
        },
        // onclickSearch: function () {
        //     this.cond.page = 0;
        //     this.cond.agencyTypeCode=popAgencyTypeCode;
        //     this.getAgencyList();
        // },
        // getAgencyList:function(){
        //     get("search",
        //         "/agency/list",
        //         this.cond,
        //         this.searchCallback);
        // },
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

