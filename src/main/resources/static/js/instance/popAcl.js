
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
            inboundList:[],
            outboundList:[],
            inbound:{
                networkAclNo:"",
                priority:"", // 우선순위 order required > unique no
                protocolTypeCode:"", // 프로토콜 protocol required > TCP or UDP or ICMP
                protocolTypeName:"",
                ipBlock:"", // 접근소스 source
                portRange:"", // 포트, port
                ruleActionCode:"", //허용여부 access required > ALLOW or DROP
                ruleActionName:"",
                networkAclRuleDescription:"" // 메모 memo
            },
            outbound:{
                networkAclNo:"",
                priority:"", // 우선순위 order
                protocolTypeCode:"", // 프로토콜 protocol
                protocolTypeName:"",
                ipBlock:"", // 목적지 target
                portRange:"", // 포트, port
                ruleActionCode:"", //허용여부 access
                ruleActionName:"",
                networkAclRuleDescription:"" // 메모 memo
            },
            protocolTypeCodeList:[
                {name:"TCP", desc:"TCP"},
                {name:"UDP", desc:"UDP"},
                {name:"ICMP", desc:"ICMP"}
            ],
            ruleActionCodeList:[
                {name:"ALLOW", desc:"허용"},
                {name:"DROP", desc:"차단"}
            ],
            vpcName:"",
            saveInfo:{
                createNetworkAclRequestDto:{
                    vpcNo:"",
                    networkAclName:"", // ACL 이름
                    networkAclDescription:"", // 메모
                     },
                addNetworkAclInboundRuleRequestDto:{
                    networkAclNo:"",
                    networkAclRuleList:[],
                }, // inbound List
                addNetworkAclOutboundRuleRequestDto:{
                    networkAclNo:"",
                    networkAclRuleList:[],
                }, // outbound List
            },

            message :{
                networkAclName:"",
                inPriority:"",
                inIpBlock:"",
                inPortRange:"",

                outPriority:"",
                outIpBlock:"",
                outPortRange:"",
            },

            pass :{
                networkAclName:false,
                inPriority : false,
                inIpBlock : false,
                inPortRange:false,

                outPriority : false,
                outIpBlock : false,
                outPortRange:false,
            },
        };
    },
    mounted: function () {

    },
    methods: {
        onload: function(vpcNo) {
            let vpcList = appMain.$refs.maincontents.vpcList;
            this.saveInfo.createNetworkAclRequestDto.vpcNo = vpcNo;
            let idx = vpcList.findIndex(function(key) {return key.vpcNo === vpcNo});
            this.vpcName =  vpcList[idx].vpcName;

            this.inboundList=[];
            this.outboundList=[];
            this.saveInfo.createNetworkAclRequestDto.networkAclName = ""; // ACL 이름
            this.saveInfo.createNetworkAclRequestDto.networkAclDescription = ""; // 메모
        },
        onKeyupName:function (e){
            let str = e.target.value;
            let ll = str.length;
            let exp1 = /[a-z0-9]/; // 영문 또는 숫자 체크
            let exp2 = /^[a-z]{1}[a-z0-9-]+$/; // 첫문자는 소문자, 소문자, 숫자, 하이픈 허용
            if(str.length < 3 || str.length > 30){
                this.pass.networkAclName = false;
                this.message.networkAclName = "최소 3글자 이상, 최대 30자까지만 입력이 가능합니다.";
            } else if(!exp2.test(str)) { // 소문자, 숫자, 특수문자(-)만 허용
                this.pass.networkAclName = false;
                this.message.networkAclName = "소문자, 숫자,\"-\"의 특수문자만 허용하며 알파벳 문자로 시작해야 합니다.";
            } else if(!exp1.test(str[ll-1])) { // 마지막 문자는 소문자 or 숫자
                this.pass.networkAclName = false;
                this.message.networkAclName = "영어 또는 숫자로 끝나야 합니다.";
            } else {
                this.pass.networkAclName = true;
                this.message.networkAclName = "";
            }
        },
        onKeyupInput:function (e) {
            let id = e.target.id;
            let str = e.target.value;
            let ll = str.length;
            let expPriority = /^[0-9]/; // 우선순위 숫자만
            let expIp = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\/(?:[0-9]|[1-2][0-9]|3[0-2]|[01]?)$/;
            // let expPort = /^[1-9]{1}[0-9-]+$/;
            let expPort = /^[0-9-]+$/; // 포트 범위
            if(id == "inPriority" || id == "outPriority") { // 우선순위 체크
                if(str > 199) {
                    this.message[id] = "199를 초과할수 없습니다.";
                    this.pass[id]= false;
                }  else if(!expPriority.test(str)) { // 잘못된 형식입니다.
                    this.message[id] = "잘못된 형식입니다.";
                    this.pass[id]= false;
                }  else {
                    this.message[id] = "";
                    this.pass[id] = true;
                }
            } else if(id == "inIpBlock" || id == "outIpBlock") { // 접근소스, 목적지 체크
                if(!expIp.test(str)) {
                    this.message[id] = "잘못된 형식입니다.";
                    this.pass[id]= false;
                } else {
                    this.message[id] = "";
                    this.pass[id] = true;
                }
                //CIDR prefix 크기는 7 보다 크거나 같아야하며, 최대값은 32입니다.###
            } else if(id == "inPortRange" || id == "outPortRange") { //  포트 체크
                this.message[id] = "";
                this.pass[id] = true;
            }
        },
        // Inbound / Outbound 탭 클릭
        onChangeTab: function(tab) {

            if ( tab == "IN"){
                document.getElementById('inbound').style.display = "block";
                document.getElementById('outbound').style.display = "none";
                document.getElementById("aInbound").style.backgroundColor = "#7F7F7F";
                document.getElementById("aOutbound").style.backgroundColor = "#f2f2f2";
            } else {
                document.getElementById('inbound').style.display = "none";
                document.getElementById('outbound').style.display = "block";
                document.getElementById("aInbound").style.backgroundColor = "#f2f2f2";
                document.getElementById("aOutbound").style.backgroundColor = "#7F7F7F";
            }
        },
        // Inbound 추가 이벤트
        onclickInboundAdd: function (){
            let param =[
                {value:this.inbound.priority,   title:"우선순위",   ref:this.$refs.inPriority},
                {value:this.$refs.sInProtocol,  title:"프로토콜",   ref:this.$refs.sInProtocol},
                {value:this.inbound.ipBlock,    title:"접근소스",   ref:this.$refs.inIpBlock},
                {value:this.inbound.portRange,   title:"포트",      ref:this.$refs.inPortRange},
                {value:this.$refs.sInRuleAction,title:"허용여부",   ref:this.$refs.sInRuleAction},
            ];

            if(!isValid(param)) return false;

            if(this.pass.inPriority && this.pass.inIpBlock) {
            } else {
                alertMsg("입력 값을 확인하세요.");
                return;
            }

            let ruleAction = this.$refs.sInRuleAction.value;
            let idx = this.ruleActionCodeList.findIndex(function (key) {
                return key.name === ruleAction
            })

            let obj = {};
            obj.priority = this.inbound.priority;
            obj.protocolTypeCode = this.$refs.sInProtocol.value;
            obj.protocolTypeName = this.$refs.sInProtocol.value;
            obj.ipBlock = this.inbound.ipBlock;
            obj.portRange = this.inbound.portRange;
            obj.ruleActionCode = this.$refs.sInRuleAction.value;
            obj.ruleActionName = this.ruleActionCodeList[idx].desc;
            obj.networkAclRuleDescription = this.inbound.networkAclRuleDescription;

            this.inboundList.push(obj);
            this.inbound = {}; // inbound 입력란 초기화
        },
        // Inbound 삭제  이벤트
        onclickInboundDel : function(idx) {
            this.inboundList.splice(idx, 1);
        },
        // Outbound 추가 이벤트
        onclickOutboundAdd: function (){
            let param =[
                {value:this.outbound.priority,   title:"우선순위", ref:this.$refs.outPriority},
                {value:this.$refs.sOutProtocol,  title:"프로토콜", ref:this.$refs.sOutProtocol},
                {value:this.outbound.ipBlock,    title:"목적지",   ref:this.$refs.outIpBlock},
                {value:this.outbound.portRange,  title:"포트",     ref:this.$refs.outPortRange},
                {value:this.$refs.sOutRuleAction,title:"허용여부", ref:this.$refs.sOutRuleAction},
            ];

            if(!isValid(param)) return false;

            if(this.pass.outPriority && this.pass.outIpBlock) {
            } else {
                alertMsg("입력 값을 확인하세요.");
                return;
            }

            let obj = {};
            obj.networkAclNo="";
            obj.priority = this.outbound.priority;
            obj.protocolTypeCode = this.$refs.sOutProtocol.value;
            obj.protocolTypeName = this.$refs.sOutProtocol.value;
            obj.ipBlock = this.outbound.ipBlock;
            obj.portRange = this.outbound.portRange;
            obj.ruleActionCode = this.$refs.sOutRuleAction.value;
            obj.ruleActionName = this.ruleActionCodeList[idx].desc;
            obj.networkAclRuleDescription = this.outbound.networkAclRuleDescription;

            this.outboundList.push(obj);
            this.outbound = {}; // outbound 입력란 초기화
        },
        // Outbound 삭제 이벤트
        onclickOutboundDel : function(idx) {
            this.outboundList.splice(idx, 1);
        },

        onclickCancel: function() {

        },

        onclickReqSave: function() {
            this.saveInfo.addNetworkAclInboundRuleRequestDto.networkAclRuleList = [];
            this.saveInfo.addNetworkAclOutboundRuleRequestDto.networkAclRuleList = [];
            this.saveInfo.addNetworkAclInboundRuleRequestDto.networkAclRuleList = this.inboundList;
            this.saveInfo.addNetworkAclOutboundRuleRequestDto.networkAclRuleList = this.outboundList;

            if(!this.pass.networkAclName){
                alertMsg("Network ACL 이름을 확인하세요.",this.$refs.networkAclName);
                return false;
            }
console.log("생성하기 : " + JSON.stringify(this.saveInfo));//tmp
            confirmMsg("생성하시겠습니까?",this.save);
        },
        // ACL 생성 및 RULE 등록
        save : function() {
            post(TID.SAVE,
                "/my/management/instance/vpc/createNetworkAclAndAddRule",
                this.saveInfo,
                this.callback);
        },

        callback: function (tid, results) {
            switch (tid) {
                case TID.SAVE:
                    console.log(results);
                    this.saveCallback(results);
                    break;
            }
        },
        saveCallback: function (results) {
            //console.log(results);
            if (results.success) {
                console.log("## : " + results.response.makeInBoundError);//tmp
                console.log("## : " + results.response.makeOutBoundError);//tmp
                appPopSubet.$refs.popupsubnet.getAclCbList();
                // inbound or outbound rule 설정 에러 발생.--> 마이페이지 관리화면에서 수정
                if(!results.response.makeInBoundError || !results.response.makeOutBoundError) {
                    alertMsgRtn("정상적으로 생성되었습니다.\nACL 규칙 설정 실패. ", fnClosePopup('aclModal'));
                } else {
                    alertMsgRtn("정상적으로 생성되었습니다.", fnClosePopup('aclModal'));
                }

            } else {
                console.log(results);
                alertMsg(results.error.message);
            }
        },

    }
});

