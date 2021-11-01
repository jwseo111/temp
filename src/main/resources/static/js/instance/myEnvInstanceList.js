
/*
 * @name : myEnvInstanceList.js
 * @date : 2021-10-13 오후 5:57
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
let loading = true;
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
                approveStatus:"", // 학습서버 처리상태
                keyword:"",//검색어
                sort: ""
            },
            approveStatusCdList:getCodeList('ApproveStatus',this.callback),// 콤보 리스트
            envInstanceList: [],
            pageInfo: {
                curr : 1,
                max : 1,
                first : 1,
                last : 1,
                prev : 1,
                next : 1,
                pages: [1],
                total: 1
            },
        };
    },
    mounted:function(){
        this.getEnvInstanceList();

    },
    methods:{
        onKeyup:function (e){
            if (e.keyCode == 13){
                this.onclickSearch();
            }
        },
        onclickSearch: function () {
            this.cond.page = 0;
            this.cond.approveStatus = this.$refs.approveStatus.value;//처리상태
            this.getEnvInstanceList();
        },

        // 목록 클릭(상세화면 이동)
        onclickView: function (reqSeq) {
            let uri = "/my/envInstance/view?menuId="+myMenuId+"&reqSeq=";
            location.href = uri + reqSeq;

        },
        // 목록 조회
        getEnvInstanceList:function () {
            let pageUri = "";
            if(role === "ROLE_ADMIN") {
                pageUri = "/env/instance/getList";
            } else if(role === "ROLE_USER") {
                pageUri = "/my/management/env/instance/getList";
            }
            get(TID.SEARCH,
                pageUri,
                this.cond,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case "ApproveStatus":
                    console.log(results.response);
                    this.approveStatusCdList = results.response;
                    setTimeout(function() {
                        loadSelect();
                    },300);
                    break;
            }
        },
        login: function() {
            location.href = "/login";
        },
        searchCallback: function (results) {
            if (results.success) {
                console.log(results.response.content);
                this.makePageNavi(results.response.pageable, results.response.total);
                this.envInstanceList = results.response.content;
                this.loading = false;
            } else {
                //console.log(results);
                alertMsg(results.error.message);
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

            this.pageInfo.first = first;
            this.pageInfo.max = max;
            this.pageInfo.curr = curr;
            this.pageInfo.last = last;
            this.pageInfo.prev = prev;
            this.pageInfo.next = next;
            this.pageInfo.total=total;

            this.pageInfo.pages = new Array();
            for (let i=first; i<=last; i++){
                this.pageInfo.pages.push(i);
            }
        },
        onclickPage : function (page){
            if(page === this.pageInfo.curr){
            } else {
                this.cond.page = page - 1;
                this.pageInfo.curr = page;
                this.getEnvInstanceList();
            }
        },
    }
});

