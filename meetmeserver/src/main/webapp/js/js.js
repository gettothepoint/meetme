// JavaScript Document

$(document).ready(function(){
    $("#registration").validate();
  });


          
                function myFunction() {
                    var pwd = document.getElementById("pwd").value;
                    var pwdc = document.getElementById("pwdc").value;
                    var ok = true;
                    if (pwd.length == 0) {
                        alert("Du hast kein Passwort eingegeben!")
                        document.getElementById("pwd").style.borderColor = "#E34234";
                        document.getElementById("pwdc").style.borderColor = "#E34234";
                        ok = false;
                    }
                    if (pwd != pwdc) {
                        alert("Deine Passwörter stimmen nicht überein!");
                        document.getElementById("pwd").style.borderColor = "#E34234";
                        document.getElementById("pwdc").style.borderColor = "#E34234";
                        ok = false;
                    }
                    else {
                    }
                    return ok;
                }
         