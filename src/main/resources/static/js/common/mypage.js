let appMypage;
let myMenuId;
//window.onload = function(){
window.addEventListener('load', function() {
        appMypage = new Vue({
            el: '#mypagemenuswrap',
        });
});

Vue.component('mypagemenus', {
    template : "#mypage-template",
    data: function() {
        return {
            menus_manager : [ // 병원
                {name: "저장신청 데이터 보기", uri: "/my/store/list", use: true},
                {name: "질환데이터 업로드", uri: "/my/diseaseUpload", use: true},
                {name: "공개신청 데이터 보기", uri:"/my/open/list", use : true},
                {name: "기업요청 데이터 보기", uri:"", use : true},
                {name: "회원정보변경", uri:"/my/userModify", use : true}
                ],
            menus_admin : [ //관리자
                {name: "회원관리", uri: "/my/admin/memberList", use: true},
                {name: "저장신청관리", uri: "/my/admin/store/list", use: true},
                {name: "공개신청 관리", uri:"/my/admin/open/list", use : true},
                {name: "학습데이터신청 관리", uri:"", use : true},
                {name: "학습환경신청 관리", uri:"", use : true},
                {name: "회원정보변경", uri:"/my/userModify", use : true}
            ],
            menus_uploader : [ // #####
                {name: "저장신청 데이터 보기", uri: "/my/store/list", use: true},
                {name: "질환데이터 업로드", use: true},
                {name: "공개신청 데이터 보기", uri:"/my/open/list", use : true}
            ],
            menus_user : [ // 기업
                {name: "데이터 사용신청 보기", uri: "", use: true},
                {name: "학습환경 사용신청 보기", use: true},
                {name: "회원정보변경", uri:"/my/userModify", use : true}
            ],
            messages : "",
            menuId:myMenuId,
        };

    },
    mounted:function(){
    },
    methods:{
        onclickMenu : function(menu,key){
            location.href = menu.uri+"?menuId="+key;
        },
    }
});

