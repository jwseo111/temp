Vue.config.devtools = true;


/*
 * @name : common.js
 * @date : 2021-06-23 오후 1:12
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */


let instance;
window.addEventListener('load', function() {
    instance  = axios.create({
        baseURL: 'http://localhost:8080',
        headers: { 'X-Custom-Header': 'Custom' },
        timeout: 10000,
    });
});

function get(tid, uri, params, callback){
    
    axios.get(uri, {
        params:params
    })
        .then(function (response) {
            // 응답 성공
            callback(tid, response.data);
        })
        .catch(function (error) {
            // 응답 실패
            if (error.response) {
                // 요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.
                callback(tid, error.response.data);
            }
            else if (error.request) {
                // 요청이 이루어 졌으나 응답을 받지 못했습니다.
                // `error.request`는 브라우저의 XMLHttpRequest 인스턴스 또는
                // Node.js의 http.ClientRequest 인스턴스입니다.
                // console.log(error.request);
            }
            else {
                // 오류를 발생시킨 요청을 설정하는 중에 문제가 발생했습니다.
                // console.log('Error', error.message);
            }
            // console.log(error.config);
        });
}

function post(tid, uri, params, callback){
    axios.post(uri, params)
        .then(function (response) {
            // 응답 성공
            callback(tid, response.data);
        })
        .catch(function (error) {
            // 응답 실패
            if (error.response) {
                // 요청이 이루어졌으며 서버가 2xx의 범위를 벗어나는 상태 코드로 응답했습니다.
                callback(tid, error.response.data);
            }
            else if (error.request) {
                // 요청이 이루어 졌으나 응답을 받지 못했습니다.
                // `error.request`는 브라우저의 XMLHttpRequest 인스턴스 또는
                // Node.js의 http.ClientRequest 인스턴스입니다.
                // console.log(error.request);
            }
            else {
                // 오류를 발생시킨 요청을 설정하는 중에 문제가 발생했습니다.
                // console.log('Error', error.message);
            }
            // console.log(error.config);
        });
}

Number.prototype.format = function(){
    if(this==0) return 0;

    var reg = /(^[+-]?\d+)(\d{3})/;
    var n = (this + '');

    while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');

    return n;
};

function getCodeList(codeId, callback){

    get(codeId,
        "/get/code/"+codeId,
        {},
        callback);
}



// 정규식
function regExp(id, str){

    const passExp = /^[a-zA-Z0-9_\!\@\#\$\%\^\&\*\?]{10,15}$/;   //pass
    //const pass2Exp = /(?=.*\d{1,15})(?=.*[~`!@#$%\^&*()-+=]{1,15})(?=.*[a-zA-Z]{1,15}).{10,15}$/;
    const pass2Exp = /(?=.*\d{1,15})(?=.*[a-zA-Z]{1,15}).{10,15}$/;

    const emailExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; // 이메일
    let rtnMsg="";
    if(id === "EMAIL"){
        if(!emailExp.test(str)){
            rtnMsg ="N";
        }
    }else if(id === "PASS"){

        if(!passExp.test(str) && str !== ""){
            rtnMsg ="10~15자 이내 영문, 숫자, 특수문자만 입력 가능합니다.";
        }
    }else if(id === "PASS2"){

        if(!pass2Exp.test(str) && str !== ""){
            rtnMsg ="10~15자 영문, 숫자 각 1회 이상 입력 가능합니다.";
        }
    }

    return rtnMsg;

}


// 빈값 체크 함수
function isNull(val){
    if (val==="" || val===null || val===undefined)
        return true;
    else
        return false;
}

// Validation check 함수
// value - 항목의 값, title - 항목의 필드명, ref - ref or id  항목으로 focus, msg - 체크 메세지, type - S : radio,selectbox
//ex) let param =[  {value:this.info.agencyTypeCode, title:"회원구분", ref:this.$refs.agencyTypeCode, type:"S", msg:""},]
//    if(!isValid(param)) return false;
function isValid(item){
    for(let i=0;i < item.length;i++){
        let type = item[i].type;
        let afterText = "은(는) 필수입니다.";
        let msg = item[i].msg;

        if(isNull(msg)){
            if(!isNull(type)){
                if("S" === type){
                    afterText = "을(를) 선택해주세요";
                }
            }
            msg = item[i].title + afterText;
        }

        if(isNull(item[i].value)){
            alert(msg);
            if(!isNull(item[i].ref)){
                item[i].ref.focus();
            }
            return false;
        }
    }
    return true;
}

// 기관 팝업 open
function openPopupAgency(code){
    window.open("/popup/agency?agencyTypeCode="+code, "pop", "width=500px,height=500px");
}

// 기관명 팝업 callback

function callbackPopupAgency(item){
    appMain.$refs.maincontents.callbackPopupAgency(item);
}

// 저장소불러오기 팝업 callback
function callbackPopupStorage(item){
    appMain.$refs.maincontents.callbackPopupStorage(item);
}

// 데이터 업로더 설정 Popup open
function openPopupUploader(seq){
    window.open("/popup/uploader?userSeq="+seq, "pop", "width=500px,height=500px");
}

// 로그인정보 get
function getUserInfo(tid,callback){
    get(tid,
        "/user/req/storage",
        {},
        callback);
};