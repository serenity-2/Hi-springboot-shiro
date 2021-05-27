package com.jzjr.springbootshiro.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import com.jzjr.springbootshiro.shiro.realms.MyRealm;
import java.util.HashMap;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        //配置系统受限资源
        HashMap<String, String> map = new HashMap<>();
        //别忘了加 /
        map.put("/index.jsp","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        shiroFilterFactoryBean.setLoginUrl("/login.jsp");
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("getRealm") Realm myRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(myRealm);
        return defaultWebSecurityManager;
    }

    @Bean
    public Realm getRealm(){
        MyRealm myRealm = new MyRealm();
        //凭证校验匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //设置散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        //开启缓存管理
        myRealm.setCacheManager(new RedisCacheManager());
        myRealm.setCachingEnabled(true);//开启全局缓存
        myRealm.setAuthenticationCachingEnabled(true);//认证认证缓存
        myRealm.setAuthenticationCacheName("authenticationCache");
        myRealm.setAuthorizationCachingEnabled(true);//开启授权缓存
        myRealm.setAuthorizationCacheName("authorizationCache");
        return myRealm;
    }
}
