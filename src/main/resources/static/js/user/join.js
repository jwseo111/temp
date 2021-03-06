let appMain;
let codeId;
let agree1;
let agree2;
let next;

window.onload = function(){
    appMain = new Vue({
        el: '#maincontentswrap',
    });

}

Vue.component('maincontents', {
    template : "#main-template",
    data: function() {
        return {
            join: {
                agree1: agree1,
                agree2: agree2,
                next : next,
            },
            info: {
                userRole:"",
                userEmail: "",
                certCode: "",
                certTxt: "",
                agencyTypeList: [],
                agencyTypeCode: "",
                agencySeq:"",
                agencyName:"",
                ceoName: "",
                blNumber: "",
                passOk1: "N",
                password1: "",
                password2: "",
                passOk2: "N",
                inputPw:"",
                diseaseCode: "",
                diseaseList: [],
                userName: "",
                userPhoneNumber: "",
                diseaseManagerYn: "",
                nCloudId: "",
                nCloudObjStorageId:"",
                nCloudAccessKey: "",
                nCloudSecretKey: "",

            },
            form: {
                action: "/regUserInfo",
                method: "POST",
            },
            message1: "",
            message2: "",
            message3: "",
            message4: "",
            certNumber:"",
            cond: {
                page: 0,
                size: 5,
                agencyName: "",
                agencyTypeCode: "",
                sort: "agencyName"
            },
            agencyList: [],
            pageInfo: {
                curr : 1,
                max : 1,
                first : 1,
                last : 1,
                prev : 1,
                next : 1,
                pages: [1]
            },
        };
    },
    mounted:function(){

        if(this.join.next === "Y"){

          if(isNull(this.join.agree1) && isNull(this.join.agree2)) {

              alertMsg("??? ?????? ???????????? ?????????.");
              location.href = "/";
              return;
          }
        }

        codeId = "AgencyType";
        getCodeList(codeId, this.callBack);

        codeId = "Disease";
        getCodeList(codeId, this.callBack);
        setTimeout(function() {
            loadSelect();
        },1000);
    },
    methods:{

        onclickBack : function(){
            location.href="/";

        },
        onclickNext : function(){
            if(!this.join.agree1 || !this.join.agree2 ){
                alertMsg("?????? ????????? ?????? ????????????.");
                return;
            }
            document.frm.submit();
        },
        callBack : function(tid, result){   // ???????????? ??????

            switch (tid){
                case "AgencyType":
                    this.info.agencyTypeList = result.response;
                    break;
                case "Disease":
                    this.info.diseaseList = result.response;
                    break;
                case "Cert":
                    if(result.success) {
                        alertMsg("?????? ????????? ?????? ???????????????.");
                        this.certNumber = result.response.certNumber;
                    }else{
                        alertMsg(result.error.message);
                        this.$refs.userEmail.focus();
                    }
                    break;
                case "SAVE":
                    if(result.success){
                        alertMsgRtn("??????????????? ?????????????????????.", this.saveRtn);
                    }else{
                        alertMsg(result.error.message);
                    }
                    break;
                default :
                    break;

            }
        },
        // ????????? ???????????????
        saveRtn : function(){
            location.href="/joinComplete";
        },
        onclickAgency : function(){
            // ????????? ???????????? ?????????
            this.info.agencySeq="";
            this.info.agencyName ="";
            this.info.ceoName="";
            this.info.blNumber="";

            if(this.info.agencyTypeCode === "COMP") {
                this.info.diseaseManagerYn = "";
            }
        },
        onclickCertSend : function(){ // ?????? email ??????

            let result = regExp("EMAIL", this.info.userEmail);
            if(result === "N" || isNull(this.info.userEmail)){
                this.message1 = "E-Mail ???????????? ??????????????????.";
                this.$refs.userEmail.focus();
            }else{
                this.message1 ="";
                post("Cert", "/user/checkAndCert/mail", this.info, this.callBack);
            }
        },
        onclickCertChk : function() { // ???????????? ??????

            if(!isNull(this.certNumber) && this.info.certCode === this.certNumber){
                this.message2 ="?????????????????????.";
                this.info.passOk1="Y";

            }else{
                this.message2 ="??????????????? ?????? ????????? ?????????.";
                this.$refs.certCode.focus();
                this.info.passOk1="N";
            }
        },
        onblurPassChk1 : function(){    //???????????? ??????
            let result = regExp("PASS2", this.info.password1);
            this.message3 = result;
            if(!isNull(result)){
               this.$refs.password1.focus();
            }

        },
        onblurPassChk2 : function(){    //???????????? ??????

            if(this.info.password2 !== ""){
                if(this.info.password1 !== this.info.password2){
                    this.message4 ="??????????????? ?????? ?????? ????????????.";
                    this.info.password2="";
                    this.info.passOk2 ="N";
                    this.$refs.password2.focus();
                    return false;
                }else{
                    this.info.passOk2 ="Y";
                    this.message4 ="";
                }
            }
        },
        popupAgency : function(){         // ?????? ??????
            if(isNull(this.info.agencyTypeCode)){
                alertMsg("?????? ????????? ??????????????????.");
                return;
            }

            fnOpenPopup('agencyModal', this.info.agencyTypeCode);
            //openPopupAgency(this.info.agencyTypeCode);
        },

        onkeyupCertCode : function(e){  // ????????????
            const numExp = /[^0-9]/gi;        // ??????
            this.info.certCode = e.target.value.replace(numExp, "");
        },
        onkeyupHanName : function(e){   // ??????
            const numHan = /[^???-???a-zA-Z]/gi;        // ??????,??????
            this.info.userName = e.target.value.replace(numHan, "");
        },
        onkeyupPhoneNumber : function(e){   // ?????????
            const numExp = /[^0-9]/gi;        // ??????
            this.info.userPhoneNumber = e.target.value.replace(numExp, "");
        },
        onkeyupEmail : function(e){
            this.info.passOk1="N";
            //const email = /[^0-9a-zA-Z@\._-]/gi; // ?????????
            const email = /[???-???\s]/gi; // ?????????
            this.info.userEmail = e.target.value.replace(email, "");
        },
        callbackPopupAgency : function(item){   // ?????? ?????? Callback

            this.info.agencySeq = item.agencySeq;
            this.info.agencyName = item.agencyName;
            this.info.ceoName = item.ceoName;
            this.info.blNumber = item.blNumber;
        },
        onclickManager : function(){    // ????????????????????? ?????????
              this.info.nCloudId = "";
              this.info.nCloudAccessKey = "";
              this.info.nCloudSecretKey = "";
              this.info.nCloudObjStorageId = "";

        },
        isFormValid : function(){
            // ?????????
            let code = document.querySelector("#diseaseCode").getAttribute("data-value");

            this.info.diseaseCode=code;
            console.log(this.info.diseaseCode + " / " + code);


            let param =[
                {value:this.info.agencyTypeCode, title:"????????????", ref:document.getElementById("rdo0"), type:"S", msg:""},
                {value:this.info.userEmail, title:"?????????", ref:this.$refs.userEmail},
                {value:this.info.certCode, title:"?????????????????????", ref:this.$refs.certCode},
            ];

            if(!isValid(param)) return false;

            if(this.info.passOk1 === "N"){
                alertMsg("????????? ??????????????? ???????????????.", this.$refs.certCode);
                return false;
            }

            param =[
                {value:this.info.password1, title:"????????????", ref:this.$refs.password1},
                {value:this.info.password2, title:"??????????????????", ref:this.$refs.password2},
            ];

            if(!isValid(param)) return false;

            if(this.info.passOk2 === "N"){
                alertMsg("??????????????? ?????? ?????? ????????????.", this.$refs.password2);
                return false;
            }

            param =[
                {value:this.info.agencyName, title:"?????????", ref:this.$refs.popupAgency, msg:"??????????????? ???????????? ???????????? ??????????????????."},
                {value:this.info.diseaseCode, title:"?????????", ref:this.$refs.diseaseCode, type:"S"},
                {value:this.info.userName, title:"???????????????", ref:this.$refs.userName},
                {value:this.info.userPhoneNumber, title:"?????????????????????", ref:this.$refs.userPhoneNumber, type:"I"},
            ];

            if(!isValid(param)) return false;


            if(this.info.agencyTypeCode !=="COMP" && isNull(this.info.diseaseManagerYn)){
                alertMsg("???????????????????????? ??????????????????.", this.$refs.diseaseManagerY);
                return false;
            }

            if(this.info.diseaseManagerYn === 'Y'){
                param =[
                    {value:this.info.nCloudId, title:"NBP ?????????", ref:this.$refs.nCloudId},
                    {value:this.info.nCloudAccessKey, title:"NBP ????????????", ref:this.$refs.nCloudAccessKey},
                    {value:this.info.nCloudSecretKey, title:"NBP ????????????", ref:this.$refs.nCloudSecretKey},
                    {value:this.info.nCloudObjStorageId, title:"???????????? ???????????? ?????????", ref:this.$refs.nCloudObjStorageId},
                ];

                if(!isValid(param)) return false;
            }
            return true;
        },
        btnSave : function(){
            if(!this.isFormValid()){
               return false;
            }
            if(this.info.passOk2 === "Y"){
                this.info.inputPw=this.info.password1;
            }

            if(this.info.agencyTypeCode ==="COMP"){
                this.info.userRole="USER";
            }else{
                if(this.info.diseaseManagerYn === "Y"){
                    this.info.userRole ="MANAGER";
                }else{
                    this.info.userRole = "UPLOADER";
                }
            }

            confirmMsg("?????? ???????????????????", this.save);

        },
        save : function(){
            post("SAVE", "/user/join", this.info, this.callBack);
        }
    },

});

