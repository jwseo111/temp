let appMain;
const TID = {
    MAIN: {value: 0, name: "main", code: "S"}
};
const hospitalList1 = [

    {url:"/images/h_logo1.png", name: "분당서울대학교병원", link:"http://www.snubh.org"},
    {url:"/images/h_logo2.png", name: "가천대길병원", link:"http://www.gilhospital.com"},
    {url:"/images/h_logo3.png", name: "가톨릭대학교서울성모병원", link:"http://www.cmcseoul.or.kr"},
    {url:"/images/h_logo4.png", name: "건국대학교병원", link:"http://www.kuh.ac.kr"},
    {url:"/images/h_logo5.png", name: "경북대학교병원", link:"http://knuh.kr"},
    {url:"/images/h_logo6.png", name: "고려대학교 안산병원", link:"http://ansan.kumc.or.kr"},
    {url:"/images/h_logo7.png", name: "고려대학교 구로병원", link:"http://guro.kumc.or.kr"},
    //{url:"/images/h_logo8.png", name: "고려대학교 안암병원", link:"http://anam.kumc.or.kr"},
    {url:"/images/h_logo9.png", name: "국립암센터", link:"http://www.ncc.re.kr"},
    {url:"/images/h_logo10.png", name: "부산대병원", link:"http://www.pnuh.or.kr"},
    {url:"/images/h_logo11.png", name: "부천성모병원", link:"http://www.cmcbucheon.or.kr"},
    {url:"/images/h_logo12.png", name: "삼성서울병원", link:"http://www.samsunghospital.com"},
    {url:"/images/h_logo13.png", name: "서울대학교병원", link:"http://www.snuh.org"},
    {url:"/images/h_logo14.png", name: "서울아산병원", link:"http://www.amc.seoul.kr"},
    {url:"/images/h_logo15.png", name: "아주대학교의료원", link:"http://www.ajoumc.or.kr"},
    {url:"/images/h_logo16.png", name: "영남대학교의료원", link:"http://yumc.ac.kr:8443"},
    {url:"/images/h_logo17.png", name: "이대목동병원", link:"http://mokdong.eumc.ac.kr"},
    {url:"/images/h_logo18.png", name: "일산백병원", link:"http://www.paik.ac.kr/ilsan"},
    {url:"/images/h_logo19.png", name: "전남대학교병원", link:"http://www.cnuh.com"},
    {url:"/images/h_logo20.png", name: "청주성모병원", link:"http://www.ccmc.or.kr"},
    {url:"/images/h_logo21.png", name: "충북대병원", link:"http://www.cbnuh.or.kr"},
];
const hospitalList2 = [
    {url:"/images/h_logo22.png", name: "한림대성심병원", link:"http://hallym.hallym.or.kr/index.asp"},
    {url:"/images/h_logo23.png", name: "한양대병원", link:"http://seoul.hyumc.com"},
];


window.onload = function () {
    appMain = new Vue({
        el: '#maincontentswrap',
    });
}

Vue.component('maincontents', {
    template: "#main-template",
    data: function () {
        return {
            cond: {
                page: 0,
                size: 5,
                openDataName: "",
                openStorageStatCode: "O_ACC",
                sort: ""
            },
            messages: "",
            noticeList: [],
            openDataList: [],
            hospitalList1: hospitalList1,
            hospitalList2: hospitalList2,

            topCate1: true,
            topCate2: false,
            btmCate1: true,
            btmCate2: false,
        };
    },
    mounted: function () {
        //this.loadHospital();
        this.loadOpenData();

    },
    methods: {

        onclickCate: function (cate) {

            if ("topCate1" === cate) {
                this.topCate1 = true;
                this.topCate2 = false;
            } else if ("topCate2" === cate) {
                this.topCate1 = false;
                this.topCate2 = true;
            } else if ("btmCate1" === cate) {
                this.btmCate1 = true;
                this.btmCate2 = false;
            } else if ("btmCate2" === cate) {
                this.btmCate1 = false;
                this.btmCate2 = true;
            }

        },
        loadHospital: function () {
            let param = {url: "/images/icon_noimage.png", name: ""};
            for (let i = 0; i < 20; i++) {
                if (this.hospitalList1.length <= i) {
                    this.hospitalList1.push(param);
                }
            }

            for (let i = 0; i < 20; i++) {
                if (this.hospitalList2.length <= i) {
                    this.hospitalList2.push(param);
                }
            }
        },
        loadOpenData: function () {

            get(TID.SEARCH,
                "/storage/open/list",
                this.cond,
                this.openDataCallBack);
        },
        openDataCallBack: function (tid, results) {

            if (results.success) {

                let data = results.response.content;
                let list = [];

                for (let i = 0; i < data.length; i++) {

                    let code = data[i].diseaseManagerUserInfo.diseaseCode.name;
                    let obj = {}
                    obj.id = data[i].reqOpenId;
                    obj.name = data[i].agencyInfo.agencyName;
                    obj.code = code;
                    obj.img = "/images/icon_" + code + ".png";
                    obj.title = data[i].openDataName;
                    list.push(obj);

                }

                this.openDataList = list;

                this.loadScroller(list);


            } else {
                console.log(results);
            }

        },
        loadScroller: function (list) {

            setTimeout(function(){

                $("#data ul").carouFredSel({
                    align: "left",
                    width: 580, // 가로길이
                    height: 200, // 세로길이
                    items: {
                        visible: list.length-1 // 보여지는 갯수 (5개가 보여진다면 +1을 하여 버블링 효과를 막는다.)
                    },
                    scroll: {
                        items: 1, // 롤링넘어가는 갯수
                        duration: 500, //롤링 속도
                        pauseOnHover: false // 마우스 오버시 롤링멈춤 true, 롤링작동 false
                    },
                    next: "#data_right", // 오른쪽으로 이동 버튼
                    prev: "#data_left", // 왼쪽으로 이동 버튼
                    direction: "left" // 롤링 방향
                });

            }, 500);

        },
        onclickLinkPage: function(url){
            window.open(url);
        }

    }
});
