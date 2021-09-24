
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
Vue.component('popupcontentsif', {
    template: "#popup-templateif",
    data: function () {
        return {
            networkInterface: [],
            messages: "",

            interfaceName:"new interface",
            memo:"",
            selected:"", // 선택된 콤보박스
            selectList:     getCodeList('ReqStorageStat',this.callback),//
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
        onclickSearch: function () {
            this.cond.page = 0;
            //this.getNetworkInterfaceList();
        },
        getNetworkInterfaceList:function(){
            // get("search",
            //     "/agency/list",
            //     this.cond,
            //     this.searchCallback);
        },
        onclickCancel: function() {

        },
        onclickReqSave: function() {

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

