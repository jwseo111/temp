let appMain;
const TID = {
    MAIN: {value: 0, name: "main", code: "S"}
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
            messages : "",
        };
    },
    mounted:function(){
    },
    methods:{
    }
});

