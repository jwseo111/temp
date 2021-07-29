let appMain;
const TID = {
    MAIN: {value: 0, name: "main", code: "S"}
};
const hospitalList1 = [
    {url: "/images/icon_hospital1.png", name: "서울아산병원"},
   /* {url: "/images/icon_hospital2.png", name: "세브란스병원"},
    {url: "/images/icon_hospital3.png", name: "삼성서울병원"},
    {url: "/images/icon_hospital4.png", name: "가천대길병원"},
    {url: "/images/icon_hospital5.png", name: "분당서울대학교병원"},
    {url: "/images/icon_hospital6.png", name: "서울대학교병원"},
    {url: "/images/icon_hospital7.png", name: "고려대학교구로병원"},
    {url: "/images/icon_hospital8.png", name: "카톨릭대학교 서울성모병원"},
    {url: "/images/icon_hospital9.png", name: "강남세브란스병원"},
    {url: "/images/icon_hospital10.png", name: "강릉아산병원"},
    {url: "/images/icon_hospital11.png", name: "경북대학교병원"},
    {url: "/images/icon_hospital12.png", name: "국립암센터"},
    {url: "/images/icon_hospital13.png", name: "부산대학교병원"},
    {url: "/images/icon_hospital14.png", name: "분당차병원"},
    {url: "/images/icon_hospital15.png", name: "양산부산대학교병원"},
    {url: "/images/icon_hospital16.png", name: "여의도성모병원"},
    {url: "/images/icon_hospital17.png", name: "울산대학교병원"},
    {url: "/images/icon_hospital18.png", name: "이대목동병원"},
    {url: "/images/icon_hospital19.png", name: "전남대학교병원"},
    {url: "/images/icon_hospital20.png", name: "제주대학교병원"},*/

];
const hospitalList2 = [

    /*{url: "/images/icon_hospital21.png", name: "충남대학교병원"},
    {url: "/images/icon_hospital22.png", name: "충북대학교병원"},
    {url: "/images/icon_hospital23.png", name: "칠곡경북대학교병원"},
    {url: "/images/icon_hospital24.png", name: "한양대학교병원"},
    {url: "/images/icon_hospital25.png", name: "화순전남대학교병원"},*/
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

            topCate1: {class: "catelinks active", listShow: true},
            topCate2: {class: "catelinks", listShow: false},
            btmCate1: {class: "catelinks active", listShow: true},
            btmCate2: {class: "catelinks", listShow: false},
        };
    },
    mounted: function () {
        this.loadHospital();
        this.loadOpenData();

    },
    methods: {

        onclickCate: function (cate) {

            if ("topCate1" === cate) {
                this.topCate1.class = "catelinks active";
                this.topCate1.listShow = true;
                this.topCate2.class = "catelinks";
                this.topCate2.listShow = false;
            } else if ("topCate2" === cate) {
                this.topCate1.class = "catelinks";
                this.topCate1.listShow = false;
                this.topCate2.class = "catelinks active";
                this.topCate2.listShow = true;
            } else if ("btmCate1" === cate) {
                this.btmCate1.class = "catelinks active";
                this.btmCate1.listShow = true;
                this.btmCate2.class = "catelinks";
                this.btmCate2.listShow = false;
            } else if ("btmCate2" === cate) {
                this.btmCate1.class = "catelinks";
                this.btmCate1.listShow = false;
                this.btmCate2.class = "catelinks active";
                this.btmCate2.listShow = true;
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

    }
});
