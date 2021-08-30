
/*
 * @name : storageStatus.js
 * @date : 2021-08-25 오후 1:40
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;
const TID = {
    SEARCH      : {value: 0, name: "search", code: "S"},
    SEARCH_SUMMARY      : {value: 0, name: "search_summary", code: "S"},
};
window.onload = function(){
    appMain = new Vue({
        el: '#maincontentswrap',
    });
}

Vue.component('maincontents', {
    template : "#main-template",
    data: function() {
        return {
            cond: {
                page: 0,
                size: 5,
                agencySeq : "", // 병원코드
                diseaseCode : "", // 질환코드
                sort: "agencyName"
            },
            pageInfo: {
                curr : 1,
                max : 1,
                first : 1,
                last : 1,
                prev : 1,
                next : 1,
                pages: [1]
            },
            messages : "",
            storageUsedList : [],
            storageUsedSummary : [],
            storageUsedSummary1 : [],
            storageUsedSummary2 : [],
            hospitalList : [], // 병원 콤보박스 리스크
            diseaseCdList : getCodeList('Disease',this.callback), // 질환콤보박스 리스트
            diseaseCate1: true,
            diseaseCate2: false,
            maxSize : 12

        };
    },
    mounted:function(){
        this.getAgencyList();
        this.getStorageUsedSummary(); // 사용현황 요약
        this.getStorageUsedList(); // 사용현황 리스트
    },
    methods:{
        onKeyup:function (e){
            if (e.keyCode == 13){
                this.onclickSearch();
            }
        },
        getAgencyList:function(){
            get("Agency",
                "/agency/list",
                {size:100,sort:"agencyName"},
                this.callback);
        },
        onclickSearch: function () {
            this.cond.page = 0;
            this.cond.agencySeq = this.$refs.agencySeq.value;//병원
            this.cond.diseaseCode = this.$refs.diseaseCode.value;//질환
            this.getStorageUsedList();
        },
        // 질환별 적재현황 카테고리 변경
        onclickCate: function (cate) {

            if ("diseaseCate1" === cate) {
                this.diseaseCate1 = true;
                this.diseaseCate2 = false;
            } else if ("diseaseCate2" === cate) {
                this.diseaseCate1 = false;
                this.diseaseCate2 = true;
            }
        },
        getStorageUsedList:function () {
            get(TID.SEARCH,
                "/storage/used/list",
                this.cond,
                this.callback);
        },
        getStorageUsedSummary:function () {
            get(TID.SEARCH_SUMMARY,
                "/storage/used/summary",
                {},
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.SEARCH:
                    this.searchCallback(results);
                    break;
                case TID.SEARCH_SUMMARY:
                    // console.log(results);
                    this.storageUsedSummary = results.response;
                    //  누적 테스트용
                    // this.storageUsedSummary.push({bucketSize:10000, diseaseCode:{desc:"테스트1"}});
                    // this.storageUsedSummary.push({bucketSize:20000, diseaseCode:{desc:"테스트2"}});
                    // this.storageUsedSummary.push({bucketSize:30000, diseaseCode:{desc:"테스트3"}});
                    // this.storageUsedSummary.push({bucketSize:40000, diseaseCode:{desc:"테스트4"}});
                    // this.storageUsedSummary.push({bucketSize:50000, diseaseCode:{desc:"테스트5"}});
                    // this.storageUsedSummary.push({bucketSize:60000, diseaseCode:{desc:"테스트6"}});

                    for(let i=0; i< this.storageUsedSummary.length; i++)
                    {
                        if(i < this.maxSize) {
                            this.storageUsedSummary1.push(this.storageUsedSummary[i]);
                        } else {
                            this.storageUsedSummary2.push(this.storageUsedSummary[i]);
                        }
                    }
                    break;
                case "Disease":
                    //console.log(results.response);
                    this.diseaseCdList = results.response;
                    setTimeout(function() {
                        loadSelect();
                    },300);
                    break;
                case "Agency":
                    let list = results.response.content;
                    this.hospitalList = list.filter(item => (item.agencyTypeCode.name != "COMP") ); // 기업 제외(병원 리스트만)
                    break;
            }
        },
        searchCallback: function (results) {
            if (results.success) {
                //console.log(results.response.content);
                this.makePageNavi(results.response.pageable, results.response.total);
                this.storageUsedList = results.response.content;
            } else {
                console.log(results);
                alertMsg(results.error.message);
            }
        },
        makePageNavi: function (pageable, total) {
            let max = Math.ceil(total / pageable.size);
            max = max == 0 ? 1 : max;
            const curr = pageable.page + 1;

            const first = Math.ceil(curr / 5) * 5 - 4;
            let last = first + 4;
            last = last>max?max:last;
            let prev = first - 1;
            prev = prev<1?1:prev;
            let next = last + 1;
            next = next>max?max:next;

            this.pageInfo.first = first;
            this.pageInfo.max = max;
            this.pageInfo.curr = curr;
            this.pageInfo.last = last;
            this.pageInfo.prev = prev;
            this.pageInfo.next = next;
            this.total = Math.ceil(total / pageable.size);

            this.pageInfo.pages = new Array();
            for (let i=first; i<=last; i++){
                this.pageInfo.pages.push(i);
            }
        },
        onclickPage : function (page){
            if(page === this.pageInfo.curr){
            } else {
                this.cond.page = page - 1;
                this.pageInfo.curr = page;
                this.getStorageUsedList();
            }
        },
    }
});
