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


// family site
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}
window.onclick = function(event) {
    document.getElementById("myDropdown").classList.toggle("active");
    if (!event.target.matches('.dropbtn')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
            if (openDropdown.classList.contains('active')) {
                openDropdown.classList.remove('active');
            }
        }
    }
}