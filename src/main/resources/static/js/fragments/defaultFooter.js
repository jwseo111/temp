var appFooter;
window.addEventListener('load', function() {
    appFooter = new Vue({
        el: '#footercontentswrap',
    });
});

Vue.component('footercontents', {
    template : "#footer-template",
    data: function() {
        return {
            messages : "",
        };
    },
    mounted:function(){
    },
    methods:{
        hoverParent : function (menu, b){
            if (b){
                menu.style.display = "block";
            } else {
                menu.style.display = "none";
            }
        },
        onclickMenu : function(child){
            location.href = child.uri;
        }
    }
});