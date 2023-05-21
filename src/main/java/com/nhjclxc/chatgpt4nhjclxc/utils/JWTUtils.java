package com.nhjclxc.chatgpt4nhjclxc.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author LuoXianchao
 * @since 2023/5/21 11:32
 */
public class JWTUtils {
    /**
     * 过期时间 单位/毫秒
     */
    public static final Long JWTTIMEOUT = 1 * 24 * 60 * 60 * 1000L;

    /**
     * 时间的单位
     */
    public static final TimeUnit JWTTimeUnit = TimeUnit.MILLISECONDS;
    /**
     * 签名
     */
    private static final String SIGNATURE = "ascs$%^*()a5EWVD1651#$*((^%";


    /**
     * 获取token
     *
     * @param info
     * @return
     */
    public static String getToken(Map<String, Object> info) {
        //获取jwt构建对象
        JwtBuilder jwtBuilder = Jwts.builder();

        //返回token
        return jwtBuilder
                //设置头
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //设置payload
                .setClaims(info)  //设置用户信息
                .setSubject("userLogin")
                .setExpiration(new Date(System.currentTimeMillis() + JWTTIMEOUT))
                .setId(UUID.randomUUID().toString())
                //设置签名
                .signWith(SignatureAlgorithm.HS256, SIGNATURE)
                //连接
                .compact();
    }

    /**
     * 验证token
     */
    public static Claims verifyToken(String token) {

        //获取解析对象
        JwtParser jwtParser = Jwts.parser();
        //解析并返回
        Jws<Claims> claimsJws = jwtParser.setSigningKey(SIGNATURE).parseClaimsJws(token);
        return claimsJws.getBody();

    }
    // public static Claims verifyToken(String token) {
    //     try {
    //
    //         //获取解析对象
    //         JwtParser jwtParser = Jwts.parser();
    //         //解析并返回
    //         Jws<Claims> claimsJws = jwtParser.setSigningKey(SIGNATURE).parseClaimsJws(token);
    //         return claimsJws.getBody();
    //
    //         //验证成功不抛异常 直接通过
    //     } catch (ExpiredJwtException e) {
    //         throw new ParseTokenException("Token过期：" + e.getMessage());
    //     } catch (UnsupportedJwtException e) {
    //         throw new ParseTokenException("Token不支持：" + e.getMessage());
    //     } catch (MalformedJwtException e) {
    //         throw new ParseTokenException("Token被修改：" + e.getMessage());
    //     } catch (SignatureException e) {
    //         throw new ParseTokenException("Token签名不一致：" + e.getMessage());
    //     } catch (IllegalArgumentException e) {
    //         throw new ParseTokenException("Token参数非法：" + e.getMessage());
    //     } catch (Exception e) {
    //         throw new ParseTokenException("解析token是发生其他异常：" + e.getMessage());
    //     }
    // }
}
