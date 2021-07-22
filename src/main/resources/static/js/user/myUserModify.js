let appMain;

const TID = {
    SEARCH: {value: 0, name: "search", code: "S"},
    INFO: {value: 0, name: "info"},
    SAVE: {value: 0, name: "save"},
    CHG: {value: 0, name: "chg"},

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
                nCloudId:"",
                nCloudObjStorageId:"",
                nCloudAccessKey:"",
                nCloudSecretKey:"",
                userEmail:"",
                userName:"",
                userPhoneNumber:"",
                userSeq:"",
                userRole:"",
                parentUserName:"",
                parentUserYn:false,
                parentYn:false,
            },
            managerYn:false,
            messages: "",
        };
    },
    mounted:function(){

        codeId = "JoinStat";
        getCodeList(codeId, this.callback);

        this.getUserInfo();

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
                case TID.CHG:
                    this.onclickChgManageCallback(results);
                    break;
            }

        },
        getUserInfo : function(){
            get(TID.INFO, "/user/my/info", null,this.callback);
        },
        getUserInfoCallback : function(results){

            let item = results.response;

            if(!isNull(item.agencyInfo)){
                this.info.agencyName = item.agencyInfo.agencyName;
                this.info.agencyTypeCode = item.agencyInfo.agencyTypeCode.name;
                this.info.agencyTypeName = item.agencyInfo.agencyTypeCode.desc;
                this.info.blNumber = item.agencyInfo.blNumber;
                this.info.ceoName = item.agencyInfo.ceoName;
            }

            if(!isNull(item.diseaseCode)){
                this.info.diseaseCode = item.diseaseCode.name;
                this.info.diseaseName = item.diseaseCode.desc;
            }

            this.info.diseaseManagerYn = item.diseaseManagerYn;
            if(!isNull(item.joinStatCode)){
                this.info.joinStatCode = item.joinStatCode.name;
                this.info.joinStatName = item.joinStatCode.desc;
            }

            this.info.nCloudAccessKey = item.nCloudAccessKey;
            this.info.nCloudId = item.nCloudId;
            this.info.nCloudObjStorageId = item.nCloudObjStorageId;
            this.info.nCloudSecretKey = item.nCloudSecretKey;
            this.info.userEmail = item.userEmail;
            this.info.userName = item.userName;
            this.info.userPhoneNumber = item.userPhoneNumber;
            this.info.userRole = item.userRole;
            this.info.userSeq = item.userSeq;
            if(!isNull(item.parentUserInfo)){
                this.info.parentUserName =item.parentUserInfo.userName;
                this.info.parentYn = true;
            }else{
                this.info.parentUserName = "지정된 질병책임자가 없습니다.";
                this.info.parentYn = false;
            }

            if(item.diseaseManagerYn === "Y"){
                this.info.diseaseManagerName = "질병책임자";
                this.managerYn = true;
            }else if(item.diseaseManagerYn === "N"){
                this.info.diseaseManagerName = "데이터업로더";
                this.info.parentUserYn = true;

            }else{
                this.info.diseaseManagerName = "";
            }


        },
        onclickMovePage : function(){
          location.href="/my/userPasswd?menuId="+myMenuId;
        },
        isFormValid : function(){
            let param =[

                    {value:this.info.userName, title:"담당자이름", ref:this.$refs.userName},
                    {value:this.info.userPhoneNumber, title:"담당자휴대전화", ref:this.$refs.userPhoneNumber},
                    {value:this.info.nCloudId, title:"NBP 아이디", ref:this.$refs.nCloudId},
                    {value:this.info.nCloudAccessKey, title:"NBP 액세스키", ref:this.$refs.nCloudAccessKey},
                    {value:this.info.nCloudSecretKey, title:"NBP 시크릿키", ref:this.$refs.nCloudSecretKey},
                ];

            if(!isValid(param)) return false;

            if(this.info.agencyTypeCode === 'COMP' || this.info.diseaseManagerYn === 'Y'){
                if(isNull(this.info.nCloudObjStorageId)){
                    alert("오브젝트 스토리지 아이디는 필수입니다.");
                    this.$refs.nCloudObjStorageId.focus();
                    return false;
                }
            }

            return true;
        },

        onclickSave : function(){ // 승인
            if(!this.isFormValid()){
                return false;
            }

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
        popupUploader : function(){ // 데이터 업로더팝업
            openPopupUploader(this.info.userSeq);
        },
        onclickChgManage : function(){
            if(confirm("질병책임자를 변경하시겠습니까?")){
                post(TID.CHG,"/user/my/manager", null, this.callback);
            }

        },
        onclickChgManageCallback : function(results){
            if (results.success) {
                alert("정상적으로 수정 되었습니다.");
                this.getUserInfo();
            }else{
                alert(results.error.message);
            }
        }

    },

});