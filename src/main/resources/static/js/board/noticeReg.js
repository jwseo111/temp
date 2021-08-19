
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
            userInfo : [],
            messages : "",
            saveInfo: {
                title : "",
                importantYn : "",
                contents : ""
            },

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
        // 업로드 변경
        onchangeUpload : function(){
            /*
                        const frm = new FormData();
                        const multipartFile = this.$refs.multipartFile;
                        //for(let i in multipartFile.files){
                        for(let i=0; i<multipartFile.files.length;i++){
                            frm.append("multipartFile", multipartFile.files[i]);
                            frm.append("bucketName",this.cond.bucketName);
                            frm.append("folderName",this.cond.folderName);
                            this.fileName = multipartFile.files[i].name;

                        }
                        this.messages = "";

                        fileUpload(TID.UPLOAD, "/my/management/storage/object/fileUpload", frm, this.uploadProgressEvent, this.callback);
            */
            this.messages = "";
            const multipartFile = this.$refs.multipartFile;
            let uri="/my/management/storage/object/fileUpload";
            let bucketName = this.cond.bucketName;
            let folderName = this.cond.folderName;
            fileUpload(TID.UPLOAD, uri, multipartFile, this.uploadProgressEvent, this.callback, bucketName, folderName);

        },
        // 취소 클릭(목록 이동)
        onclickCancel: function () {
            location.href = "/board/notice/main";
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
        // 파일 업로드
        fileUpload: function () {
            const frm = new FormData();
            const multipartFile = this.$refs.multipartFile;
            //for(let i in multipartFile.files){
            for(let i=0; i<multipartFile.files.length;i++){
                frm.append("multipartFile", multipartFile.files[i]);

                this.fileName = multipartFile.files[i].name;
            }

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
                    //console.log(results);
                    this.saveCallback(results);
                    break;
                case TID.UPLOAD:
                    if (results.success) {
                        console.log(results);
                        alertMsgRtn("정상적으로 저장되었습니다.",this.onclickCancel);
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
                this.notice      = results.response;
                this.saveInfo = this.notice;
            } else {
                alertMsg(results.error.message);
            }
        },
        saveCallback: function (results) {
            console.log(results);//tmp
            if (results.success) {
                // 첨부파일이 있을 경우 첨부파일 등록 로직 추가
                const frm = new FormData();
                const uploadFile = this.$refs.uploadFile;
                console.log("### 파일 : " + uploadFile.files.length);//tmp
                if(uploadFile.files.length > 0) {
                    for (let i in uploadFile.files){
                        frm.append("multipartFile", uploadFile.files[i]);
                        console.log("uploadFile.files : "  +uploadFile.files[i].name);//tmp
                    }
                    frm.append("noticeSeq", this.noticeSeq);
                    console.log(frm);
                    fileUpload(TID.UPLOAD, "/board/notice/file/upload", frm, this.callback);

                } else {
                    alertMsgRtn("정상적으로 저장되었습니다.",this.onclickCancel);
                }

            } else {
                //console.log(results);
                alertMsg(results.error.message);
            }
        },
        // saveRtn: function() {
        //     location.href = "/lndata/store/main";
        // },
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

    axios.post(uri, formData, config)
        .then((response) => {
            // 응답 처리
            callback(tid, response.data);
        })
        .catch((error) => {
            // 예외 처리
            if (error.response) {
                // 요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.
                callback(tid, error.response.data);
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

