package org.example.security.realm;

import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.boot.autoconfigure.ShiroBeanAutoConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;

@Configuration
public class ShiroConfig extends ShiroBeanAutoConfiguration {

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

//        Map<String, Filter> filterMap = new LinkedHashMap<>();
//        filterMap.put("jwt",  new JwtFilter());
//        shiroFilterFactoryBean.setFilters(filterMap);
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("cors",new CorsFilter());
        filterMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
//
        Map<String, String> filterRuleMap = new LinkedHashMap<String, String>();
//        filterRuleMap.put("/index","anon");
        filterRuleMap.put("/swagger-ui/**","anon");
        filterRuleMap.put("/swagger-resources/**","anon");
        filterRuleMap.put("/v3/api-docs/**", "anon");
        filterRuleMap.put("/login","anon");
        filterRuleMap.put("/error","anon");
        filterRuleMap.put("/favicon.ico", "anon");
        filterRuleMap.put("/demo/**","anon");
//        filterRuleMap.put("/**", "authcBearer");
        filterRuleMap.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);

//        shiroFilterFactoryBean.setLoginUrl("/login");
//        shiroFilterFactoryBean.setGlobalFilters(Arrays.asList("cors", "noSessionCreation"));
        shiroFilterFactoryBean.setGlobalFilters(Arrays.asList("noSessionCreation"));

        return shiroFilterFactoryBean;
    }

    @Bean
    protected Authorizer authorizer() {
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        return authorizer;
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
