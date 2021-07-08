/*
 * @name : myStorageList.js
 * @date : 2021-06-28 오후 1:05
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH      : {value: 0, name: "search", code: "S"}
};
window.onload = function(){
    appMain = new Vue({
        el: '#maincontentswrap',
    });
}

Vue.component('maincontents', {
    template : "#main-template",
    data: function() {
        return {
            cond: {
                page: 0,
                size: 5,
                dataName: "",
                reqStorageStatCode: reqStorageStatCode,
                sort: ""
            },
            reqStorageList: [],
            pageInfo: {
                curr : 1,
                max : 1,
                first : 1,
                last : 1,
                prev : 1,
                next : 1,
                pages: [1]
            },
            messages : "",
            reqStoreStatCd : "",
            reqStorageId : "",
            reqStoreStatCdList : getCodeList('ReqStorageStat',this.callback),
            isYn : getCodeList('IsYn',this.callback)

        };
    },
    mounted:function(){
        this.getMyReqStorageList();
    },
    methods:{
        onKeyup:function (e){
            if (e.keyCode == 13){
                this.onclickSearch();
            }
        },
        onclickSearch: function () {
            this.cond.page = 0;
            this.cond.reqStorageStatCode = this.reqStoreStatCd;
            this.getMyReqStorageList();
        },
        // 목록 > 신청번호 클릭(화면 이동)
        onclickReq: function (reqStorageId) {
           location.href = "/my/store/req?reqStorageId="+reqStorageId;
        },

        getMyReqStorageList:function () {
            get(TID.SEARCH,
                "/my/management/storage/req/list",
                this.cond,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case "ReqStorageStat":
                    console.log(results.response);
                    this.reqStoreStatCdList = results.response;
                    break;
                case "IsYn":
                    console.log(results.response);
                    this.isYn = results.response;
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                //console.log(results.response);
                this.makePageNavi(results.response.pageable, results.response.total);
                this.reqStorageList = results.response.content;
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
            this.getReqStorageList();
        },
    }
});

