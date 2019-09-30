function login() {
    var $loginButton = $("#loginButton");
    var username = $("#exampleInputUsername").val().trim();
    var password = $("#exampleInputPassword").val().trim();
    // var rememberMe = $("#primary").is(':checked');
    var rememberMe = 1;
    if (username === "") {
        $MB.n_warning("请输入用户名！");
        return;
    }
    if (password === "") {
        $MB.n_warning("请输入密码！");
        return;
    }

    $.ajax({
        type: "post",
        url: ctx + "login",
        data: {
            "username": username,
            "password": password,
            "rememberMe": rememberMe
        },
        dataType: "json",
        success: function (r) {
            if (r.code === 0) {
                location.href = ctx + 'index';
            } else {
                $MB.n_warning(r.msg);
            }
        }
    });
}

document.onkeyup = function (e) {
    if (window.event)
        e = window.event;
    var code = e.charCode || e.keyCode;
    if (code === 13) {
        login();
    }
};