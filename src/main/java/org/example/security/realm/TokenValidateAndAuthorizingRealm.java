package org.example.security.realm;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.example.security.pojo.JwtUser;
import org.example.security.util.JwtUtil;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class TokenValidateAndAuthorizingRealm extends AuthorizingRealm {

    //权限管理部分的代码先行略过
    //......

    public TokenValidateAndAuthorizingRealm() {
        //CredentialsMatcher，自定义匹配策略（即验证jwt token的策略）
        super(new CredentialsMatcher() {
            @Override
            public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
                BearerToken bearerToken = (BearerToken) authenticationToken;
                String bearerTokenString = bearerToken.getToken();
                log.debug(bearerTokenString);
                boolean verified = JwtUtil.verifyTokenOfUser(bearerTokenString);
                return verified;
            }
        });
    }

    @Override
    public String getName() {
        return "TokenValidateAndAuthorizingRealm";
    }

    @Override
    public Class getAuthenticationTokenClass() {
        //设置由本realm处理的token类型。BearerToken是在filter里自动装配的。
        return BearerToken.class;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        boolean res = super.supports(token);
        log.debug("[TokenValidateRealm is supports]" + res);
        return res;
    }

    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException, TokenExpiredException {
        log.debug("doGetAuthenticationInfo 将token装载成用户信息");

        BearerToken bearerToken = (BearerToken) authenticationToken;
        String bearerTokenString = bearerToken.getToken();

        JwtUser jwtUser = JwtUtil.recreateUserFromToken(bearerTokenString);//只带着用户名和roles

        SimpleAuthenticationInfo res = new SimpleAuthenticationInfo(jwtUser, bearerTokenString, this.getName());
        /*Constructor that takes in an account's identifying principal(s) and its corresponding credentials that verify the principals.*/
        //        这个返回值是造Subject用的，返回值供createSubject使用
        return res;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        JwtUser user = (JwtUser) SecurityUtils.getSubject().getPrincipal();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(user.getRoles());
        log.info("doGetAuthorizationInfo {}", user.getRoles());

        Set<String> stringPermissions = new HashSet<String>();
        stringPermissions.add("pd");
        simpleAuthorizationInfo.addStringPermissions(stringPermissions);
        return simpleAuthorizationInfo;
    }
}
