let appMain;
const TID = {
    LOGIN: {value: 0, name: "login", code: "S"},
    FID: {value: 0, name: "fid", code: "I"},
    CERT: {value: 0, name: "cert", code: "P"},
    CHG: {value: 0, name: "chg", code: "R"},
};
let type;
window.onload = function(){
    appMain = new Vue({
        el: '#maincontentswrap',
    });
}

Vue.component('maincontents', {
    template : "#main-template",
    data: function() {
        return {
            login:{
                email : "",
                password : "",
                saveId:false,
            },
            info:{
                userEmail : "",
                userName :"",
                inputPw :"",
                userPhoneNumber :"",
                certCode : "",
                password1:"",
                password2:"",
            },
            certNumber : "",
            messages : "",
            messages3:"",
            messages4:"",
            messages5:"",
            messages6:"",
            type: type,
            passChg: false,
            certOk: true,
            resultId:[],
        };
    },
    mounted:function(){
        // login id save
        if(isNull(type)){
            let saveId = localStorage.getItem("auth_center_login_userId");
            if(!isNull(saveId)){
                this.login.email = saveId;
                this.login.saveId = true;
            }
        }

    },
    methods:{
        hideMessage : function(){
            this.messages = "";
        },
        callback : function(tid, results){
            console.log(tid +" / " + results)

            switch (tid) {
                case TID.LOGIN:
                    this.loginCallback(results);
                break;
                case TID.FID:
                    this.callbackFindId(results);
                break;
                case TID.CERT:
                    this.callbackCertChk(results);
                break;
                case TID.CHG:
                    this.callbackPassChg(results);
                break;
            default :
                break;
            }
        },
        loginCallback: function(results){
            if(results.success){

                if(this.login.saveId){
                    localStorage.setItem("auth_center_login_userId", this.login.email);
                }else{
                    localStorage.removeItem("auth_center_login_userId");
                }
                location.href="/";
            }else{
                alertMsg(results.error.message);
            }
        },
        onclickLogin : function(){

            if(isNull(this.login.email)){
                alertMsg("??????????????? ????????? ???????????? ??????????????????.", this.$refs.loginEmail);
                return;
            }

            if(isNull(this.login.password)){
                alertMsg("??????????????? ??????????????????.", this.$refs.loginPassword);
                return;
            }

            post(TID.LOGIN, "/login", this.login, this.callback);
            // get(TID.LOGIN, "/agency/list", {page:0, size:100, agencyName:"", agencyTypeCode:"HOSP", sort:""}, this.callback);
        },
        onkeyupForm :function(e){
            if(e.keyCode == 13){
                this.onclickLogin();
            }
        },
        onclickPageMove : function(tid){
            let url = "";
            switch (tid) {
                case "ID":
                    url = "/login/findIdPw?type="+tid;
                    break;
                case "PASS":
                    url = "/login/findIdPw?type="+tid;
                    break;
                case "JOIN":
                    url = "/signup";
                    break;
                case "BACK":
                    url = "/login";
                    break;
            }
            location.href=url;
        },
        onkeyupHanName : function(e){   // ??????
            const numHan = /[^???-???a-zA-Z]/gi;        // ??????
            this.info.userName = e.target.value.replace(numHan, "");
        },
        onkeyupPhoneNumber : function(e){   // ?????????

            this.info.userPhoneNumber = e.target.value.replace(/[^0-9]/gi, "");
        },
        // ????????? ?????? ??????
       onclickFindId : function(){
           let param =[
               {value:this.info.userName, title:"??????", ref:this.$refs.userName},
               {value:this.info.userPhoneNumber, title:"???????????????", ref:this.$refs.userPhoneNumber},
           ];

           if(!isValid(param)) return false;
           this.resultId=[];
            post(TID.FID, "/user/find/mail", this.info, this.callback);
        },
        callbackFindId : function(results){

            if(results.success){
                for(let i=0; i< results.response.length;i++){
                    this.resultId.push(results.response[i].userEmail);
                }
            }else{
                this.messages = results.error.message;
                setTimeout(this.hideMessage, 3000);
            }
        },
        onkeyupEmail : function(e){
            //const email = /[^0-9a-zA-Z@\._-]/gi; // ?????????
            const email = /[???-???\s]/gi; // ?????????
            this.info.userEmail = e.target.value.replace(email, "");
        },
        onkeyupCertCode : function(e){  // ????????????
            const numExp = /[^0-9]/gi;        // ??????
            this.info.certCode = e.target.value.replace(numExp, "");
        },
        onclickCertSend : function(){ // ?????? email ??????
            this.messages3="";
            let result = regExp("EMAIL", this.info.userEmail);
            if(result === "N" || isNull(this.info.userEmail)){
                this.messages3 = "E-Mail ???????????? ??????????????????.";
                this.$refs.email.focus();
            }else{
                post(TID.CERT, "/user/find/pw/cert/mail", this.info, this.callback);
            }
        },
        callbackCertChk : function(results) {

            if (results.success) {
                alertMsg("?????? ????????? ?????? ???????????????.");
                this.certNumber = results.response.certNumber;
            } else {
                alertMsg(results.error.message);
            }
        },
        onclickCertChk : function() { // ???????????? ??????
            this.messages4="";
            if(!isNull(this.certNumber) && this.info.certCode === this.certNumber){
                alertMsg("?????? ???????????????. ????????? ??????????????? ??????????????????.");
                this.passChg = true;
                this.certOk=false;
            }else{
                this.messages4 ="??????????????? ?????? ????????? ?????????.";
                this.$refs.certCode.focus();
            }
        },
        onblurPassChk1 : function(){    //???????????? ??????
            let result = regExp("PASS2", this.info.password1);
            this.messages5 = result;
            if(!isNull(result)){
                this.$refs.password1.focus();
            }

        },
        isFormValid : function(){
            this.messages5="";
            this.messages6="";
           let param =[
                {value:this.info.password1, title:"????????????", ref:this.$refs.password1},
                {value:this.info.password2, title:"??????????????????", ref:this.$refs.password2},
            ];

            if(!isValid(param)) return false;

            if(this.info.password1 !== this.info.password2){
                this.messages6="??????????????? ?????? ?????? ????????????.";
                this.$refs.password2.focus();
                return false;
            }

            return true;
        },
        onclickPassChg : function(){

            if(!this.isFormValid()){
                return false;
            }

            this.info.inputPw=this.info.password1;

            post(TID.CHG, "/user/change/pw", this.info, this.callback);
        },
        callbackPassChg : function(results){

            if(results.success){
                alertMsgRtn("????????? ??????????????? ?????? ???????????????.", this.saveRtn);

            }else{
                alertMsg(results.error.message);

            }
        },
        saveRtn : function(){
            location.href="/login";
        },
    }
});

