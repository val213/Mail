package com.example.backend.utils;

import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * JWT工具类
 * @author apelious
 */
public class JwtUtil {

    /**
     * Token的默认时限为一天
     */
    public static final Long JWT_TTL = 24 * 60 * 60 *1000L;

    /**
     * 秘钥明文
     */
    public static final String JWT_KEY = "micerlab";

    public static String getUUID(){
        return IdUtil.getSnowflakeNextIdStr();
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return JWT String
     */
    public static String createJWT(String subject, List<String> permissions, Map<String, String> authentications) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID(), permissions, authentications);
        return builder.compact();
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return JWT String
     */
    public static String createJWT(String subject, Long ttlMillis, List<String> permissions, Map<String, String> authentications) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID(), permissions, authentications);
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid, List<String> permissions, Map<String, String> authentications) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                //唯一的ID
                .setId(uuid)
                // 主题
                .setSubject(subject)
                // 签发者
                .setIssuer("apelious")
                // 签发时间
                .setIssuedAt(now)
                //使用HS256对称加密算法签名, 第二个参数为秘钥
                .signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate)
                //payload
                .claim("permissions", permissions)
                //payload
                .claim("authentications", authentications);
    }

    public static String createJWT(String subject, Long ttlMillis) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                //唯一的ID
                .setId(uuid)
                // 主题
                .setSubject(subject)
                // 签发者
                .setIssuer("apelious")
                // 签发时间
                .setIssuedAt(now)
                //使用HS256对称加密算法签名, 第二个参数为秘钥
                .signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate);
    }

    public static void main(String[] args) throws Exception
    {
        String jwt = createJWT("18038383020", null);
        Claims claims = parseJWT(jwt);
        String subject = claims.getSubject();
        System.out.println(subject);

    }


    /**
     * 创建token
     * @param id random UUID
     * @param subject JWT主题，目前存储的是用户的id
     * @param ttlMillis JWT持续的有效时间
     * @param permissions 用户权限信息
     * @param authentications 用户具体权限信息
     * @return JWT String
     */
    public static String createJWT(String id, String subject, Long ttlMillis, List<String> permissions, Map<String, String> authentications) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id, permissions, authentications);
        return builder.compact();
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return SecretKey Object
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 解析
     *
     * @param jwt JWT String
     * @return Claims Object
     * @throws Exception 解析错误则抛出异常
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


}