package org.example.security.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.example.security.pojo.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class UsernamePasswordRealm extends AuthenticatingRealm {

    /*构造器里配置Matcher*/
    public UsernamePasswordRealm() {
        super();
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);//密码保存策略一致，2次md5加密
        this.setCredentialsMatcher(hashedCredentialsMatcher);
    }

    /**
     * 通过该方法来判断是否由本realm来处理login请求
     *
     * 调用{@code doGetAuthenticationInfo(AuthenticationToken)}之前会shiro会调用{@code supper.supports(AuthenticationToken)}
     * 来判断该realm是否支持对应类型的AuthenticationToken,如果相匹配则会走此realm
     *
     * @return
     */
    @Override
    public Class getAuthenticationTokenClass() {
        return UsernamePasswordToken.class;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        //继承但啥都不做就为了打印一下info
        boolean res = super.supports(token);//会调用↑getAuthenticationTokenClass来判断
        log.debug("[UsernamePasswordRealm is supports]" + res);
        return res;
    }

    /**
     * 用户名和密码验证，login接口专用。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {

        UsernamePasswordToken usernamePasswordToken=(UsernamePasswordToken) token;
        String userName = usernamePasswordToken.getUsername();
        if (!"test".equals(userName)) {
            log.error("userName is not found, userName:{}", userName);
            return null;
        }

        String passwordFromDB = "ea48576f30be1669971699c09ad05c94";
        String salt = "123456";

        //在使用jwt访问时，shiro中能拿到的用户信息只能是token中携带的jwtUser，所以此处保持统一。
        JwtUser jwtUser=new JwtUser();
        jwtUser.setUserName(userName);
        List<String> roles = new ArrayList<>();
        roles.add("user");
        jwtUser.setRoles(roles);
        SimpleAuthenticationInfo res = new SimpleAuthenticationInfo(jwtUser, passwordFromDB, ByteSource.Util.bytes(salt), getName());
        return res;
    }
}
