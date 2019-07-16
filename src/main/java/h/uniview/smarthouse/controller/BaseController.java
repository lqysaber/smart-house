package h.uniview.smarthouse.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import h.uniview.smarthouse.entity.UserEntity;

public class BaseController {

    protected static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    protected UserEntity getCurrentUser() {
        return (UserEntity) getSubject().getPrincipal();
    }

    protected Session getSession() {
        return getSubject().getSession();
    }

    protected Session getSession(Boolean flag) {
        return getSubject().getSession(flag);
    }

    protected void login(AuthenticationToken token) {
        getSubject().login(token);
    }
    
    protected String handleError(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();
        for (ObjectError oe : bindingResult.getAllErrors()) {
            msg.append(oe.getDefaultMessage()).append(";");
        }
        return msg.toString();
    }

}
