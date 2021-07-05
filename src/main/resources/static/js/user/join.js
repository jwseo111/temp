let appMain;
let codeId;
const numExp = /[^0-9]/gi;        // 숫자
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
        };
    },
    mounted:function(){
        if(this.join.next === "Y"){

          if(this.join.agree1 !== "Y" && this.join.agree2 !== "Y") {

              alert("잘 못된 접근방식 입니다.");
              location.href = "/";
              return;
          }
        }

        codeId = "AgencyType";
        getCodeList(codeId, this.callBack);

        codeId = "Disease";
        getCodeList(codeId, this.callBack);

    },
    computed: function(){


    },
    methods:{

        onclickBack : function(){
            location.href="/";

        },
        onclickNext : function(){
            if(isNull(this.join.agree1) || isNull(this.join.agree2 )){
                alert("모든 약관에 동의 해주세요.");
                return;
            }
            document.frm.submit();
        },
        callBack : function(tid, result){   // 공통코드 조회

            switch (tid){
                case "AgencyType":
                    this.info.agencyTypeList = result.response;
                    break;
                case "Disease":
                    this.info.diseaseList = result.response;
                    break;
                case "Cert":
                    if(result.success) {
                        alert("인증 메일이 발송 되었습니다.");
                        this.certNumber = result.response.certNumber;
                    }else{
                        alert(result.error.message);
                    }
                    break;
                case "SAVE":
                    if(result.success){
                        alert("정상적으로 등록되었습니다.");
                        location.href="/joinComplete";
                    }else{
                        alert(result.error.message);
                    }
                    break;
                default :
                    break;

            }
        },
        onclickAgency : function(){
          if(this.info.agencyTypeCode === "COMP") {
              this.info.diseaseManagerYn = "";
          }
        },
        onclickCertSend : function(){ // 인증 email 전송

            let result = regExp("EMAIL", this.info.userEmail);
            if(result === "N" || isNull(this.info.userEmail)){
                this.message1 = "E-Mail 형식으로 입력해주세요.";
                this.$refs.userEmail.focus();
            }else{
                post("Cert", "/user/cert/mail", this.info, this.callBack);
            }
        },
        onclickCertChk : function() { // 인증번호 확인

            if(!isNull(this.certNumber) && this.info.certCode === this.certNumber){
                this.message2 ="확인되었습니다.";
                this.info.passOk1="Y";

            }else{
                this.message2 ="인증번호를 다시 확인해 주세요.";
                this.$refs.password2.focus();
                this.info.passOk1="N";
            }
        },
        onblurPassChk1 : function(){    //비밀번호 체크
            let result = regExp("PASS2", this.info.password1);
            this.message3 = result;
            if(!isNull(result)){
               this.$refs.password1.focus();
            }

        },
        onblurPassChk2 : function(){    //비밀번호 확인

            if(this.info.password2 !== ""){
                if(this.info.password1 !== this.info.password2){
                    this.message4 ="비밀번호가 일치 하지 않습니다.";
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
        popupAgency : function(){         // 기관 팝업
            if(isNull(this.info.agencyTypeCode)){
                alert("회원 구분을 선택해주세요.");
                document.getElementById("rdo0").focus();
                return;
            }
            openPopupAgency(this.info.agencyTypeCode);
        },
        onkeyupPhoneNumber : function(e){   // 핸드폰
            this.info.userPhoneNumber = e.target.value.replace(numExp, "");
        },
        onkeyupEmail : function(e){
            this.info.passOk1="N";
            const email = /[^0-9a-zA-Z@\._]/gi; // 이메일
            this.info.userEmail = e.target.value.replace(email, "");
        },
        callbackPopupAgency : function(item){   // 기관 팝업 Callback

            this.info.agencySeq = item.agencySeq;
            this.info.agencyName = item.agencyName;
            this.info.ceoName = item.ceoName;
            this.info.blNumber = item.blNumber;
        },
        isFormValid : function(){

            let param =[
                {value:this.info.agencyTypeCode, title:"회원구분", ref:document.getElementById("rdo0"), type:"S", msg:""},
                {value:this.info.userEmail, title:"이메일", ref:this.$refs.userEmail},
                {value:this.info.certCode, title:"이메일인증코드", ref:this.$refs.certCode},
            ];

            if(!isValid(param)) return false;

            if(this.info.passOk1 === "N"){
                alert("이메일 인증확인은 필수입니다.");
                this.$refs.certCode.focus();
                return false;
            }

            param =[
                {value:this.info.password1, title:"비밀번호", ref:this.$refs.password1},
                {value:this.info.password2, title:"비밀번호확인", ref:this.$refs.password2},
            ];

            if(!isValid(param)) return false;

            if(this.info.passOk2 === "N"){
                alert("비밀번호가 일치 하지 않습니다.");
                this.$refs.password2.focus();
                return false;
            }

            param =[
                {value:this.info.agencyName, title:"비밀번호", ref:this.$refs.popupAgency, msg:"기관팝업을 클릭하여 기관명을 선택해주세요."},
                {value:this.info.diseaseCode, title:"질병명", ref:this.$refs.diseaseCode, type:"S"},
                {value:this.info.userName, title:"담당자이름", ref:this.$refs.userName},
                {value:this.info.userPhoneNumber, title:"담당자휴대전화", ref:this.$refs.userPhoneNumber, type:"I"},
            ];

            if(!isValid(param)) return false;

            if(this.info.agencyTypeCode !=="COMP" && isNull(this.info.diseaseManagerYn)){
                alert("질병책임자여부를 선택해주세요.");
                this.$refs.diseaseManagerY.focus();
                return false;
            }

            param =[
                {value:this.info.nCloudId, title:"NBP 아이디", ref:this.$refs.nCloudId},
                {value:this.info.nCloudAccessKey, title:"NBP 액세스키", ref:this.$refs.nCloudAccessKey},
                {value:this.info.nCloudSecretKey, title:"NBP 시크릿키", ref:this.$refs.nCloudSecretKey},
            ];

            if(!isValid(param)) return false;

            return true;
        },
        btnSave : function(){

            if(!this.isFormValid()){
                return false;
            }

            if(this.info.passOk2 === "Y"){
                this.info.inputPw=this.info.password1;
            }

            if(this.agencyTypeCode ==="COMP"){
                this.info.userRole="USER";
            }else{
                if(this.info.diseaseManagerYn === "Y"){
                    this.info.userRole ="MANAGER";
                }else{
                    this.info.userRole = "UPLOADER";
                }
            }

            if(confirm("등록 하시겠습니까?")){
                post("SAVE", "/user/join", this.info, this.callBack);
            }
        },
    },

});