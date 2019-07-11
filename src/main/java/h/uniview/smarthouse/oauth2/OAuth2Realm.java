package h.uniview.smarthouse.oauth2;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import h.uniview.smarthouse.entity.UserEntity;
import h.uniview.smarthouse.service.UserService;

@Component
public class OAuth2Realm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	/**
	 * 授权(验证权限时调用)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		return null;
	}

	/**
	 *  认证(登录时调用)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		// 获取用户输入的用户名和密码
		String userName = (String) token.getPrincipal();
		String password = new String((char[]) token.getCredentials());

		// 通过用户名到数据库查询用户信息
		UserEntity user = this.userService.findByName(userName);

		if (user == null)
			throw new UnknownAccountException("用户名或密码错误！");
		if (!password.equals(user.getPassword()))
			throw new IncorrectCredentialsException("用户名或密码错误！");
		return new SimpleAuthenticationInfo(user, password, getName());
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof OAuth2Token;
	}

}
