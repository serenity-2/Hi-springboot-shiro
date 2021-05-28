package com.jzjr.springbootshiro.config;

import com.jzjr.springbootshiro.filter.AnyRolesAuthorizationFilter;
import com.jzjr.springbootshiro.filter.JwtAuthFilter;
import com.jzjr.springbootshiro.realms.DbShiroRealm;
import com.jzjr.springbootshiro.realms.JwtRealm;
import com.jzjr.springbootshiro.service.UserService;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager, UserService userService) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
        shiroFilterFactoryBean.setLoginUrl("/login.jsp");
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("authcToken", createJwtFilter(userService));
        filters.put("anyRole", createRolesFilter());
        return shiroFilterFactoryBean;
    }

    @Bean
    protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/user/login", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/logout", "noSessionCreation,authcToken[permissive]");
        chainDefinition.addPathDefinition("/image/**", "anon");
        //只允许admin或manager角色的用户访问
        chainDefinition.addPathDefinition("/article/admin", "noSessionCreation,authcToken,anyRole[manager]");
        chainDefinition.addPathDefinition("/article/list", "noSessionCreation,authcToken");
        chainDefinition.addPathDefinition("/article/*", "noSessionCreation,authcToken[permissive]");
        chainDefinition.addPathDefinition("/**", "noSessionCreation,authcToken");
        return chainDefinition;
    }

    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(UserService userService) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealms(Arrays.asList(getJwtRealm(userService), getDbRealm(userService)));
        return defaultWebSecurityManager;
    }

    @Bean
    public Realm getDbRealm(UserService userService) {
        DbShiroRealm dbShiroRealm = new DbShiroRealm(userService);
        //凭证校验匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //设置散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        dbShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return dbShiroRealm;
    }

    @Bean
    public Realm getJwtRealm(UserService userService) {
        JwtRealm jwtRealm = new JwtRealm(userService);
        return jwtRealm;
    }

    @Bean
    public Authenticator authenticator(UserService userService) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setRealms(Arrays.asList(getJwtRealm(userService), getDbRealm(userService)));
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }

    public JwtAuthFilter createJwtFilter(UserService userService) {
        return new JwtAuthFilter(userService);
    }

    protected AnyRolesAuthorizationFilter createRolesFilter(){
        return new AnyRolesAuthorizationFilter();
    }
    /**
     * 由于jwt无状态，这里需要禁用session，不保证用户登录状态，每次请求都重新认证
     * 需要注意的是，如果用户代码里调用Subject.getSession()还是可以用session，如果要完全禁用，要配合下面的noSessionCreation的Filter来实现
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }
}
