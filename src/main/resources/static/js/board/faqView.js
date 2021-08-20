
/*
 * @name : faqView.js
* @date : 2021-08-17 오전 9:32
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH : {value: 0, name: "search", code: "S"}
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
            faqSeq: faqSeq,
            userInfo : [],
            faq: [],
            messages : "",
        };
    },
    mounted:function(){
        this.getFaqView();
        this.getUserInfo();
    },
    methods:{
        // FAQ 상세 조회
        getFaqView:function (){
            get(TID.SEARCH,
                "/board/faq/get/"+this.faqSeq,
                {},
                this.callback);
        },
        getUserInfo: function(){
            get("usrInfo","/user/my/info",{},this.callback);
        },
        // 취소 클릭(목록 이동)
        onclickList: function () {
            location.href = "/board/faq/main";
        },

        // 수정
        onclickModify: function () {
            confirmMsg("수정하시겠습니까?", this.mod);
        },
        mod: function() {
            location.href = "/board/faq/reg?faqSeq="+this.faqSeq;
        },
        // 삭제
        onclickDelete: function () {
            confirmMsg("삭제하시겠습니까?", this.del);
        },
        del: function() {
            post(TID.DELETE,
                "/board/faq/delete/"+this.faqSeq,
                {},
                this.callback);
        },
        // 제목 클릭(상세보기)
        onclickView: function (faqSeq) {
            location.href = "/board/faq/view?faqSeq="+faqSeq;
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
                case TID.DELETE:
                    if (results.success) {
                        alertMsgRtn("정상적으로 삭제되었습니다.",this.onclickList);
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    break;

            }
        },
        searchCallback: function (results) {
            if (results.success) {
                this.faq = results.response;
            } else {
                alertMsg(results.error.message);
            }
        },

    }
});

