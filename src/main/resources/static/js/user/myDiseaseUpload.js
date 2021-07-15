/*
 * @name : myStorageList.js
 * @date : 2021-06-28 오후 1:05
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH      : {value: 0, name: "search", code: "S"},
    SEARCH_OBJECT: {value: 0, name: "searchObject", code: "S"},
    UPLOAD : {value: 0, name: "upload", code: "I"}
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
                folderName: "",
                bucketName: "",
                sort: ""
            },
            myStorageBucketList: [],
            myStorageObjectList: [],
            uploadType : {
                style : {display : "none"},
                type :[
                    {name: "파일 업로드", uri: "", use: true},
                    {name: "폴더 업로드", uri: "", use: true}
                ]
            },
            path : ["/"],
            messages : ""
        };
    },
    mounted:function(){
        this.getMyStorageBucketList();
    },
    methods:{
        // 저장소(버킷) 목록 조회
        getMyStorageBucketList:function(){
            get(TID.SEARCH,
                "/my/management/storage/bucket/list",
                {},
                this.callback);
        },
        // 저장소(버킷) 목록 클릭
        onclickBucket : function (bucketName){
            this.cond.bucketName = bucketName;
            this.path = ["/"];
            this.getMyStorageObjectList(bucketName,"");

        },
        // object 목록 클릭
        onclickObject : function (objectName, eTag){
            let folderName = this.cond.folderName + objectName;
            let bucketName = this.cond.bucketName;

            if(!eTag) { // 폴더 클릭시 조회
                this.getMyStorageObjectList(bucketName, folderName);
            }

        },

        // object 목록 조회
        getMyStorageObjectList : function(bucketName, folderName){
            this.cond.folderName = folderName;
            get(TID.SEARCH_OBJECT,
                "/my/management/storage/object/list/"+bucketName,
                this.cond,
                this.callback);
        },
        // Path 클릭(해당 폴더로 이동)
        onclickPath : function(idx){
            let pathName = this.path[idx]=="/"?"":this.path[idx];
            let folderName = "";
            for(let i=1; i < idx+1; i++){
                folderName = folderName + this.path[i];
            }
            this.getMyStorageObjectList(this.cond.bucketName, folderName);
        },

        // 새로고침 클릭
        onclickReload : function(){
            this.getMyStorageObjectList(this.cond.bucketName, this.cond.folderName);
        },
        // 업로드 버튼 over
        hoverUpload : function(b){
            if(b){
                this.uploadType.style.display = "block";
            } else{
                this.uploadType.style.display = "none";
            }
        },

        // 업로드 버튼 클릭
        onclickUpload : function(){
            this.uploadType.style.display = "block";

        },

        uploadProgressEvent: function(progressEvent){
            this.messages = "";
            this.messages = progressEvent.loaded.format() + "/" + progressEvent.total.format()
                + " ("+ (Math.round(progressEvent.loaded/progressEvent.total*10000)/100) +"%) ";

            if (progressEvent.loaded == progressEvent.total){
                this.messages = "업로드 완료";
            }
        },
        // 업로드 버튼 클릭
        onclickUpload : function(){
            console.log("파일 업로드");//tmp
            const frm = new FormData();
            const multipartFile = this.$refs.multipartFile;

            for(let i in multipartFile.files){
                frm.append("multipartFile", multipartFile.files[i]);
            }

            console.log(frm);
            fileUpload(TID.UPLOAD, "/etc/fileUpload", frm, this.uploadProgressEvent, this.callback);

        },
        // 업로드(폴더) 버튼 클릭
        onclickUploadFolder : function(){
            console.log("폴더 업로드");//tmp
        },
        //
        onclick : function(type){
            console.log("클릭");//tmp

        },

        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case TID.SEARCH_OBJECT:
                    this.searchObjectCallback(results);
                    break;
                case TID.UPLOAD:
                    this.uploadCallback(results);
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                console.log("버킷리스트");//tmp
                console.log(results);
                this.myStorageBucketList = results.response;
            } else {
                console.log(results);
            }
        },
        searchObjectCallback: function (results) {
            if (results.success) {
                //console.log("오브젝트 조회");//tmp
                //console.log(results.response);//tmp
                this.myStorageObjectList = results.response;
                let folderName = this.cond.folderName.split("/");
                this.pathTemp = ["/"];
                for(let i=0 ; i < folderName.length-1 ; i++){
                    this.pathTemp = this.pathTemp.concat(folderName[i]+"/");
                    //this.path = this.pathTemp;
                }
                this.path = this.pathTemp;
            } else {
                console.log(results);
            }
        },

        uploadCallback: function (results) {
            if(results.success){
                console.log("업로드 성공: "  + results);
            } else {
                console.log(results);
                this.messages = results.error.message;            }
        }

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
                 console.log(error.request);
            }
            else {
                // 오류를 발생시킨 요청을 설정하는 중에 문제가 발생했습니다.
                 console.log('Error', error.message);
            }
        })
}

