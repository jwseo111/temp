
/*
 * @name : inquiryView.js
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
            inquirySeq : inquirySeq,
            userInfo : [],
            isAdmin : false,
            inquiry : [], // 문의하기 상세
            replyInfo : [], // 답변 상세
            messages : "",
        };
    },
    mounted:function(){
        this.getInquiryView();
        this.getUserInfo();
    },
    methods:{
        // 문의하기 상세 조회
        getInquiryView:function (){
            get(TID.SEARCH,
                "/board/inquiry/get/"+this.inquirySeq,
                {},
                this.callback);
        },
        getUserInfo: function(){
            get("usrInfo","/user/my/info",{},this.callback);
        },
        // 취소 클릭(목록 이동)
        onclickList: function () {
            location.href = "/board/inquiry/main";
        },
        // 답변등록
        onclickReplyReg: function () {
            confirmMsg("답변을 등록하시겠습니까?", this.reply);
        },
        // 답변수정
        onclickReplyMod: function () {
            confirmMsg("답변을 수정하시겠습니까?", this.reply);
        },
        // 답변 등록/수정
        reply: function() {
            location.href = "/board/inquiry/reply?inquirySeq="+this.inquirySeq;
        },
        // 답변삭제
        onclickReplyDel: function () {
            confirmMsg("답변을 삭제하시겠습니까?", this.replyDelete);
        },
        replyDelete: function() {
            post(TID.DELETE,
                "/board/inquiry/delete/"+this.replyInfo.inquirySeq,
                {},
                this.callback);
        },
        // 수정
        onclickMod: function () {
            if(this.inquiry.answerYn == "Y") {
                alertMsg("답변이 등록되어 수정할수 없습니다.");
                return;
            }
            confirmMsg("수정하시겠습니까?", this.mod);
        },
        mod: function() {
            location.href = "/board/inquiry/reg?inquirySeq="+this.inquirySeq;
        },
        // 삭제
        onclickDel: function () {
            confirmMsg("삭제하시겠습니까?", this.del);
        },
        del: function() {
            post(TID.DELETE,
                "/board/inquiry/delete/"+this.inquirySeq,
                {},
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
                        this.isAdmin = this.userInfo.userRole=="ADMIN"?true:false;
                    } else {
                        console.log(results);
                        //alertMsg(results.error.message);
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
                this.inquiry      = results.response;
                this.replyInfo      = results.response.children[0];
                this.inquiry.answerYn = results.response.children.length>0?"Y":"N";

            } else {
                alertMsgRtn(results.error.message, this.onclickList);
            }
        },

    }
});

