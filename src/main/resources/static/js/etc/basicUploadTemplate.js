var appMain;
var TID = {
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
            messages : "",
        };
    },
    mounted:function(){
    },
    methods:{
        hideMessage : function(){
            this.messages = "";
        },
        callback : function(tid, results){
            switch (tid) {
                case TID.UPLOAD:
                    this.uploadCallback(results);
                    break;
            }
        },
        uploadCallback: function(results){
            if(results.success){
                console.log(results);
            }else{
                this.messages = results.error.message;
            }
        },
        uploadProgressEvent: function(progressEvent){
            this.messages = "";
            this.messages = progressEvent.loaded.format() + "/" + progressEvent.total.format()
                + " ("+ (Math.round(progressEvent.loaded/progressEvent.total*10000)/100) +"%) ";

            if (progressEvent.loaded == progressEvent.total){
                this.messages = "업로드 완료";
            }
        },
        onclickUpload : function(){

            const frm = new FormData();
            const multipartFile = this.$refs.multipartFile;

            for (let i in multipartFile.files){
                frm.append("multipartFile", multipartFile.files[i]);
            }

            frm.append("test", "test");

            console.log(frm);

            fileUpload(TID.UPLOAD, "/etc/fileUpload", frm, this.uploadProgressEvent, this.callback);
        },
    }
});

function fileUpload(tid, uri, formData, progEvent, callback){

    const headers = {
        'Content-Type': 'multipart/form-data'
    };
    const config = {
        onUploadProgress: progressEvent => progEvent(progressEvent),
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