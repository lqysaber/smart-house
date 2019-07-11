package h.uniview.smarthouse.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

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

}
