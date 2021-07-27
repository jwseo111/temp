
/*
 * @name : popAgencySearch.js
 * @date : 2021-06-23 오후 1:05
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;

const TID = {
    INFO: {value: 0, name: "info", code: "I"},
    LIST: {value: 1, name: "list", code: "L"},
    SAVE: {value: 2, name: "save", code: "U"},
};

window.onload = function () {
    appMain = new Vue({
        el: '#maincontentswrap',
    });
}
Vue.component('maincontents', {
    template: "#main-template",
    data: function () {
        return {

            agencyName:"",
            diseaseName:"",
            diseaseManageName:"",
            parentUserSeq:"",
            uploaderList:[],
            messages: "",
            checked:[],

        };
    },
    mounted: function () {

        get(TID.INFO, "/user/my/info", null,this.callback);



    },
    methods: {
        callback: function(tid, results){

            switch (tid) {
                case TID.INFO:
                    this.getUserInfoCallback(results);
                    break;
                case TID.LIST:
                    this.getListCallback(results);
                    break;
                case TID.SAVE:
                    this.onclickSaveCallback(results);
                    break;

            }

        },
        onclickClose: function(){
          window.close();
        },
        onclickSave: function(){
            for(let i=0;i < this.uploaderList.length;i++){
                let data = null;
                for (let y=0;y < this.checked.length;y++){

                    if(this.checked[y] ===this.uploaderList[i].userSeq){
                       data = this.parentUserSeq;
                    }

                }
                this.uploaderList[i].parentUserSeq = data;
            }
            if(confirm("저장 하시겠습니까?")){
                post(TID.SAVE,"/user/my/uploader",this.uploaderList,this.onclickSaveCallback);
            }

        },
        getUserInfoCallback : function(result) {

            let data = result.response;
            this.agencyName= data.agencyInfo.agencyName;
            this.diseaseName = data.diseaseCode.desc;
            this.diseaseManageName = data.userName;
            this.parentUserSeq = data.userSeq;

            // 업로더 조회
            get(TID.LIST, "/user/my/uploader",null,this.callback);

        },
        getListCallback : function(result){
            this.uploaderList=result.response;
            for(let i=0;i < this.uploaderList.length;i++){
                if(this.uploaderList[i].parentUserSeq === this.parentUserSeq){
                    this.checked.push(this.uploaderList[i].userSeq);
                }

            }
        },
        onclickSaveCallback : function(tid, result){
            if (result.success) {
                alert("정상적으로 수정 되었습니다.");
                this.onclickClose();
            } else {
                alert(result.error.message);
            }
        },


    },
});