
/*
 * @name : popAcl.js
 * @date : 2021-09-23 오후 2:51
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appPopAcl;
window.addEventListener('load', function() {
    appPopAcl = new Vue({
        el: '#popupwrapacl',
    });
});
Vue.component('popupacl', {
    template: "#popup-template-acl",
    data: function () {
        return {
            aclName:"",
            ipAddress:"",
            memo:"",
            selected:"", // 선택된 콤보박스
            selectList:     getCodeList('ReqStorageStat',this.callback),//
            inboundList:[],
            outboundList:[],
            inbound:{
                order:"", // 우선순위
                protocol:"", // 프로토콜
                source:"", // 접근소스
                port:"", // 포트,
                access:"", //허용여부
                memo:"" // 메모
            },
            outbound:{
                order:"", // 우선순위
                protocol:"", // 프로토콜
                target:"", // 목적지
                port:"", // 포트,
                access:"", //허용여부
                memo:"" // 메모
            },
        };
    },
    mounted: function () {

    },
    methods: {
        // Inbound추가 클릭
        onclickInboundAdd: function (){
            this.inbound.protocol = this.$refs.sInProtocol.value;
            this.inbound.access = this.$refs.sInAccess.value;
            this.inboundList.push(this.inbound);
            this.inbound = {}; // inbound 입력란 초기화
        },
        onclickCancel: function() {

        },

        onclickReqSave: function() {
            console.log("VPC 생성 클릭");//tmp
            console.log("VPC 이름 : " +  this.vpcName);//tmp

        },
        callback: function (tid, results) {
            switch (tid) {
                case "ReqStorageStat":
                    //console.log(results.response);
                    // this.interfaceList = results.response;
                    // this.subnetList = results.response;
                    this.selectList = results.response;

                    break;
            }
        },
        searchCallback: function (tid,results) {
            if (results.success) {
                this.makePageNavi(results.response.pageable, results.response.total);
                this.agencyList = results.response.content;
            } else {
                console.log(results);
            }
        },
        // makePageNavi: function (pageable, total) {
        //     let max = Math.ceil(total / pageable.size);
        //     max = max == 0 ? 1 : max;
        //     const curr = pageable.page + 1;
        //
        //     const first = Math.ceil(curr / 5) * 5 - 4;
        //     let last = first + 4;
        //     last = last>max?max:last;
        //     let prev = first - 1;
        //     prev = prev<1?1:prev;
        //     let next = last + 1;
        //     next = next>max?max:next;
        //
        //     this.pageInfo.first = first;
        //     this.pageInfo.max = max;
        //     this.pageInfo.curr = curr;
        //     this.pageInfo.last = last;
        //     this.pageInfo.prev = prev;
        //     this.pageInfo.next = next;
        //
        //     this.pageInfo.pages = new Array();
        //     for (let i=first; i<=last; i++){
        //         this.pageInfo.pages.push(i);
        //     }
        // },
        // onclickPage : function (page){
        //     this.cond.page = page - 1;
        //     this.getAgencyList();
        // },
        // selectAgency : function (agency){
        //     callbackPopupAgency(agency);
        //     fnClosePopup('networkInterfaceModal');
        // },

    }
});

