package h.uniview.smarthouse.controller;

import h.uniview.smarthouse.data.PropCenter;
import h.uniview.smarthouse.data.UserXMLCenter;
import h.uniview.smarthouse.entity.UserEntity;
import h.uniview.smarthouse.service.UserService;
import h.uniview.smarthouse.utils.MD5Utils;
import h.uniview.smarthouse.utils.R;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController extends BaseController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String CODE_KEY = "_code";

    @Autowired
    private PropCenter propCenter;

    @Autowired
    private UserService userService;

    @Autowired
    private UserXMLCenter userXMLCenter;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public R login(String username, String password, Boolean rememberMe) {
        // 密码 MD5 加密
        password = MD5Utils.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        try {
            Subject subject = getSubject();
            if (subject != null)
                subject.logout();
            super.login(token);
            return R.ok();
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            return R.error(e.getMessage());
        } catch (AuthenticationException e) {
            return R.error("认证失败！");
        }
    }

    @PostMapping("/user/update/password")
    @ResponseBody
    public R updatePassword(String username, String oldPwd, String newPwd, String cfmPwd) {
        try {
            UserEntity user = getCurrentUser();
            if(!username.equals(user.getUsername())) {
                return R.error("System Error.");
            }

            String password = MD5Utils.encrypt(username.toLowerCase(), oldPwd);
            if(!password.equals(user.getPassword())) {
                return R.error("Raw password is error.");
            }

            if(!newPwd.equals(cfmPwd)) {
                return R.error("New password is not same with confirm password.");
            }
            String newPassword = MD5Utils.encrypt(user.getUsername().toLowerCase(), newPwd);
            userXMLCenter.updatePassword(username, newPassword);
            user.setPassword(newPassword);

            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @RequestMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @GetMapping("/403")
    public String forbid() {
        return "403";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        // 登录成后，即可通过 Subject 获取登录的用户信息
        UserEntity user = super.getCurrentUser();
        model.addAttribute("user", user);
        return "index";
    }
}
