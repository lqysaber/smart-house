package h.uniview.smarthouse.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import h.uniview.smarthouse.data.PropCenter;
import h.uniview.smarthouse.oauth2.OAuth2Filter;
import h.uniview.smarthouse.oauth2.OAuth2Realm;

@Configuration
public class ShiroConfig {

	@Autowired
	private PropCenter propCenter;

	/**
	 * session 管理对象
	 * 
	 * @return DefaultWebSessionManager
	 */
	@Bean("sessionManager")
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 设置session超时时间，单位为毫秒
		sessionManager.setGlobalSessionTimeout(propCenter.getShiro().getSessionTimeout());
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionIdCookieEnabled(true);
		return sessionManager;
	}

	@Bean("securityManager")
	public SecurityManager securityManager(OAuth2Realm oaAuth2Realm, SessionManager sessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(oaAuth2Realm);
		// 配置 rememberMeCookie
		securityManager.setRememberMeManager(rememberMeManager());
		securityManager.setSessionManager(sessionManager);
		return securityManager;
	}

	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();

		Map<String, Filter> filters = shiroFilter.getFilters();// 获取filters
		filters.put("oauth2", new OAuth2Filter());

		// 设置 securityManager
		shiroFilter.setSecurityManager(securityManager);
		// 登录的 url
		shiroFilter.setLoginUrl(propCenter.getShiro().getLoginUrl());
		// 登录成功后跳转的 url
		shiroFilter.setSuccessUrl(propCenter.getShiro().getSuccessUrl());
		// 未授权 url
		shiroFilter.setUnauthorizedUrl(propCenter.getShiro().getUnauthorizedUrl());

		LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 设置免认证 url
		String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(propCenter.getShiro().getAnonUrl(), ",");
		for (String url : anonUrls) {
			filterChainDefinitionMap.put(url, "anon");
		}
		// 配置退出过滤器，其中具体的退出代码 Shiro已经替我们实现了
		filterChainDefinitionMap.put(propCenter.getShiro().getLogoutUrl(), "logout");
		// 除上以外所有 url都必须认证通过才可以访问，未通过认证自动访问 LoginUrl
		filterChainDefinitionMap.put("/**", "oauth2");

		shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilter;
	}

	/**
	 * rememberMe cookie 效果是重开浏览器后无需重新登录
	 *
	 * @return SimpleCookie
	 */
	private SimpleCookie rememberMeCookie() {
		// 设置 cookie 名称，对应 login.html 页面的 <input type="checkbox" name="rememberMe"/>
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		// 设置 cookie 的过期时间，单位为秒，这里为一天
		cookie.setMaxAge(propCenter.getShiro().getCookieTimeout());
		return cookie;
	}

	/**
	 * cookie管理对象
	 *
	 * @return CookieRememberMeManager
	 */
	private CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		// rememberMe cookie 加密的密钥
		cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
		return cookieRememberMeManager;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 用于开启 Thymeleaf 中的 shiro 标签的使用
	 *
	 * @return ShiroDialect shiro 方言对象
	 */
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
		proxyCreator.setProxyTargetClass(true);
		return proxyCreator;
	}

}
