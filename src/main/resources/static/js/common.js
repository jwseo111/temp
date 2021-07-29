

// main-nav
$(document).ready(function(){
    $("#mainNav, #mainNavPop").hover(
        function () {
            $(".nav-cont").stop().slideDown(400);
        },
        function () {
            $(".nav-cont").stop().slideUp(400);
        }
    );

});


// data rolling

$(function () {

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



 // category tab
 function openCate(evt, tabName) {
  var i, catecontent, catelinks;
  catecontent = document.getElementsByClassName("cate-content");
  for (i = 0; i < catecontent.length; i++) {
    catecontent[i].style.display = "none";
  }
  catelinks = document.getElementsByClassName("catelinks");
  for (i = 0; i < catelinks.length; i++) {
    catelinks[i].className = catelinks[i].className.replace(" active", "");
  }
  document.getElementById(tabName).style.display = "block";
  evt.currentTarget.className += " active";
}
