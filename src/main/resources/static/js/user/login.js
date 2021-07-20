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
                console.log(results);
                if(this.login.saveId){
                    localStorage.setItem("auth_center_login_userId", this.login.email);
                }else{
                    localStorage.removeItem("auth_center_login_userId");
                }
                location.href="/";
            }else{
                this.messages = results.error.message;
                setTimeout(this.hideMessage, 3000);
            }
        },
        onclickLogin : function(){
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
        onkeyupPhoneNumber : function(e){   // 핸드폰

            this.info.userPhoneNumber = e.target.value.replace(/[^0-9]/gi, "");
        },
       onclickFindId : function(){
           let param =[
               {value:this.info.userName, title:"이름", ref:this.$refs.userName},
               {value:this.info.userPhoneNumber, title:"휴대폰번호", ref:this.$refs.userPhoneNumber},
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
        onclickCertSend : function(){ // 인증 email 전송
            let result = regExp("EMAIL", this.info.userEmail);
            if(result === "N" || isNull(this.info.userEmail)){
                this.messages2 = "E-Mail 형식으로 입력해주세요.";
                setTimeout(this.hideMessage2, 3000);
                this.$refs.email.focus();
            }else{
                post(TID.CERT, "/user/cert/mail", this.info, this.callback);
            }
        },
        callbackCertChk : function(results) {

            if (results.success) {
                alert("인증 메일이 발송 되었습니다.");
                this.certNumber = results.response.certNumber;
            } else {
                alert(results.error.message);
            }
        },
        onclickCertChk : function() { // 인증번호 확인

            if(!isNull(this.certNumber) && this.info.certCode === this.certNumber){
                alert("확인 되었습니다. 새로운 비밀번호로 변경해주세요.");
                this.passChg = true;
                this.certOk=false;
            }else{
                this.messages2 ="인증번호를 다시 확인해 주세요.";
                this.$refs.password2.focus();
            }
        },
        onblurPassChk1 : function(){    //비밀번호 체크
            let result = regExp("PASS2", this.info.password1);
            this.messages = result;
            if(!isNull(result)){
                this.$refs.password1.focus();
            }

        },
        isFormValid : function(){
           let param =[
                {value:this.info.password1, title:"비밀번호", ref:this.$refs.password1},
                {value:this.info.password2, title:"비밀번호확인", ref:this.$refs.password2},
            ];

            if(!isValid(param)) return false;

            if(this.info.password1 !== this.info.password2){
                alert("비밀번호가 일치 하지 않습니다.");
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
            console.log(results);
            if(results.success){
                alert("새로운 비밀번호로 변경 되었습니다.");
                location.href="/login";
            }else{
                this.messages = results.error.message;
                setTimeout(this.hideMessage, 3000);
            }
        },

    }
});

