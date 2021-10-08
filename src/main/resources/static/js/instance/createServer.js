
let appMain;
let openStorageStatCode;
const TID = {
    GET_SERVER_SPEC      : {value: 0, name: "getServerSpec", code: "S",
        uri: "/my/management/instance/classic/server/getServerProductList"},
    CREATE_SERVER      : {value: 1, name: "createServer", code: "C",
        uri: "/my/management/instance/classic/server/createServerInstances"},

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
                serverImageProductCode: "SPSW0LINUX000139",
                productType: "STAND",
                storageType : "SSD",
                memorySize: "4",
                serverProductCode:"",
                feeSystemTypeCode:"MTRAT",
                serverName:"servername", // 소문자만 가능
                serverDescription:"serverDesc",
            },
            serverOsType: [
                {desc:"CentOS 7.8 (64-bit)", productCode:"SPSW0LINUX000139", productName:"centos-7.8-64"},
                {desc:"Ubuntu Server 16.04 (64-bit)", productCode:"SPSW0LINUX000029", productName:"ubuntu-16.04-64-server"},
                {desc:"Windows Server 2016 (64-bit)", productCode:"SPSW0WINNTEN0016A", productName:"Windows Server 2016 (64-bit) English Edition"}
            ],
            serverProductType:[
                {code:"HICPU", codeName:"High CPU"},
                {code:"STAND", codeName:"Standard"},
                {code:"HIMEM", codeName:"High Memory"},
                {code:"MICRO", codeName:"Micro Server"},
                {code:"COMPT", codeName:"Compact Server"},
                {code:"CPU", codeName:"CPU"},
                {code:"GPU", codeName:"GPU"},
                {code:"VDS", codeName:"VDS Server"}
            ],
            storageType:[
                {code:"SSD", codeName:"SSD"},
                {code:"HDD", codeName:"HDD"}
            ],
            memorySize:[
                {code:"1", codeName:"1GB"},
                {code:"2", codeName:"2GB"},
                {code:"4", codeName:"4GB"},
                {code:"8", codeName:"8GB"},
                {code:"16", codeName:"16GB"},
                {code:"32", codeName:"32GB"},
                {code:"64", codeName:"64GB"},
                {code:"128", codeName:"128GB"},
            ],
            specs:[

            ],
            feeSystemTypeCode:[
                {code:"FXSUM", codeName:"월 정액제"},
                {code:"MTRAT", codeName:"시간 요금제"},
            ],
            messages : "",
        };
    },
    mounted:function(){
        this.getServerSpec();
    },
    methods:{
        onKeyup:function (e){
            if (e.keyCode == 13){
            }
        },
        getServerSpec:function () {
            get(TID.GET_SERVER_SPEC,
                TID.GET_SERVER_SPEC.uri,
                this.cond,
                this.callback);
        },
        callback: function (tid, results) {
            switch (tid) {
                case TID.GET_SERVER_SPEC:
                    this.gerServerSpecCallback(results);
                    break;
                case TID.CREATE_SERVER:
                    this.createServerCallback(results);
                    break;
            }
        },
        gerServerSpecCallback: function(results){
            this.specs = results.response;
            if (this.specs.length == 0){
                this.specs.unshift({productDescription:"유효한 상품이 없습니다"});
            }else{
                this.specs.unshift({productDescription:"선택하세요"});
            }
            this.cond.serverProductCode = null;
        },
        request: function (){
            get(TID.CREATE_SERVER,
                TID.CREATE_SERVER.uri,
                this.cond,
                this.callback);
        },
        createServerCallback: function (results){
            console.log(results);
        }
    }
});

