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
                alert(results.error.message);
            }
        },
        onclickLogin : function(){

            if(isNull(this.login.email)){
                alert("회원가입시 사용한 이메일을 입력해주세요.");
                this.$refs.loginEmail.focus();
                return;
            }

            if(isNull(this.login.password)){
                alert("비밀번호를 입력해주세요.");
                this.$refs.loginPassword.focus();
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
        onkeyupHanName : function(e){   // 한글
            const numHan = /[^ㄱ-힣a-zA-Z]/gi;        // 숫자
            this.info.userName = e.target.value.replace(numHan, "");
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
        onkeyupEmail : function(e){
            //const email = /[^0-9a-zA-Z@\._-]/gi; // 이메일
            const email = /[ㄱ-힣\s]/gi; // 이메일
            this.info.userEmail = e.target.value.replace(email, "");
        },
        onkeyupCertCode : function(e){  // 인증코드
            const numExp = /[^0-9]/gi;        // 숫자
            this.info.certCode = e.target.value.replace(numExp, "");
        },
        onclickCertSend : function(){ // 인증 email 전송
            this.messages3="";
            let result = regExp("EMAIL", this.info.userEmail);
            if(result === "N" || isNull(this.info.userEmail)){
                this.messages3 = "E-Mail 형식으로 입력해주세요.";
                this.$refs.email.focus();
            }else{
                post(TID.CERT, "/user/find/pw/cert/mail", this.info, this.callback);
            }
        },
        callbackCertChk : function(results) {

            if (results.success) {
                alertMsg("인증 메일이 발송 되었습니다.");
                this.certNumber = results.response.certNumber;
            } else {
                alert(results.error.message);
            }
        },
        onclickCertChk : function() { // 인증번호 확인
            this.messages4="";
            if(!isNull(this.certNumber) && this.info.certCode === this.certNumber){
                alertMsg("확인 되었습니다. 새로운 비밀번호로 변경해주세요.");
                this.passChg = true;
                this.certOk=false;
            }else{
                this.messages4 ="인증번호를 다시 확인해 주세요.";
                this.$refs.certCode.focus();
            }
        },
        onblurPassChk1 : function(){    //비밀번호 체크
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
                {value:this.info.password1, title:"비밀번호", ref:this.$refs.password1},
                {value:this.info.password2, title:"비밀번호확인", ref:this.$refs.password2},
            ];

            if(!isValid(param)) return false;

            if(this.info.password1 !== this.info.password2){
                this.messages6="비밀번호가 일치 하지 않습니다.";
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
                alertMsg(results.error.message);

            }
        },

    }
});

