var instance;
window.onload = function(){
    instance  = axios.create({
        baseURL: 'http://localhost:8080',
        headers: { 'X-Custom-Header': 'Custom' },
        timeout: 10000,
    });
}

function post(tid, uri, param, callback){
    axios.post(uri, param)
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