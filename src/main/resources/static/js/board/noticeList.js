
/*
 * @name : noticeList.js
 * @date : 2021-08-12 오후 3:27
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
                keyword: "",
                sort: "importantYn"
            },
            noticeList: [],
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
            messages : "",
            userInfo:[],

        };
    },
    mounted:function(){
        this.getNoticeList();
        this.getUserInfo();
    },
    methods:{
        onKeyup:function (e){
            if (e.keyCode == 13){
                this.onclickSearch();
            }
        },
        onclickSearch: function () {
            this.cond.page = 0;
            this.getNoticeList();
        },
        getUserInfo: function(){
            get("usrInfo","/user/my/info",{},this.callback);
        },
        // 목록 > 등록 클릭(화면 이동)
        onclickReg: function () {
            location.href = "/board/notice/reg";
        },
        // 목록 > 제목 클릭(상세보기)
        onclickView: function (noticeSeq) {
            location.href = "/board/notice/view?noticeSeq="+noticeSeq;
        },
        getNoticeList:function () {
            get(TID.SEARCH,
                "/board/notice/list",
                this.cond,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case "usrInfo":
                    if (results.success) {
                        this.userInfo = results.response;
                    } else {
                        console.log(results);
                    }
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                //console.log(results);
                this.makePageNavi(results.response.pageable, results.response.total);
                this.noticeList = results.response.content;
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
            //this.total = Math.ceil(total / pageable.size);
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
                this.getNoticeList();
            }
        },
    }
});

