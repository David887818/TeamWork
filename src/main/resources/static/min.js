var modal = document.getElementById('idF01');

window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
var modalOne = document.getElementById('idF02');

window.onclick = function (event) {
    if (event.target == modalOne) {
        modal.style.display = "none";
    }
}
var modal = document.getElementById('idF03');

window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
var modalOne = document.getElementById('idF04');

window.onclick = function (event) {
    if (event.target == modalOne) {
        modal.style.display = "none";
    }
}

/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */
function myFunction() {
    document.getElementById("myDropdown2").classList.remove("show");
    document.getElementById("myDropdown4").classList.remove("show");
    document.getElementById("myDropdown3").classList.remove("show");
    document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function (event) {
    if (!event.target.matches('.dropbtn')) {

        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}


$(document).ready(function () {
    $(".click").click(function () {
        $(".panel").slideToggle("slow");
    });
    $(".clickForm").click(function () {
        $(".panel").slideDown();
    });
});




// Get the modal
var modal = document.getElementById('myModal');

// Get the button that opens the modal
var btn = document.getElementById("myBtn");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the modal
btn.onclick = function() {
    modal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}



function myFunction2() {
    document.getElementById("myDropdown2").classList.toggle("show");
    document.getElementById("myDropdown4").classList.remove("show");
    document.getElementById("myDropdown3").classList.remove("show");
    document.getElementById("myDropdown").classList.remove("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
    if (!event.target.matches('.dropbtn2')) {

        var dropdowns2 = document.getElementsByClassName("dropdown-content2");
        var i;
        for (i = 0; i < dropdowns2.length; i++) {
            var openDropdown2 = dropdowns2[i];
            if (openDropdown2.classList.contains('show')) {
                openDropdown2.classList.remove('show');
            }
        }
    }
}
function myFunction3() {
    document.getElementById("myDropdown4").classList.remove("show");
    document.getElementById("myDropdown3").classList.toggle("show");
    document.getElementById("myDropdown2").classList.remove("show");
    document.getElementById("myDropdown").classList.remove("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
    if (!event.target.matches('.dropbtn3')) {

        var dropdowns2 = document.getElementsByClassName("dropdown-content3");
        var i;
        for (i = 0; i < dropdowns3.length; i++) {
            var openDropdown3 = dropdowns3[i];
            if (openDropdown3.classList.contains('show')) {
                openDropdown3.classList.remove('show');
            }
        }
    }
}
function myFunction4() {
    document.getElementById("myDropdown4").classList.toggle("show");
    document.getElementById("myDropdown3").classList.remove("show");
    document.getElementById("myDropdown2").classList.remove("show");
    document.getElementById("myDropdown").classList.remove("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
    if (!event.target.matches('.dropbtn4')) {

        var dropdowns2 = document.getElementsByClassName("dropdown-content4");
        var i;
        for (i = 0; i < dropdowns4.length; i++) {
            var openDropdown4 = dropdowns4[i];
            if (openDropdown4.classList.contains('show')) {
                openDropdown4.classList.remove('show');
            }
        }
    }
}

//
//
// $(function () {
//     $(".heading-compose").click(function () {
//         $(".side-two").css({
//             "left": "0"
//         });
//     });
//
//     $(".newMessage-back").click(function () {
//         $(".side-two").css({
//             "left": "-100%"
//         });
//     });
// })