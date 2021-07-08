let appMain;

const TID = {
    SEARCH: {value: 0, name: "search", code: "S"},
    INFO: {value: 0, name: "info"},
    SAVE: {value: 0, name: "save"}

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
            joinStatCode: [],
            info : {
                agencyName:"",
                agencyTypeCode:"",
                agencyTypeName:"",
                blNumber:"",
                ceoName:"",
                diseaseCode:"",
                diseaseName:"",
                diseaseManagerYn:"",
                diseaseManagerName:"",
                joinStatCode:"",
                joinStatName:"",
                nCloudAccessKey:"",
                nCloudId:"",
                nCloudSecretKey:"",
                userEmail:"",
                userName:"",
                userPhoneNumber:"",
                userSeq:"",
                userRole:"",
            },
            messages: "",
        };
    },
    mounted:function(){

            codeId = "JoinStat";
            getCodeList(codeId, this.callback);


            let url ="/user/my/info";
            get(TID.INFO, url, null,this.callback);


    },
    methods:{
        callback: function(tid, results){

            switch (tid) {
                case "JoinStat":
                    this.joinStatCode = results.response;
                    break;
                case TID.INFO:
                    this.getUserInfoCallback(results);
                    break;
                case TID.SAVE:
                    this.onclickSaveCallback(results);
                    break;

            }

        },
        getUserInfoCallback : function(results){

            console.log(results.response);
            let item = results.response;
            this.info.agencyName = item.agencyInfo.agencyName;
            this.info.agencyTypeCode = item.agencyInfo.agencyTypeCode.name;
            this.info.agencyTypeName = item.agencyInfo.agencyTypeCode.desc;
            this.info.blNumber = item.agencyInfo.blNumber;
            this.info.ceoName = item.agencyInfo.ceoName;
            this.info.diseaseCode = item.diseaseCode.name;
            this.info.diseaseName = item.diseaseCode.desc;
            this.info.diseaseManagerYn = item.diseaseManagerYn;
            this.info.joinStatCode = item.joinStatCode.name;
            this.info.joinStatName = item.joinStatCode.desc;
            this.info.nCloudAccessKey = item.nCloudAccessKey;
            this.info.nCloudId = item.nCloudId;
            this.info.nCloudSecretKey = item.nCloudSecretKey;
            this.info.userEmail = item.userEmail;
            this.info.userName = item.userName;
            this.info.userPhoneNumber = item.userPhoneNumber;
            this.info.userRole = item.userRole;
            this.info.userSeq = item.userSeq;

            if(item.diseaseManagerYn === "Y"){
                this.info.diseaseManagerName = "질병책임자";
            }else if(item.diseaseManagerYn === "Y"){
                this.info.diseaseManagerName = "데이터업로더";
            }else{
                this.info.diseaseManagerName = "";
            }


        },
        onclickMovePage : function(){
          location.href="/my/userPasswd";
        },
        onclickSave : function(){ // 승인
            if(confirm("수정 하시겠습니까?")){
                post(TID.SAVE,"/user/my/info", this.info, this.callback);
            }

        },
        onclickSaveCallback : function(results){

            if (results.success) {
                alert("정상적으로 수정 되었습니다.");
            } else {
                alert(results.error.message);
            }
        },

    },

});