
/*
 * @name : inquiryReply.js
 * @date : 2021-08-23 오후 2:59
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH : {value: 0, name: "search", code: "S"},
    SAVE   : {value: 0, name: "save", code: "I"},
    UPLOAD : {value : 0, name : "upload", code : "I"}
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
            inquiryReg : true,
            inquiry : [], // 문의하기 상세
            inquiryFiles : [],
            userInfo : [],
            messages : "",
            replyInfo: {
                orgInquirySeq : inquirySeq,
                answerYn : "Y",
                contents : "",
            },
            maxFileCnt : 5,
            maxFileSize : 5,
        };
    },
    mounted:function(){
        this.getUserInfo();
        if(!isNull(this.inquirySeq)){
            this.inquiryReg = false;
            this.getInquiryView();
        }
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
        onChange: function(e, idx) {
            document.getElementById("uploadText"+idx).value = e.target.files[0].name;
            if(!this.fileSizeChk(e.target.files[0])) {
                alertMsg("첨부파일 사이즈는  5MB 이내로 등록 가능합니다.");
                document.getElementById("uploadFile"+idx).value = "";
                document.getElementById("uploadText"+idx).value = "";
                return false;
            }
        },
        // // 신규 첨부파일 삭제
        // onClickFileDelete: function (idx) {
        //     document.getElementById("uploadFile"+idx).value = "";
        //     document.getElementById("uploadText"+idx).value = "";
        // },
        // // 기존 첨부파일 삭제
        // onClickOrgFileDelete: function (fileSeq) {
        //     let idx = this.inquiryFiles.findIndex(function(key) {return  key.fileSeq === fileSeq});
        //     this.inquiryFiles.splice(idx, 1);
        // },
        // 취소 클릭(상세보기 이동)
        onclickCancel: function () {
            this.onclickView();
        },
        // (상세보기)
        onclickView: function () {
            location.href = "/board/inquiry/view?inquirySeq="+this.inquirySeq;
        },
        // 저장 메소드 호출
        onclickSave:function () {
            if(!this.replyInfo.contents){
                alertMsg("답변내용은 필수입니다.", this.$refs.contents );
                return false;
            }

            confirmMsg("저장하시겠습니까?",this.save);
        },
        save: function() {
            console.log("원본 넘버 : " );
            console.log(this.replyInfo.orgInquirySeq);//tmp
            console.log("contents : " + this.replyInfo.contents);//tmp
            console.log("answerYn : " + this.replyInfo.answerYn);//tmp
            post(TID.SAVE,
                "/board/inquiry/save",
                this.replyInfo,
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
                        //alertMsg(results.error.message);
                    }
                    break;
                case TID.SAVE:
                    //console.log(results);
                    this.saveCallback(results);
                    break;
                case TID.UPLOAD:
                    if (results.success) {
                        //console.log(results);
                        alertMsgRtn("정상적으로 저장되었습니다.",this.onclickView);
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                console.log(results);
                this.inquiry      = results.response;
                this.inquiryFiles = results.response.inquiryFiles;

            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
        saveCallback: function (results) {
            if (results.success) {
                alertMsgRtn("정상적으로 저장되었습니다.",this.onclickView);//tmp
                // // 저장 성공시 첨부파일이 있을 경우 첨부파일 등록 로직 추가
                // const frm = new FormData();
                // let cnt = document.getElementsByName("uploadFile").length;
                // for(let i=1; i<cnt+1; i++) {
                //     let file = document.getElementById("uploadFile"+i).files;
                //     if(file[0]){ // 첨부된 파일 있음
                //         frm.append("multipartFile", file[0]);
                //     }
                // }
                //
                // if(frm.getAll("multipartFile").length > 0) {
                //     this.inquirySeq = results.response.inquirySeq;
                //     frm.append("inquirySeq", this.inquirySeq);
                //     fileUpload(TID.UPLOAD, "/board/inquiry/file/upload", frm, this.callback);
                // } else {
                //     //this.inquirySeq = results.response.inquirySeq;
                //     alertMsgRtn("정상적으로 저장되었습니다.",this.onclickView);
                // }
            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
        fileSizeChk: function(file) {
            let maxSize = this.maxFileSize * 1024 * 1024; // 5MB
            let fileSize = file.size;

            if(fileSize > maxSize) {
                return false;
            } else {
                return true;
            }
        },
        // 검색 selectebox 이벤트
        searchChange:function(data){
            this.saveInfo.inquiryType = data;
        }
    }
});

// 검색 selectebox 이벤트
function selectChange(){
    const data= document.querySelector("#inquiryType").value;
    appMain.$refs.maincontents.searchChange(data);
}
function fileUpload(tid, uri, formData, callback){

    const headers = {
        'Content-Type': 'multipart/form-data'
    };
    const config = {
        //onUploadProgress: progressEvent => progEvent(progressEvent),
        headers: headers
    };
    openLoading();

    axios.post(uri, formData, config)
        .then((response) => {
            // 응답 처리
            callback(tid, response.data);
            closeLoading();
        })
        .catch((error) => {
            // 예외 처리
            if (error.response) {
                // 요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.
                callback(tid, error.response.data);
                closeLoading();
            }
            else if (error.request) {
                // 요청이 이루어 졌으나 응답을 받지 못했습니다.
                // `error.request`는 브라우저의 XMLHttpRequest 인스턴스 또는
                // Node.js의 http.ClientRequest 인스턴스입니다.
                // console.log(error.request);
            }
            else {
                // 오류를 발생시킨 요청을 설정하는 중에 문제가 발생했습니다.
                // console.log('Error', error.message);
            }
        })
}