/*
 * @name : myStorageList.js
 * @date : 2021-06-28 오후 1:05
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
let isLast = false;
let failList = [];
const TID = {
    SEARCH      : {value: 0, name: "search", code: "S"},
    SEARCH_OBJECT: {value: 0, name: "searchObject", code: "S"},
    UPLOAD : {value: 0, name: "upload", code: "I"},
    DOWNLOAD : {value:0, name: "download"},
    DELETE : {value:0, name: "delete"}
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
            myObjectResultList : [],  // 처리결과
            myObjectResult : [],
            deleteList: [
            ]
            ,
            uploadType : {
                style : {display : "none"},
                type :[
                    {name: "파일 업로드", uri: "", use: true},
                    {name: "폴더 업로드", uri: "", use: true}
                ]
            },
            path : ["/"],
            messages : "",
            delChecked:[], // 삭제 체크박스
            bucketChecked:[], // 버킷(저장소) 체크박
            checked:"checked",
            fileName:""
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
        onclickBucket : function (bucketName, idx){
            this.cond.bucketName = bucketName;
            this.path = ["/"];
            this.bucketSelect(idx);
            this.getMyStorageObjectList(bucketName,"");

        },
        // 저장소(버킷) 체크박스 클릭
        //bucketSelect : function(idx){
        bucketSelect : function(idx, checked){
            this.bucketChecked = [];
            this.bucketChecked.push(idx);

        },
        // object 목록 클릭
        onclickObject : function (objectName, eTag){
            let folderName = this.cond.folderName + objectName;
            let bucketName = this.cond.bucketName;

            if(!eTag) { // 폴더 클릭시 조회
                this.myObjectResult = [];
                this.getMyStorageObjectList(bucketName, folderName);
            } else { //  파일 클릭
                console.log("파일 클릭");//tmp
                console.log("파일명 : " +objectName);//tmp
                console.log("현재 위치: " +this.cond.folderName);//tmp
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
            this.myObjectResult = [];
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

        uploadProgressEvent: function(progressEvent, fileName){
            this.messages = "";
            this.messages = progressEvent.loaded.format() + "/" + progressEvent.total.format()
                + " ("+ (Math.round(progressEvent.loaded/progressEvent.total*10000)/100) +"%) ";

            if (progressEvent.loaded == progressEvent.total){
                //this.messages = "업로드 완료";
            }
            //this.myObjectResult = [];
            let result = "진행중";

            if (progressEvent.loaded == progressEvent.total){
                result= "업로드 완료";
            }

            let idx = this.myObjectResult.findIndex(function(key) {return key.task === fileName})
            if(idx < 0) {
                this.myObjectResult.push({
                    task: fileName,
                    size: progressEvent.total.format(),
                    progress: (Math.round(progressEvent.loaded / progressEvent.total * 10000) / 100),
                    result: result
                });
            }else{
                this.myObjectResult[idx].progress = (Math.round(progressEvent.loaded / progressEvent.total * 10000) / 100);
                this.myObjectResult[idx].result   = result;
            }

        },
        // 업로드(파일, 폴더) 버튼 클릭
        onclickUpload : function(){
            this.myObjectResult = [];
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
/*
        // 업로드(폴더) 변경
        onchangeUploadFolder : function(){
            const frmFolder = new FormData();
            const multipartFolder = this.$refs.multipartFolder;
            for(let i in multipartFolder.files){
                frmFolder.append("multipartFile", multipartFolder.files[i]);
                frmFolder.append("bucketName",this.cond.bucketName);
                frmFolder.append("folderName",this.cond.folderName);
            }
            this.messages = "";
            fileUpload(TID.UPLOAD, "/my/management/storage/object/fileUpload", frmFolder, this.uploadProgressEvent, this.callback);
        },
        */

        // 업로드(폴더) 변경
        onchangeUploadFolder : function(){
            this.messages = "";
            const multipartFile = this.$refs.multipartFolder;
            let uri="/my/management/storage/object/fileUpload";
            let bucketName = this.cond.bucketName;
            let folderName = this.cond.folderName;
            fileUpload(TID.UPLOAD, uri, multipartFile, this.uploadProgressEvent, this.callback, bucketName, folderName);
        },


        //
        onclick : function(type){

        },
        // 삭제 버튼 클릭
        onclickDelete : function(){
            this.deleteList = new Array();
            for(let i=0;i<this.myStorageObjectList.length;i++){
                //console.log(i+" : " + this.cond.folderName + " : " + this.myStorageObjectList[i].name + " : " + this.delChecked[i]);
                if(this.delChecked[i]){ // 삭제 체크박스 true
                    this.deleteList
                        .push({
                            bucketName:this.cond.bucketName,
                            objectName:this.cond.folderName+this.myStorageObjectList[i].name
                        })
                }
            }

            post(TID.DELETE,
                "/my/management/storage/object/delete",
                this.deleteList,
                this.callback);

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
                case TID.DELETE:
                    if (results.success) {
                        alert("정상적으로 삭제되었습니다.");
                        this.onclickReload(); // 오브젝트 리스트 새로고침
                    } else {
                        alert("에러 :\n"+results.error.message);
                    }
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                //console.log(results);
                this.myStorageBucketList = results.response;
            } else {
                console.log(results);
                alert("에러 :\n"+results.error.message);
            }
        },
        searchObjectCallback: function (results) {
            if (results.success) {
                //console.log(results);
                this.myStorageObjectList = results.response;
                let folderName = this.cond.folderName.split("/");
                this.pathTemp = ["/"];
                for(let i=0 ; i < folderName.length-1 ; i++){
                    this.pathTemp = this.pathTemp.concat(folderName[i]+"/");
                }
                this.path = this.pathTemp;

                // 파일 input 값 reset
                this.inputClear();
                this.delChecked = [];

            } else {
                console.log(results);
                alert("에러 :\n"+results.error.message);
            }
        },
        uploadCallback: function (results) {
            if(results.success){
                //console.log(results);
                if(isLast) {
                    //alert("정상적으로 업로드되었습니다.");
                    //this.onclickReload(); // 오브젝트 리스트 새로고침
                    this.getMyStorageObjectList(this.cond.bucketName, this.cond.folderName); // 오브젝트 리스트 조회
                }
            } else {
                console.log(results);
                this.messages = results.error.message;
                for(let i=0; i <failList.length;i++){
                    let idx = this.myObjectResult.findIndex(function(key) {return key.task === failList[i].task})
                    this.myObjectResult[idx].size = failList[i].size;
                    this.myObjectResult[idx].progress = failList[i].progress;
                    this.myObjectResult[idx].result = failList[i].result;
                }
                failList = [];
            }
        },
        inputClear: function () {
            const inputFile = this.$refs.multipartFile;
            const inputFolder = this.$refs.multipartFolder;
            inputFile.type = 'text';
            inputFile.type = 'file';

            inputFolder.type = 'text';
            inputFolder.type = 'file';
        }

    }
});

function fileUpload(tid, uri, multipartFile, progEvent, callback, bucketName, folderName){

    const headers = {
        'Content-Type': 'multipart/form-data'
    };
    const config = {
        onUploadProgress: progressEvent => progEvent(progressEvent),
        headers: headers
    };
    let fileLen = multipartFile.files.length;
    for(let i=0; i<fileLen;i++){
        let formData = new FormData();
        formData.append("multipartFile", multipartFile.files[i]);
        formData.append("bucketName", bucketName);
        formData.append("folderName", folderName);
        let fileName = multipartFile.files[i].name;
        //axios.post(uri, formData, config)
        axios.post(uri, formData, {
                                    headers: headers,
                                    onUploadProgress: progressEvent => progEvent(progressEvent, fileName)
                                    //onUploadProgress: progressEvent => progEvent(progressEvent)
        })
            .then((response) => {
                isLast = false;
                if(i === fileLen - 1){
                    isLast = true;
                }
                // 응답 처리
                callback(tid, response.data);
            })
            .catch((error) => {
                // 예외 처리
                if (error.response) {
                    // 요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.
                    failList = [];
                    failList.push({
                        task : fileName,
                        size : 0,
                        progress : 0,
                        result: "에러 : " + error.response.data.error.message
                    });
                    callback(tid, error.response.data);
                }
                else if (error.request) {
                    // 요청이 이루어 졌으나 응답을 받지 못했습니다.
                    // `error.request`는 브라우저의 XMLHttpRequest 인스턴스 또는
                    // Node.js의 http.ClientRequest 인스턴스입니다.
                     console.log(error.request);
                    alert(error.message);
                }
                else {
                    // 오류를 발생시킨 요청을 설정하는 중에 문제가 발생했습니다.
                     console.log('Error', error.message);
                     alert(error.message);
                }
            })
    }
};
/*
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
};
*/


