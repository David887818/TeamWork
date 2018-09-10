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