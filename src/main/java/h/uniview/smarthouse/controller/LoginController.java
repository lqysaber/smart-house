package h.uniview.smarthouse.controller;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
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

import h.uniview.smarthouse.data.PropCenter;
import h.uniview.smarthouse.entity.UserEntity;
import h.uniview.smarthouse.service.UserService;
import h.uniview.smarthouse.utils.MD5Utils;
import h.uniview.smarthouse.utils.R;

@Controller
public class LoginController extends BaseController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String CODE_KEY = "_code";

    @Autowired
    private PropCenter propCenter;

    @Autowired
    private UserService userService;

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
