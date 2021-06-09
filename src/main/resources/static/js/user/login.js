var appMain;
var TID = {
    LOGIN : {value : 0, name : "login", code : "S"}
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
            login:{
                email : "",
                password : "",
            },
            messages : "",
        };
    },
    mounted:function(){
    },
    methods:{
        hideMessage : function(){
            this.messages = "";
        },
        callback : function(tid, results){
            switch (tid) {
                case TID.LOGIN:
                    this.loginCallback(results);
                break;
            }
        },
        loginCallback: function(results){
            if(results.success){
                console.log(results);
            }else{
                this.messages = results.error.message;
                setTimeout(this.hideMessage, 3000);
            }
        },
        onclickLogin : function(){
            post(TID.LOGIN, "/login", this.login, this.callback);
        },
        onkeyupForm :function(e){
            if(e.keyCode == 13){
                this.onclickLogin();
            }
        },
    }
});
