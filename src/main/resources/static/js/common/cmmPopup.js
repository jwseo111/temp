
/*
 * @name : popAgencySearch.js
 * @date : 2021-06-23 오후 1:05
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

let appPop1;
let popAgencyTypeCode;

window.addEventListener('load', function() {
    appPop1 = new Vue({
        el: '#popupagencywrap',
    });
});
Vue.component('popupagencycontents', {
    template: "#popup-template",
    data: function () {
        return {
            cond: {
                page: 0,
                size: 5,
                agencyName: "",
                agencyTypeCode: "",
                sort: "agencyName"
            },
            agencyList: [],
            pageInfo: {
                curr : 1,
                max : 1,
                first : 1,
                last : 1,
                prev : 1,
                next : 1,
                pages: [1]
            },
            messages: "",
            alertMsg: "",
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
            this.cond.agencyTypeCode=popAgencyTypeCode;
            this.getAgencyList();
        },
        getAgencyList:function(){
            get("search",
                "/agency/list",
                this.cond,
                this.searchCallback);
        },
        searchCallback: function (tid,results) {
            if (results.success) {
                this.makePageNavi(results.response.pageable, results.response.total);
                this.agencyList = results.response.content;
            } else {
                console.log(results);
            }
        },
        makePageNavi: function (pageable, total) {
            let max = Math.ceil(total / pageable.size);
            max = max == 0 ? 1 : max;
            const curr = pageable.page + 1;

            const first = Math.ceil(curr / 5) * 5 - 4;
            let last = first + 4;
            last = last>max?max:last;
            let prev = first - 1;
            prev = prev<1?1:prev;
            let next = last + 1;
            next = next>max?max:next;

            this.first = first;
            this.max = max;
            this.curr = curr;
            this.last = last;
            this.prev = prev;
            this.next = next;

            this.pageInfo.pages = new Array();
            for (let i=first; i<=last; i++){
                this.pageInfo.pages.push(i);
            }
        },
        onclickPage : function (page){
            this.cond.page = page - 1;
            this.getAgencyList();
        },
        selectAgency : function (agency){
            callbackPopupAgency(agency);
            fnClosePopup('agencyModal');
        },
        alertClose : function(){
            this.alertMsg="";
            fnClosePopup('alertMsgModal');
        }

    }
});