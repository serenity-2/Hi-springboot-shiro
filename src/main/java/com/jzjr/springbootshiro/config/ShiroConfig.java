package com.jzjr.springbootshiro.config;

import com.jzjr.springbootshiro.cache.RedisCacheManager;
import com.jzjr.springbootshiro.filter.AnyRolesAuthorizationFilter;
import com.jzjr.springbootshiro.filter.JwtAuthFilter;
import com.jzjr.springbootshiro.realms.DbShiroRealm;
import com.jzjr.springbootshiro.realms.JwtRealm;
import com.jzjr.springbootshiro.realms.VerifyRealm;
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
    /**
     *注册shiro的Filter，拦截请求
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager, UserService userService) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
//        filters.put("authcToken", createRolesFilter());
//        filters.put("anyRole", createRolesFilter());

//        filters.put("authcToken", createJwtFilter(userService));

        return shiroFilterFactoryBean;
    }

    @Bean
    protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        //anno表示无需认证即可访问，authc表示需要认证才能访问（禁用session后就不能使用authc）
        chainDefinition.addPathDefinition("/user/login", "noSessionCreation,anon");
        //noSessionCreation表示该接口禁止创建session
        chainDefinition.addPathDefinition("/user/verityCode/login", "anon");
//        chainDefinition.addPathDefinition("/article/admin","perms[article:manager]");
        //做用户认证，permissive参数的作用是当token无效时也允许请求访问，不会返回鉴权未通过的错误
//        chainDefinition.addPathDefinition("/logout", "noSessionCreation,authcToken[permissive]");
//        chainDefinition.addPathDefinition("/image/**", "anon");
        //只允许admin或manager角色的用户访问
//        chainDefinition.addPathDefinition("/article/admin", "noSessionCreation,anyRole[admin,manager]");
//        chainDefinition.addPathDefinition("/article/list", "noSessionCreation,authcToken");
//        chainDefinition.addPathDefinition("/article/**", "noSessionCreation,authcToken");
//        chainDefinition.addPathDefinition("/article/*", "noSessionCreation,authcToken[permissive]");
        // 默认进行用户鉴权
        chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;
    }

    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(UserService userService) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
//        defaultWebSecurityManager.setRealms(Arrays.asList(getJwtRealm(userService), getDbRealm(userService)));
        defaultWebSecurityManager.setRealm(getVerifyRealm());
//        defaultWebSecurityManager.setCacheManager(new RedisCacheManager());
        return defaultWebSecurityManager;
    }

    /**
     * 用于用户名密码登录时认证的realm
     */
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

    /**
     * 用于JWT token认证的realm
     */
    @Bean
    public Realm getJwtRealm(UserService userService) {
        JwtRealm jwtRealm = new JwtRealm(userService);
        return jwtRealm;
    }

    /**
     * 用于短信验证码认证的realm
     */
    @Bean
    public Realm getVerifyRealm() {
        VerifyRealm verifyRealm = new VerifyRealm();
        return verifyRealm;
    }

    /**
     * 初始化Authenticator
     */
    @Bean
    public Authenticator authenticator(UserService userService) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        //设置两个Realm，一个用于用户登录验证和访问权限获取；一个用于jwt token的认证
//        authenticator.setRealms(Arrays.asList(getJwtRealm(userService), getDbRealm(userService)));
        authenticator.setRealms(Arrays.asList(getVerifyRealm()));
        //设置多个realm认证策略，一个成功即跳过其它的
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
//    @Bean
//    protected SessionStorageEvaluator sessionStorageEvaluator() {
//        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
//        sessionStorageEvaluator.setSessionStorageEnabled(true);
//        return sessionStorageEvaluator;
//    }
}
