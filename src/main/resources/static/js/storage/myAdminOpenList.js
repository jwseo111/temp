
/*
 * @name : myOpenList.js
 * @date : 2021-07-07 오전 11:05
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
let openStorageStatCode;
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
                openDataName: "",
                openStorageStatCode: "",
                sort: ""
            },
            openStorageList: [],
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
            openStorageStatCd : "", // 선택된 콤보박스
            openStorageStatCdList : getCodeList('OpenStorageStat',this.callback), // 상태콤보박스 리스트
//            isYn : getCodeList('IsYn',this.callback)

        };
    },
    mounted:function(){
        this.getMyOpenStorageList();

    },
    methods:{
        onKeyup:function (e){
            if (e.keyCode == 13){
                this.onclickSearch();
            }
        },
        onclickSearch: function () {
            this.cond.page = 0;
            this.getMyOpenStorageList();
        },
        // 목록 > 신청번호 클릭(화면 이동)
        onclickReq: function (openStorageId) {
            location.href = "/my/admin/open/view?menuId="+myMenuId+"&openStorageId="+openStorageId;
        },

        getMyOpenStorageList:function () {
            get(TID.SEARCH,
                "/storage/open/list",
                this.cond,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case "OpenStorageStat":
                    this.openStorageStatCdList = results.response;
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {

                this.makePageNavi(results.response.pageable, results.response.total);
                this.openStorageList = results.response.content;
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
            this.getMyOpenStorageList();
        },
    }
});

