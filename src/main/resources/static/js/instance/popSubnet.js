
/*
 * @name : popSubnet.js
 * @date : 2021-09-23 오전 10:46
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

let appPopSubet;
window.addEventListener('load', function() {
    appPopSubet = new Vue({
        el: '#popupwrapsubnet',
    });
});
Vue.component('popupsubnet', {
    template: "#popup-template-subnet",
    data: function () {
        return {
            saveInfo:{
                subnetName:"", // subnet 이름
                ipv4CidrBlock:"", // IP 허용범위
                zoneCode:"", // 가용ZONE
            },
            // zoneSelected:"", // 선택된 콤보박스
            selected:"",
            zoneList: [], // 가용Zone 콤보 리스트
        };
    },
    mounted: function () {
        this.getZoneList();
    },
    methods: {
        onclickPop: function (popId) {
            console.log("생성 팝업 : " + popId);//tmp
            document.getElementById(popId).style.display = "block";
            document.documentElement.style.overflowX = 'hidden';
            document.documentElement.style.overflowY = 'hidden';
        },
        // Subnet 콤보 리스트 조회
        getZoneList: function() {
            get("zoneList",
                "/my/management/instance/vpc/server/getZoneList",
                {},
                this.callback);
        },

        onclickCreateSave: function() {
            console.log("subnet 생성 클릭");//tmp
            console.log("subnet 이름 : " +  this.saveInfo.subnetName);//tmp
            console.log("subnet ipv4CidrBlock : " +  this.saveInfo.ipv4CidrBlock);//tmp
            console.log("subnet zoneCode : " +  this.saveInfo.zoneCode);//tmp

            if(!this.saveInfo.subnetName){
                alertMsg("Subnet 이름은 필수입니다.",this.$refs.subnetName);
                return false;
            }
            if(!this.saveInfo.ipv4CidrBlock){
                alertMsg("IP 주소 범위는 필수입니다.",this.$refs.ipv4CidrBlock);
                return false;
            }
            if(!this.saveInfo.zoneCode){
                alertMsg("가용Zone을 선택해주세요.", this.$refs.zoneCode );
                return false;
            }
            confirmMsg("생성하시겠습니까?", this.save);

        },
        save: function(){
            post(TID.SAVE,
                "/my/management/instance/vpc/createSubnet",
                this.saveInfo,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case "zoneList":
                    console.log(results);
                    if (results.success) {
                        this.zoneList = results.response.getZoneListResponse.zoneList;
                    } else {
                        //console.log(results);
                        alertMsg(results.error.message);
                    }
                    break;
                case TID.SAVE:
                    console.log(results);
                    this.saveCallback(results);
                    break;
            }
        },
        saveCallback: function (results) {
            if (results.success) {
                alertMsgRtn("정상적으로 생성되었습니다.", fnClosePopup('subnetModal'));
            } else {
                console.log(results);
                alertMsg(results.error.message);
            }
        },
        // 검색 selectebox 이벤트
        searchChange:function(data){
            this.saveInfo.zoneCode = data;
        }
    }
});

// 검색 selectebox 이벤트
function selectChange(){
    const data= document.querySelector("#zoneCode").value;
    appPopSubet.$refs.popupsubnet.searchChange(data);
}