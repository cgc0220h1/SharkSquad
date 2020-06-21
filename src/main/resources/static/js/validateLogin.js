let firstName = document.getElementById("firstName");
let lastName = document.getElementById("lastName");
let phone = document.getElementById("phone");
let email = document.getElementById("email");
let username = document.getElementById("username");
let password = document.getElementById("password");

let firstNameError = document.getElementById("firstNameError");
let lastNameError = document.getElementById("lastNameError");
let phoneError = document.getElementById("phoneError");
let emailError = document.getElementById("emailError");
let usernameError = document.getElementById("usernameError");
let passwordError = document.getElementById("passwordError");

let namePattern = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ" +
    "ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ" +
    "ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$";
let phonePattern = "^0[0-9]{9}$";
let emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
let usernamePattern = "^[a-z0-9_-]{3,16}$";

firstName.addEventListener("blur", firstNameVerify, true);
lastName.addEventListener("blur", lastNameVerify, true);
phone.addEventListener("blur", phoneVerify, true);
email.addEventListener("blur", emailVerify, true);
username.addEventListener("blur", usernameVerify, true);
password.addEventListener("blur", passwordVerify, true);

function validate() {
    if (firstName.value == "") {
        firstNameError.textContent = "Không được để trống Tên";
        firstName.focus();
        return false;
    } else if (!firstName.value.match(namePattern)) {
        firstNameError.textContent = "Tên bắt đầu bằng chứ cái";
        firstName.focus();
        return false;
    }
    if (lastName.value == "") {
        lastNameError.textContent = "Không được để trống Họ và tên đệm";
        lastName.focus();
        return false;
    } else if (!lastName.value.match(namePattern)) {
        lastNameError.textContent = "Họ và tên đệm bắt đầu bằng chứ cái";
        lastName.focus();
        return false;
    }
    if (phone.value == "") {
        phoneError.textContent = "Không được để trống Số điện thoại";
        phone.focus();
        return false;
    } else if (!phone.value.match(phonePattern)) {
        phoneError.textContent = "Không đúng định dạng Số điện thoại";
        phone.focus();
        return false;
    }
    if (email.value == "") {
        emailError.textContent = "Không được để trống Email";
        email.focus();
        return false;
    } else if (!email.value.match(emailPattern)) {
        emailError.textContent = "Định dạng email không phù hợp";
        email.focus();
        return false;
    }
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

function firstNameVerify() {
    if (firstName != "") {
        firstNameError.innerHTML = "";
        return true;
    }
}

function lastNameVerify() {
    if (lastName != "") {
        lastNameError.innerHTML = "";
        return true;
    }
}

function phoneVerify() {
    if (phone != "") {
        phoneError.innerHTML = "";
        return true;
    }
}

function emailVerify() {
    if (email != "") {
        emailError.innerHTML = "";
        return true;
    }
}

function passwordVerify() {
    if (username != "") {
        usernameError.innerHTML = "";
        return true;
    }
}

function usernameVerify() {
    if (username != "") {
        usernameError.innerHTML = "";
        return true;
    }
}