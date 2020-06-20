let username = document.forms["userForm"]["username"];
let password = document.forms["userForm"]["password"];

let usernameError = document.getElementById("usernameError");
let passwordError = document.getElementById("passwordError");

let usernamePattern = "^[a-z0-9_-]{3,16}$";


username.addEventListener("blur", usernameVerify, true);
password.addEventListener("blur", passwordVerify, true);

function validate() {
    if (username.value == "") {
        usernameError.textContent = "Không được để trống Tên đăng nhập";
        username.focus();
        return false;
    } else if (!username.value.match(usernamePattern)) {
        usernameError.textContent = "Tên đăng nhập có độ dài từ 3 - 16 ký tự";
        username.focus();
        return false;
    }
    if (password.value == "") {
        passwordError.textContent = "Không được để trống Mật khẩu";
        password.focus();
        return false;
    }
}

function passwordVerify() {
    if (username != "") {
        username.style.border = "#555555";
        usernameError.innerHTML = "";
        return true;
    }
}

function usernameVerify() {
    if (username != "") {
        username.style.border = "#555555";
        usernameError.innerHTML = "";
        return true;
    }
}