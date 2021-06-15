var appHeader;
window.addEventListener('load', function() {
    appHeader = new Vue({
        el: '#headercontentswrap',
    });
});

Vue.component('headercontents', {
    template : "#header-template",
    data: function() {
        return {
            menus : [
                {
                    name : "학습 포탈 소개", use : true, style : {display : "none"},
                    children: [
                        {name : "닥터앤서 소개", uri: "/intro/dranswer", use : true},
                        {name : "사용 가이드", uri: "/intro/guide", use : true}
                    ]
                },
                {
                    name : "학습 데이터 관리", use : true, style : {display : "none"},
                    children: [
                        {name : "질환데이터 저장 신청", uri: "/lndata/store/main", use : true},
                        {name : "질환데이터 공개 신청", uri: "/lndata/open/main", use : true},
                        {name : "학습데이터 사용 신청", uri: "/lndata/use/main", use : true}
                    ]
                },
                {
                    name : "학습 환경 관리", use : true, style : {display : "none"},
                    children: [
                        {name : "학습 환경 사용 신청", uri: "/lnenv/main", use : true}
                    ]
                },
                {
                    name : "서비스 현황", use : false, style : {display : "none"},
                    children: [
                        {name : "질환 데이터 적재 현황", uri: "/status/archived/main", use : true},
                        {name : "학습 데이터 활용 현황", uri: "/status/supplied/main", use : true}
                    ]
                }
            ],
            messages : "",
        };
    },
    mounted:function(){
    },
    methods:{
        hoverParent : function (menu, b){
            if (b){
                menu.style.display = "block";
            } else {
                menu.style.display = "none";
            }
        },
        onclickMenu : function(child){
            location.href = child.uri;
        }
    }
});