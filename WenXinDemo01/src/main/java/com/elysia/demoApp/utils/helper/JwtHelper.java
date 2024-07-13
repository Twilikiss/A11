package com.elysia.demoApp.utils.helper;


import com.elysia.demoApp.exception.WenXinException;
import com.elysia.demoApp.model.result.utils.ResultCodeEnum;
import io.jsonwebtoken.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * 原代码采用的对称加密算法HS512，存在一定的安全问题
 * 这里替换成了非对称加密算法RS512
 * @author cxb
 * @ClassName JwtHelper
 * @date 10/5/2023 下午10:42
 */
@Component
public class JwtHelper {
    /**
     * 设置accessToken过期时间
     * 单位为毫秒
     * 有效时间设置为1天
     */
    private static final long ACCESS_TOKEN_EXPIRATION = 24*60*60*1000;
    /**
     * 设置refreshToken过期时间
     * 单位为毫秒
     * 有效时间设置为7天
     */
    private static final long REFRESH_TOKEN_EXPIRATION = 7*24*60*60*1000;
    /**
     * token的密钥
     */
    private static final String TOKEN_SIGN_KEY = "Elysia";
    /**
     * 密钥库密码
     */
    private static final String KEY_STATION_PASSWORD = "ElysiaLoveHuman";
    /**
     * 密钥密码
     */
    private static final String KEY_PASSWORD = "ElysiaLoveHuman";

    /**
     * 通过jwt生成对应的access_token和refresh_token
     * @ClassName JwtHelper
     * @author   cxb
     * @date  10/5/2023 下午10:48
     * @param uid  uid
     * @param userName name
     * @return Map token
     */
    public static Map<String, Object> createToken(String uid, String userName) {
        // 获取到私钥：位于resource下
        ClassPathResource keyFileResource = new ClassPathResource("Elysia.jks");
        // 创建私钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyFileResource,KEY_STATION_PASSWORD.toCharArray());
        KeyPair keyContent = keyStoreKeyFactory.getKeyPair("Elysia", KEY_PASSWORD.toCharArray());
        // 获取到私钥
        PrivateKey privateKey = keyContent.getPrivate();

        String accessToken = Jwts.builder()
                // 设置jwt-id
                .setId(String.valueOf(System.currentTimeMillis()))
                .setSubject("WENXIN-USER")
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .claim("uid", uid)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.RS512, privateKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();

        String refreshToken = Jwts.builder()
                // 设置jwt-id
                .setId(String.valueOf(System.currentTimeMillis()))
                .setSubject("WENXIN-USER")
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .claim("uid", uid)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.RS512, privateKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        Map<String, Object> token = new HashMap<>();
        token.put("access_token", accessToken);
        token.put("refresh_token", refreshToken);
        return token;
    }
    /**
     * 通过jwt解析获取其id
     * @ClassName JwtHelper
     * @author   cxb
     * @date  10/5/2023 下午10:49
     * @param token token值
     * @return java.lang.Long
     */
    public static String getUserUid(String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token);
        } catch (Exception e) {
            throw new WenXinException(ResultCodeEnum.SIGN_ERROR);
        }
        // 针对jwt的“加密算法置空”问题
        String algorithm = claimsJws.getHeader().getAlgorithm();
        if (!Objects.equals(algorithm, "RS512")) {
            throw new WenXinException(ResultCodeEnum.ACCESSTOKEN_ERROR);
        }
        Claims claims = claimsJws.getBody();
        String userId = (String) claims.get("uid");
        return userId;
    }
    /**
     * 通过jwt解析获取其name
     * @ClassName JwtHelper
     * @author   cxb
     * @date  10/5/2023 下午10:49
     * @param token token值
     * @return java.lang.Long
     */
    public static String getUserName(String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        // 针对jwt的“加密算法置空”问题
        String algorithm = claimsJws.getHeader().getAlgorithm();
        if (!Objects.equals(algorithm, "RS512")) {
            throw new WenXinException(ResultCodeEnum.PARAM_ERROR);
        }
        return (String) claims.get("userName");
    }
    /**
     * 获取公钥,便于后续验证
     * @ClassName JwtHelper
     * @author   cxb
     * @date  13/5/2023 下午12:17
     * @return java.security.PublicKey
     */
    public static PublicKey getPublicKey() {
        // 从resource中取得公钥文件
        Resource publicKey = new ClassPathResource("public.key");

        try {
            InputStreamReader reader = new InputStreamReader(publicKey.getInputStream());
            BufferedReader publicBr = new BufferedReader(reader);
            StringBuilder publicSb = new StringBuilder();
            String temp;
            while ((temp = publicBr.readLine()) != null) {
                publicSb.append(temp);
            }
            // 将string类型转换为java中的PublicKey
            byte[] byteKey = Base64.getDecoder().decode(publicSb.toString());
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            throw new WenXinException(ResultCodeEnum.PUBLIC_KEY_ERROR);
        }
    }
//    public static void main(String[] args) {
//        JwtHelper jwtHelper = new JwtHelper();
//        String token = jwtHelper.createToken(1L, "你永远喜欢爱莉希雅");
//        System.out.println(token);
//        System.out.println(jwtHelper.getUserId(token));
//        System.out.println(jwtHelper.getUserName(token));
//    }
}

