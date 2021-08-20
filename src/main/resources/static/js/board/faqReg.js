
/*
 * @name : faqReg.js
 * @date : 2021-08-17 오전 9:32
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH_USER : {value: 0, name: "search", code: "S"},
    SAVE        : {value: 0, name: "save", code: "I"}
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
            faqSeq : faqSeq,
            faqReg : true,
            faq : [],
            userInfo : [],
            messages : "",
            // selected:"", // 선택된 콤보박스(문의유형)
            questionTypeList : getCodeList('QuestionType',this.callback), // 콤보박스 리스트
            saveInfo: {
                title : "",
                contents : "",
                questionType:""
            },

        };
    },
    mounted:function(){
        this.getUserInfo();
        if(!isNull(this.faqSeq)){
            this.faqReg = false;
            this.getFaqView();
        }


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
        onclickCancel: function () {
            if(this.faqSeq) { // 수정중
                location.href = "/board/faq/view?faqSeq="+this.faqSeq;
            } else { // 신규등록
                location.href = "/board/faq/main";
            }
        },
        // (상세보기)
        onclickView: function () {
            location.href = "/board/faq/view?faqSeq="+this.faqSeq;
        },

        // 저장 메소드 호출
        onclickSave:function () {
            if(!this.saveInfo.title){
                alertMsg("제목은 필수입니다.", this.$refs.title );
                return false;
            }

            if(!this.saveInfo.questionType){
                alertMsg("문의유형을 선택해주세요.", this.$refs.questionType );
                return false;
            }

            if(!this.saveInfo.contents){
                alertMsg("내용은 필수입니다.", this.$refs.contents );
                return false;
            }

            confirmMsg("저장하시겠습니까?",this.save);
        },
        save: function() {
            post(TID.SAVE,
                "/board/faq/save",
                this.saveInfo,
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
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    break;
                case "QuestionType":
                    this.questionTypeList = results.response;
                    setTimeout(function() {
                        loadSelect();
                    },300);
                    break;
                case TID.SAVE:
                    //console.log(results);
                    this.saveCallback(results);
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                this.faq      = results.response;
                this.saveInfo.title = this.faq.title;
                this.saveInfo.contents = this.faq.contents;
                this.saveInfo.faqSeq = this.faq.faqSeq;
                this.saveInfo.questionType  = this.faq.questionType.name;
            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
        saveCallback: function (results) {
            if (results.success) {
                this.faqSeq = results.response.faqSeq;
                alertMsgRtn("정상적으로 저장되었습니다.",this.onclickView);
            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
        // 검색 selectebox 이벤트
        searchChange:function(data){
            this.saveInfo.questionType = data;
        }
    }
});

// 검색 selectebox 이벤트
function selectChange(){
    const data= document.querySelector("#questionType").value;
    appMain.$refs.maincontents.searchChange(data);
}