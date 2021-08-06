let appMain;
let userSeq;
const TID = {
    SEARCH: {value: 0, name: "search", code: "S"},
    INFO: {value: 0, name: "info"},
    ACCEPT: {value: 0, name: "accept"}

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
            search : {
                page: 0,
                size: 10,
                joinStat :"",
                userName :"",
            },
            list :[],
            joinStatCode: [],
            pageInfo: {
                curr : 1,
                max : 1,
                first : 1,
                last : 1,
                prev : 1,
                next : 1,
                pages: [1]
            },
            userSeq :userSeq,
            info : {

                agencyName:"",
                agencyTypeCode:"",
                agencyTypeName:"",
                blNumber:"",
                ceoName:"",
                diseaseCode:"",
                diseaseName:"",
                diseaseManagerYn:"",
                diseaseManagerName:"",
                joinStatCode:"",
                joinStatName:"",
                nCloudId:"",
                nCloudObjStorageId:"",
                nCloudAccessKey:"",
                nCloudSecretKey:"",
                userEmail:"",
                userName:"",
                userPhoneNumber:"",
                userSeq:"",
            },
            showYn:true,
            acceptYn:false,
            messages: "",
        };
    },
    mounted:function(){

        if(isNull(this.userSeq)){
            codeId = "JoinStat";
            getCodeList(codeId, this.callback);
            this.onclickSearch();
        }else{

            let url ="/user/info/"+this.userSeq;

            get(TID.INFO, url, null,this.callback);

        }
        setTimeout(function() {
            loadSelect();
        },500);
    },
    computed: function(){
    },
    methods:{
        onclickSearch : function(){
            this.search.joinStat=this.$refs.joinStat.value;
            get(TID.SEARCH,"/user/list", this.search, this.callback);
        },
        callback: function(tid, results){

            switch (tid) {
                case TID.SEARCH:
                    this.onclickSearchCallback(results);
                    break;
                case "JoinStat":
                    this.joinStatCode = results.response;
                    break;
                case TID.INFO:
                    this.getUserInfoCallback(results);
                    break;
                case TID.ACCEPT:
                    this.onclickAcceptCallback(results);
                    break;

            }

        },
        onclickSearchCallback: function(results){
            if (results.success) {
                this.makePageNavi(results.response.pageable, results.response.total);
                this.list = results.response.content;
            } else {
                alertMsg(results.error.message);
            }
        },
        makePageNavi: function(pageable, total){

            let max = Math.ceil(total / pageable.size);
            max = max == 0 ? 1 : max;
            let curr = pageable.page + 1;

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

            this.pageInfo.pages = new Array();
            for (let i=first; i<=last; i++){
                this.pageInfo.pages.push(i);
            }

        },
        onclickPage : function (page){
            this.search.page = page - 1;
            this.onclickSearch();
        },
        onclickDetail : function(seq){
            location.href="/my/admin/memberView?menuId="+myMenuId+"&userSeq="+seq;
        },
        getUserInfoCallback : function(results){

            const item = results.response;
            this.info.agencyName = item.agencyInfo.agencyName;
            this.info.agencyTypeCode = item.agencyInfo.agencyTypeCode.name;
            this.info.agencyTypeName = item.agencyInfo.agencyTypeCode.desc;
            this.info.blNumber = item.agencyInfo.blNumber;
            this.info.ceoName = item.agencyInfo.ceoName;
            this.info.diseaseCode = item.diseaseCode.name;
            this.info.diseaseName = item.diseaseCode.desc;
            this.info.diseaseManagerYn = item.diseaseManagerYn;
            this.info.joinStatCode = item.joinStatCode.name;
            this.info.joinStatName = item.joinStatCode.desc;
            this.info.nCloudId = item.nCloudId;
            this.info.nCloudObjStorageId = item.nCloudObjStorageId;
            this.info.nCloudAccessKey = item.nCloudAccessKey;
            this.info.nCloudSecretKey = item.nCloudSecretKey;
            this.info.userEmail = item.userEmail;
            this.info.userName = item.userName;
            this.info.userPhoneNumber = item.userPhoneNumber;

            this.info.userSeq = item.userSeq;

            if(item.diseaseManagerYn === "Y"){
                this.info.diseaseManagerName = "질병책임자";
            }else if(item.diseaseManagerYn === "N") {
                this.info.diseaseManagerName = "데이터업로더";
            }

            if(item.userRole === "ADMIN"){  // 질병명, 책임자여부 숨김
                this.showYn = false;
            }

            if(this.info.joinStatCode === "REQUEST"){ // 요청만 승인버튼 노출
                this.acceptYn=true;
            }

        },
        onclickAccept : function(){ // 승인
            confirmMsg("승인 하시겠습니까?", this.save);
        },
        save : function() {
            post(TID.ACCEPT,"/user/accept", this.info, this.callback);
        },
        onclickAcceptCallback : function(results){

            if (results.success) {
                alertMsgRtn("정상적으로 승인처리 되었습니다.", this.saveRtn);
            } else {
                alertMsg(results.error.message);
            }
        },
        saveRtn : function(){
            location.href="my/admin/memberList?menuId=0";
        },
        onclickBack : function(){
            history.back();
        },
    },

});