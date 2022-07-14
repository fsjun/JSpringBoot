package org.example.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.example.security.pojo.JwtUser;

import java.util.Date;

@Slf4j
public class JwtUtil {


    //指定一个token过期时间（毫秒）
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;  //20分钟
    private static final String JWT_TOKEN_SECRET_KEY = "yourTokenKey";
    //↑ 记得换成你自己的秘钥

    public static String createJwtTokenByUser(JwtUser user) {

        String secret = JWT_TOKEN_SECRET_KEY;

        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);    //使用密钥进行哈希
        // 附带username信息的token
        return JWT.create()
                .withClaim("username", user.getUserName())
                .withClaim("roles", user.getRoles())
                //                .withClaim("permissions",permissionService.getPermissionsByUser(user))
                .withExpiresAt(date)  //过期时间
                .sign(algorithm);     //签名算法
        //r-p的映射在服务端运行时做，不放进token中
    }


    /**
     * 校验token是否正确
     */
    public static boolean verifyTokenOfUser(String token) throws TokenExpiredException {
        //user要从sercurityManager拿，确保用户用的是自己的token
        log.info("verifyTokenOfUser");
        String secret = JWT_TOKEN_SECRET_KEY;//

        //根据密钥生成JWT效验器
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("username", getUserName(token))//从不加密的消息体中取出username
                .build();
        //生成的token会有roles的Claim，这里不加不知道行不行。
        // 一个是直接从客户端传来的token，一个是根据盐和用户名等信息生成secret后再生成的token
        DecodedJWT jwt = verifier.verify(token);
        //能走到这里
        return true;
    }

    /**
     * 在token中获取到username信息
     */
    public static String getUserName(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static JwtUser recreateUserFromToken(String token) {
        JwtUser user = new JwtUser();
        DecodedJWT jwt = JWT.decode(token);

        user.setUserName(jwt.getClaim("username").asString());
        user.setRoles(jwt.getClaim("roles").asList(String.class));
        //r-p映射在运行时去取
        return user;
    }

    /**
     * 判断是否过期
     */
    public static boolean isExpire(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().getTime() < System.currentTimeMillis();
    }
}
