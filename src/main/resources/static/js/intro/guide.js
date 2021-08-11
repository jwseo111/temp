
/*
 * @name : popAgencySearch.js
 * @date : 2021-06-23 오후 1:05
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

let appMain;

window.onload = function () {
    appMain = new Vue({
        el: '#maincontentswrap',
    });
}

Vue.component('maincontents', {
    template: "#main-template",
    data: function () {
        return {
            message:"",
            tab1: true,
            tab2: false,
            tab3: false,
        };
    },
    mounted: function () {
        if(!isNull(tabs)){
            this.onclickTab(tabs);
        }
    },
    methods: {
        // tab 클릭
        onclickTab: function(no){
            if(no == 1){
                this.tab1 =true;
                this.tab2 =false;
                this.tab3 =false;
            }else if(no == 2){
                this.tab1 =false;
                this.tab2 =true;
                this.tab3 =false;

            }else if(no == 3){
                this.tab1 =false;
                this.tab2 =false;
                this.tab3 =true;

            }

        },

    },
});