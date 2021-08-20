
/*
 * @name : noticeReg.js
 * @date : 2021-08-13 오후 3:22
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
            noticeSeq : noticeSeq,
            noticeReg : true,
            notice : [],
            noticeFiles : [],
            userInfo : [],
            messages : "",
            saveInfo: {
                title : "",
                importantYn : "",
                contents : ""
            },
            maxFileCnt : 5,
            maxFileSize : 5,
        };
    },
    mounted:function(){
        this.getUserInfo();
        if(!isNull(this.noticeSeq)){
            this.noticeReg = false;
            this.getNoticeView();
        }
    },
    methods:{
        // 공지사항 상세 조회
        getNoticeView:function (){
            get(TID.SEARCH,
                "/board/notice/get/"+this.noticeSeq,
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
        // 신규 첨부파일 삭제
        onClickFileDelete: function (idx) {
            document.getElementById("uploadFile"+idx).value = "";
            document.getElementById("uploadText"+idx).value = "";
        },
        // 기존 첨부파일 삭제
        onClickOrgFileDelete: function (fileSeq) {
            let idx = this.noticeFiles.findIndex(function(key) {return  key.fileSeq === fileSeq});
            this.noticeFiles.splice(idx, 1);
        },
        // 취소 클릭(목록 이동)
        onclickCancel: function () {
            if(this.noticeSeq) { // 수정중
                location.href = "/board/notice/view?noticeSeq="+this.noticeSeq;
            } else { // 신규등록
                location.href = "/board/notice/main";
            }
        },
        // (상세보기)
        onclickView: function () {
            location.href = "/board/notice/view?noticeSeq="+this.noticeSeq;
        },
        // 저장 메소드 호출
        onclickSave:function () {

            if(!this.saveInfo.title){
                alertMsg("제목은 필수입니다.", this.$refs.title);
                return false;
            }
            if(!this.saveInfo.importantYn){
                alertMsg("중요여부는 필수입니다.");
                return false;
            }
            if(!this.saveInfo.contents){
                alertMsg("내용은 필수입니다.", this.$refs.contents);
                return false;
            }

            confirmMsg("저장하시겠습니까?",this.save);
        },
        save: function() {
            post(TID.SAVE,
                "/board/notice/save",
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
                case TID.SAVE:
                    console.log(results);
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
                //console.log(results);
                this.notice      = results.response;
                this.saveInfo = this.notice;
                this.noticeFiles = results.response.noticeFiles;
            } else {
                alertMsg(results.error.message);
            }
        },
        saveCallback: function (results) {
            //console.log(results);
            if (results.success) {
                // 저장 성공시 첨부파일이 있을 경우 첨부파일 등록 로직 추가
                const frm = new FormData();
                let cnt = document.getElementsByName("uploadFile").length;
                for(let i=1; i < cnt+1 ; i++) {
                    let file = document.getElementById("uploadFile"+i).files;
                    if(file[0]) { // 첨부된 파일 있음
                        frm.append("multipartFile", file[0]);
                    }
                }

                if(frm.getAll("multipartFile").length > 0) {
                    this.noticeSeq = results.response.noticeSeq;
                    frm.append("noticeSeq", this.noticeSeq);
                    fileUpload(TID.UPLOAD, "/board/notice/file/upload", frm, this.callback);
                } else {
                        alertMsgRtn("정상적으로 저장되었습니다.",this.onclickView);
                }

            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
        fileSizeChk: function(file) {
            let maxSize = maxFileSize * 1024 * 1024; // 5MB
            let fileSize = file.size;

            if(fileSize > maxSize) {
                return false;
            } else {
                return true;
            }
        },
    }
});

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

